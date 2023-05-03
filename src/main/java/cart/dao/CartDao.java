package cart.dao;

import java.util.List;

import cart.entity.CartEntity;
import cart.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartDao {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;


    public CartDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("CART").usingGeneratedKeyColumns("cart_id");
    }

    public long insert(CartEntity cartEntity) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("product_id", cartEntity.getProductId())
                .addValue("member_id", cartEntity.getMemberId());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<ProductEntity> selectAllProductByMemberId(Long memberId) {
        String sql = "SELECT * FROM (SELECT CART.* FROM CART INNER JOIN MEMBER ON CART.member_id = MEMBER.member_id) AS CM " +
                "INNER JOIN PRODUCT ON CM.product_id = PRODUCT.product_id " +
                "WHERE CM.member_id = ?";
        return jdbcTemplate.query(sql, productEntityRowMapper(), memberId);
    }

    private RowMapper<ProductEntity> productEntityRowMapper() {
        return (rs, rowNum) ->
                new ProductEntity.Builder()
                        .productId(rs.getLong("product_id"))
                        .name(rs.getString("name"))
                        .imgUrl(rs.getString("img_url"))
                        .price(rs.getInt("price"))
                        .build();
    }
}