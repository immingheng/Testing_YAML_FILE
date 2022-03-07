package ibf2021.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf2021.springboot.models.Listing;
import ibf2021.springboot.models.User;
import ibf2021.springboot.repositories.ShopeeRepo;
import ibf2021.springboot.repositories.UserRepo;
import ibf2021.springboot.services.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class authenticationRESTController {

    private static final Logger logger = Logger.getLogger(authenticationRESTController.class.getName());

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthService authSvc;

    @Autowired
    ShopeeRepo shopeeRepo;

    @GetMapping(path = "/user/shopee_shop_id")
    public ResponseEntity<Integer> getShopeeShopID(@RequestHeader("authorization") String jwt) {
        logger.info(jwt);
        User user = authSvc.extractUserFromJWT(jwt);
        logger.info(user.toString());
        try {
            int shopee_shop_id = userRepo.getShopeeShopId(user);
            return ResponseEntity.ok().body(shopee_shop_id);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("USER DOES NOT HAVE A SHOPEE LINKED IN THE DATABASE.");
            return ResponseEntity.ok().body(0);
        }
    }

    @GetMapping(path = "/user/shopeeItems")
    public ResponseEntity<List<Listing>> getAllShopeeListings(@RequestParam int shopee_shop_id) {
        List<Listing> shopeeItems = new ArrayList<>();
        // QUERY INTO DB TO GET LISTINGS
        shopeeItems = shopeeRepo.getListingsFromDB(shopee_shop_id);
        logger.info("First item's name --> " + shopeeItems.get(0).getProduct_name());
        return ResponseEntity.ok().body(shopeeItems);
    }

}
