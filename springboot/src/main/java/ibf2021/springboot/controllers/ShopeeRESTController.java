package ibf2021.springboot.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf2021.springboot.models.Listing;
import ibf2021.springboot.models.User;
import ibf2021.springboot.repositories.UserRepo;
import ibf2021.springboot.services.AuthService;
import ibf2021.springboot.services.ShopeeAPICallService;

@RestController
@RequestMapping(path = "/api/shopee", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopeeRESTController {

    private static final Logger logger = Logger.getLogger(ShopeeRESTController.class.getName());

    @Autowired
    ShopeeAPICallService shopeeSvc;

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthService authSvc;

    @GetMapping(path = "listings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Listing>> getAllListing(@RequestHeader("authorization") String jwt,
            @RequestParam Integer shop_id) {
        // CHECK IF shop_id exists in DB,
        // If so, retrieve its data from DB
        // else make a call into shopee and retrieve them?
        logger.info("SHOP ID FROM FRONTEND IS ->" + shop_id);

        User user = authSvc.extractUserFromJWT(jwt);
        user.setShopee_shop_id((shop_id));
        // INSERT USER INTO DB
        userRepo.addUser2mySQL(user.getEmail(), user.getName(), user.getShopee_shop_id());

        return shopeeSvc.getItemLists(shop_id);
    }

    // DEPRECATED - SANITISED THE VALUES BEFORE SENDING IT BACK TO THE CLIENT SIDE
    // @GetMapping(path = "listing/{itemId}", produces =
    // MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> getItemDetail(@PathVariable int itemId) {
    // return shopeeSvc.getItemDetail(itemId);
    // }

}
