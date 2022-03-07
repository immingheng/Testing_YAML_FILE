package ibf2021.springboot.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

// Model of what I want to extract from shopee api call to be passed back to front-end
public class Listing {
    String image;
    String product_name;
    String description;
    int quantity;
    float price;
    int product_id;
    int shopee_shop_id;

    public Listing(String image, String product_name, String description, int quantity, float price, int product_id,
            int shopee_shop_id) {
        this.image = image;
        this.product_name = product_name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.product_id = product_id;
        this.shopee_shop_id = shopee_shop_id;
    }

    public Listing() {
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_name() {
        return this.product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getProduct_id() {
        return this.product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getShopee_shop_id() {
        return this.shopee_shop_id;
    }

    public void setShopee_shop_id(int shopee_shop_id) {
        this.shopee_shop_id = shopee_shop_id;
    }

    public static Listing populate(SqlRowSet rs) {
        Listing listing = new Listing();
        listing.setDescription(rs.getString("product_description"));
        listing.setImage(rs.getString("image_thumbnail"));
        listing.setPrice(rs.getFloat("price"));
        listing.setProduct_id(rs.getInt("product_id"));
        listing.setProduct_name(rs.getString("product_name"));
        listing.setQuantity(rs.getInt("quantity"));
        listing.setShopee_shop_id(rs.getInt("shopee_shop_id"));
        return listing;
    }

}