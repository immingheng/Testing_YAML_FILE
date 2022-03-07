package ibf2021.springboot.services;

import static ibf2021.springboot.utilities.SensitiveData.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ibf2021.springboot.models.Listing;
import ibf2021.springboot.repositories.ShopeeRepo;

import org.apache.commons.codec.binary.Hex;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class ShopeeAPICallService {

    @Autowired
    ShopeeRepo shopeeRepo;

    private static final Logger logger = Logger.getLogger(ShopeeAPICallService.class.getName());

    public String authHashHMACSHA256(String RequestEndPoint, String rawHttpBody, String APIKey) {
        if (APIKey == null) {
            logger.warning("SHOPEE SECRET API KEY IS NOT SET!");
        }
        String baseString = "%s|%s".formatted(RequestEndPoint, rawHttpBody);
        logger.info("BASESTRING>> " + baseString);
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(APIKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            return Hex.encodeHexString((mac.doFinal(baseString.getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to compute HMAC-SHA256");
        }
    }

    // GET EPOCH TIME STAMP BASED ON SYSTEM MACHINE
    public long getCurrentEpochTime() {
        logger.info("Current EPOCH TIME IS >>>" + System.currentTimeMillis() / 1000);
        return System.currentTimeMillis() / 1000;
    }

    // METHOD RETURNS UP TO A MAXIMUM OF 100 PRODUCTS - WILL HAVE TO IMPLEMENT
    // PAGINATION &/ SEARCH IN THE FUTURE FOR BOTH CLIENT AND SERVER SIDE
    public ResponseEntity<List<Listing>> getItemLists(Integer shop_id) {
        logger.info("SHOP ID PASSED INTO getItemLists method in SVC ->" + shop_id);

        List<Listing> listings = new ArrayList<>();

        String URI = "https://partner.test-stable.shopeemobile.com/api/v1/items/get";
        JsonObject requestBody = Json.createObjectBuilder()
                .add("pagination_offset", 0)
                .add("pagination_entries_per_page", 100)
                .add("shopid", shop_id)
                .add("partner_id", SHOPEE_PARTNER_ID)
                .add("timestamp", getCurrentEpochTime())
                .build();
        logger.info("REQUEST BODY THAT WAS BUILT FOR getItemLists -> " + requestBody.toString());

        RequestEntity<String> req = RequestEntity.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authHashHMACSHA256(URI, requestBody.toString(), SHOPEE_TEST_SECRET_KEY))
                .body(requestBody.toString(), String.class);
        logger.info("REQUEST ENTITY WITH HEADER AND ETC TO POST TO SHOPEE" + req.toString());
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        logger.info("RESPONSE FROM SHOPEE API ->" + resp.toString());

        // GET PRODUCT_ID

        JsonObject data = ResponseEntity2JSON(resp);
        // Convert ResponseEntity to JSON
        JsonArray jarray = data.getJsonArray("items");
        for (JsonValue item : jarray) {
            Listing listing = new Listing();
            int item_id = item.asJsonObject().getInt("item_id");

            logger.info("item_id >> " + item_id);

            // Converting getItemDetail response to JSON
            JsonObject itemDetail = ResponseEntity2JSON(getItemDetail(item_id, shop_id)).getJsonObject("item");

            // itemDetail = LISTING DETAIL
            String image = itemDetail.getJsonArray("images").get(0).toString();
            String description = itemDetail.getString("description");
            float price = itemDetail.getJsonNumber("price").longValue();
            String name = itemDetail.getString("name");
            int quantity = itemDetail.getInt("stock");
            int shopee_shop_id = itemDetail.getInt("shopid");

            logger.info("shopee_shop_id from itemDetail->" + shopee_shop_id);

            listing.setImage(image);
            listing.setDescription(description);
            listing.setPrice(price);
            listing.setProduct_name(name);
            listing.setQuantity(quantity);
            listing.setProduct_id(item_id);
            listing.setShopee_shop_id(shopee_shop_id);
            logger.info("SHOPEE SHOP ID AFTER SETTING IT IN LISTING -> " + listing.getShopee_shop_id());
            listings.add(listing);
        }
        // WANT TO ALSO BATCH UPDATE INTO DB TO STORE INDIVIDUAL LISTING IN mySQL

        shopeeRepo.batchUpdateListings2DB(listings);

        return ResponseEntity.ok().body(listings);
    }

    public JsonObject ResponseEntity2JSON(ResponseEntity<String> resp) {
        Reader reader = new StringReader(resp.getBody().toString());
        JsonReader JSONReader = Json.createReader(reader);
        JsonObject data = JSONReader.readObject();
        return data;
    }

    // Method to call after getting entire item lists to extract item_id and obtain
    // entire item detail to be saved into DB
    public ResponseEntity<String> getItemDetail(int itemId, Integer shop_id) {
        String URI = "https://partner.test-stable.shopeemobile.com/api/v1/item/get";
        JsonObject requestBody = Json.createObjectBuilder()
                .add("item_id", itemId)
                .add("shopid", shop_id)
                .add("partner_id", SHOPEE_PARTNER_ID)
                .add("timestamp", getCurrentEpochTime())
                .build();
        logger.info("REQUEST BODY THAT WAS BUILT FOR getItemDetail -> " + requestBody.toString());
        RequestEntity<String> req = RequestEntity.post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authHashHMACSHA256(URI, requestBody.toString(), SHOPEE_TEST_SECRET_KEY))
                .body(requestBody.toString(), String.class);
        logger.info("REQUEST ENTITY WITH HEADER AND ETC TO POST TO SHOPEE" + req.toString());
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        logger.info("RESPONSE FROM SHOPEE API ->" + resp.toString());
        return resp;
    }

    // INCOMPLETE - TO BE IMPLEMENTED IN THE FUTURE
    // Assumes that there will only be ONE IMAGE URL else have to carry out a loop
    // to handle a Collection of image URL accordingly
    public ResponseEntity<String> createItem(String imageURL, String shop_id) {
        // To upload an item to SHOPEE, the API call flow goes like this:
        // 1. Call into Image.UploadImg to convert URL image to Shopee image. (Image
        // Hosting Website Images encapsulated with [""])
        String ImageURI = "https://partner.test-stable.shopeemobile.com/api/v1/image/upload";
        JsonArray images = Json.createArrayBuilder()
                .add("[\"" + imageURL + "\"]").build();
        JsonObject requestBody = Json.createObjectBuilder()
                .add("images", images)
                .add("shopid", Integer.parseInt(shop_id))
                .add("partner_id", SHOPEE_PARTNER_ID)
                .add("timestamp", getCurrentEpochTime())
                .build();
        logger.info("REQUEST BODY THAT WAS BUILT FOR image.uploadImg -> " + requestBody.toString());
        RequestEntity<String> req = RequestEntity.post(ImageURI)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authHashHMACSHA256(ImageURI, requestBody.toString(), SHOPEE_TEST_SECRET_KEY))
                .body(requestBody.toString(), String.class);
        logger.info("REQUEST ENTITY WITH HEADER AND ETC TO POST TO SHOPEE" + req.toString());
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        logger.info("RESPONSE FROM SHOPEE API ->" + resp.toString());

        String shopeeImgUrl = ResponseEntity2JSON(resp).getJsonArray("images").get(0).asJsonObject()
                .getString("shopee_image_url");
        if (shopeeImgUrl.isEmpty()) {
            String error_desc = ResponseEntity2JSON(resp).getJsonArray("images").get(0).asJsonObject()
                    .getString("error_desc");
            logger.warning("image.UploadImg failed due to " + error_desc);
        }

        // 2. Fetch categories and select a suitable category_id in order to fetch
        // category attribute
        // String CategoryURI =
        // "https://partner.test-stable.shopeemobile.com/api/v1/shop_categorys/get";

        // 3. Collect attribute for categoryid with endpoint item.GetAttribute
        // 4. Call into logistics.GetLogistics to get information of logistics supported
        // by the shop.
        // 5. With all the information, call into item.Add end point to upload product -
        // if the product is successfully uploaded, an item_id will generated and
        // returned.

        return null;
    }

}
