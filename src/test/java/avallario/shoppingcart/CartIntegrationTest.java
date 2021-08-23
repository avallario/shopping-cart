package avallario.shoppingcart;

import avallario.shoppingcart.model.Cart;
import avallario.shoppingcart.model.Product;
import avallario.shoppingcart.repository.CartRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartIntegrationTest {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String COMB_JSON = "{\"id\":\"1000\",\"name\":\"Comb\",\"price\":4.99}";
    private static final String BRUSH_JSON = "{\"id\":\"1001\",\"name\":\"Brush\",\"price\":6.99}";
    private static final String SHAMPOO_JSON = "{\"id\":\"1002\",\"name\":\"Shampoo\",\"price\":5.49}";
    private static final String CART_1_JSON = "{\"id\":\"2000\",\"products\":[{\"id\":\"1000\",\"name\":\"Comb\",\"price\":4.99},{\"id\":\"1001\",\"name\":\"Brush\",\"price\":6.99}]}";
    private static final String CART_2_JSON = "{\"id\":\"2001\",\"products\":[{\"id\":\"1002\",\"name\":\"Shampoo\",\"price\":5.49}]}";

    @Inject
    CartRepository cartRepository;

    @BeforeEach
    public void setup() {

        Product combProduct = new Product("1000", "Comb", 4.99);
        Product brushProduct = new Product("1001", "Brush", 6.99);
        Product shampooProduct = new Product("1002", "Shampoo", 5.49);
        Cart cart1 = new Cart("2000", new ArrayList<>(List.of(combProduct, brushProduct)));
        Cart cart2 = new Cart("2001", new ArrayList<>(List.of(shampooProduct)));

        cartRepository.deleteCart("0");
        cartRepository.updateCart(cart1);
        cartRepository.updateCart(cart2);
    }

    @Test
    public void testGetCart1() {

        given()
                .when()
                .get("/carts/2000")
                .then()
                .statusCode(200)
                .body(equalTo(CART_1_JSON));
    }

    @Test
    public void testGetCart2() {

        given()
                .when()
                .get("/carts/2001")
                .then()
                .statusCode(200)
                .body(equalTo(CART_2_JSON));
    }

    @Test
    public void testGetCartNotFound() {

        given()
                .when()
                .get("/carts/0")
                .then()
                .statusCode(404);
    }

    @Test
    public void testRemoveProductFromCart() {

        given()
                .when()
                .delete("/carts/2000/products/1000")
                .then()
                .statusCode(204);

        Cart cart = this.cartRepository.getCart("2000");
        assertEquals(1, cart.getProducts().size());
        assertEquals("1001", cart.getProducts().get(0).getId());
    }

    @Test
    public void testRemoveProductFromCartNotFound() {

        given()
                .when()
                .delete("/carts/0/products/1000")
                .then()
                .statusCode(404);
    }

    @Test
    public void testAddProductToCart() {

        given()
                .contentType(JSON_CONTENT_TYPE)
                .body(SHAMPOO_JSON)
                .when()
                .post("/carts/2000/products")
                .then()
                .statusCode(204);

        Cart cart = this.cartRepository.getCart("2000");
        assertEquals(3, cart.getProducts().size());

        boolean cartContainsShampoo = cart.getProducts()
                .stream()
                .anyMatch(product -> "1002".equals(product.getId()));
        assertTrue(cartContainsShampoo);
    }

    @Test
    public void testAddProductToCartCreatesNewCart() {

        given()
                .contentType(JSON_CONTENT_TYPE)
                .body(COMB_JSON)
                .when()
                .post("/carts/0/products")
                .then()
                .statusCode(204);

        Cart cart = this.cartRepository.getCart("0");
        assertNotNull(cart);
    }

    @Test
    public void testDeleteCart() {

        given()
                .when()
                .delete("/carts/2001")
                .then()
                .statusCode(204);

        Cart cart = this.cartRepository.getCart("2001");
        assertNull(cart);
    }

    @Test
    public void testDeleteCartNotFound() {

        given()
                .when()
                .delete("/carts/0")
                .then()
                .statusCode(404);
    }

}
