package ibf2021.springboot.mySQL;

public class SQL {

    public static final String SQL_SAVE_USER_TO_MYSQL = "insert ignore into user(name, email, shopee_shop_id) values (?, ? ,?)";

    public static final String SQL_CHECK_USER_EXISTS = "select shopee_shop_id from user where email = ?";

    public static final String SQL_ADD_LISTING_TO_MYSQL = "insert ignore into shopee(product_id, shopee_shop_id, image_thumbnail, product_name, product_description, quantity, price) values (?,?,?,?,?,?,?)";

    public static final String SQL_GET_ALL_LISTING_FROM_MYSQL = "select * from shopee where shopee_shop_id = ?";

}