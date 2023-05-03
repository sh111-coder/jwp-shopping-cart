package cart.service;


import static cart.fixture.MemberFixtures.INSERT_MEMBER_ENTITY;
import static cart.fixture.MemberFixtures.MEMBER_AUTH_REQUEST;
import static cart.fixture.ProductFixtures.DUMMY_SEONGHA_ID;
import static cart.fixture.ProductFixtures.INSERT_PRODUCT_ENTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dto.ProductResponse;
import cart.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("장바구니 상품 추가 시 사용자 정보로 조회한 사용자가 없으면 예외가 발생한다.")
    void saveProduct_throw_not_found_member() {
        // when, then
        assertThatThrownBy(() -> cartService.saveProduct(MEMBER_AUTH_REQUEST, DUMMY_SEONGHA_ID))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("사용자 인증 정보에 해당하는 사용자가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("사용자 정보와 상품 ID로 장바구니에 상품을 추가한다.")
    void saveProduct() {
        // given
        long insertedProductId = productDao.insert(INSERT_PRODUCT_ENTITY);
        long insertedMemberId = memberDao.insert(INSERT_MEMBER_ENTITY);

        // when, then
        assertDoesNotThrow(() -> cartService.saveProduct(MEMBER_AUTH_REQUEST, insertedProductId));
    }

    @Test
    @DisplayName("사용자 정보로 장바구니 목록을 조회한다.")
    void findAllProductByMemberInfo() {
        // given
        long insertedProductId = productDao.insert(INSERT_PRODUCT_ENTITY);
        long insertedMemberId = memberDao.insert(INSERT_MEMBER_ENTITY);

        // when
        cartService.saveProduct(MEMBER_AUTH_REQUEST, insertedProductId);
        List<ProductResponse> allProductByMemberInfo = cartService.findAllProductByMemberInfo(MEMBER_AUTH_REQUEST);

        // then
        assertAll(
                () -> assertThat(allProductByMemberInfo).hasSize(1),
                () -> assertThat(allProductByMemberInfo.get(0).getId()).isEqualTo(insertedProductId)
        );
    }
}
