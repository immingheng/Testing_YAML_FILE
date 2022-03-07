package ibf2021.springboot.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ibf2021.springboot.models.User;

import static ibf2021.springboot.mySQL.SQL.*;

@Repository
public class UserRepo {

    @Autowired
    private JdbcTemplate template;

    public int addUser2mySQL(String email, String name, int shopee_shop_id) {
        // insert into user(name, email, shopee_shop_id) values (?, ? ,?)"
        return this.template.update(SQL_SAVE_USER_TO_MYSQL, name, email, shopee_shop_id);
    }

    public int getShopeeShopId(User user) {
        // return this.template.queryForRowSet(sql)
        SqlRowSet rs = template.queryForRowSet(SQL_CHECK_USER_EXISTS, user.getEmail());
        rs.next();
        int shopee_shop_id = rs.getInt("shopee_shop_id");
        return shopee_shop_id;
    }

}
