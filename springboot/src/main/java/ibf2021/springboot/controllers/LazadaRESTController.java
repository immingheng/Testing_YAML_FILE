package ibf2021.springboot.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/lazada", produces = MediaType.APPLICATION_JSON_VALUE)
public class LazadaRESTController {

    @GetMapping()
    public ResponseEntity<String> getAllListing() {
        return null;
    }

    @PostMapping()
    public ResponseEntity<String> addProduct() {
        return null;
    }

}
