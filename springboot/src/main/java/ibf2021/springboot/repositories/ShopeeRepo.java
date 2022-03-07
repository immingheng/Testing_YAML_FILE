package ibf2021.springboot.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ibf2021.springboot.models.Listing;

import static ibf2021.springboot.mySQL.SQL.*;

@Repository
public class ShopeeRepo {

    @Autowired
    JdbcTemplate template;

    private final static Logger logger = Logger.getLogger(ShopeeRepo.class.getName());

    // insert ignore into shopee(product_id, shopee_shop_id, image_thumbnail,
    // product_name, product_description, quantity, price) values (?,?,?,?,?,?,?)
    public void batchUpdateListings2DB(List<Listing> listings) {
        template.batchUpdate(SQL_ADD_LISTING_TO_MYSQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Listing listing = listings.get(i);

                ps.setInt(1, listing.getProduct_id());
                logger.info("prod_id ->" + listing.getProduct_id());

                ps.setInt(2, listing.getShopee_shop_id());
                logger.info("shop_id ->" + listing.getShopee_shop_id());

                ps.setString(3, listing.getImage());
                logger.info("image url ->" + listing.getImage());

                ps.setString(4, listing.getProduct_name());
                logger.info("product name ->" + listing.getProduct_name());

                ps.setString(5, listing.getDescription());
                logger.info("product description ->" + listing.getDescription());

                ps.setInt(6, listing.getQuantity());
                logger.info("product qty ->" + listing.getQuantity());

                ps.setFloat(7, listing.getPrice());
                logger.info("price ->" + listing.getPrice());
            }

            @Override
            public int getBatchSize() {
                return listings.size();
            }
        });

    }

    // RETRIEVE ENTIRE LISTINGS FROM DB
    public List<Listing> getListingsFromDB(int shopee_shop_id) {
        List<Listing> listings = new ArrayList<>();
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ALL_LISTING_FROM_MYSQL, shopee_shop_id);
        while (rs.next()) {
            // set all fields and return a fully populated listing
            listings.add(Listing.populate(rs));
        }
        return listings;
    }
}
