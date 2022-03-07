package ibf2021.springboot.models;

public class User {
    String name;
    String email;
    int shopee_shop_id;

    public User() {
    }

    public User(String name, String email, int shopee_shop_id) {
        this.name = name;
        this.email = email;
        this.shopee_shop_id = shopee_shop_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getShopee_shop_id() {
        return this.shopee_shop_id;
    }

    public void setShopee_shop_id(int shopee_shop_id) {
        this.shopee_shop_id = shopee_shop_id;
    }

}
