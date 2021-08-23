package avallario.shoppingcart.repository;

import avallario.shoppingcart.model.Cart;
import avallario.shoppingcart.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InMemoryCartRepositoryTest {

    private static final Product PRODUCT_COMB = new Product("1000", "Comb", 4.99);
    private static final Product PRODUCT_BRUSH = new Product("1001", "Brush", 6.99);
    private static final Product PRODUCT_SHAMPOO = new Product("1002", "Shampoo", 5.49);

    private InMemoryCartRepository cartRepository;

    @BeforeEach
    public void setup() {

        Cart cart1 = new Cart("2000", List.of(PRODUCT_COMB, PRODUCT_BRUSH));
        Cart cart2 = new Cart("2001", List.of(PRODUCT_SHAMPOO));

        Map<String, Cart> cartMap = new HashMap<>();
        cartMap.put(cart1.getId(), cart1);
        cartMap.put(cart2.getId(), cart2);

        this.cartRepository = new InMemoryCartRepository(cartMap);
    }

    @Test
    public void testGetExistingCart() {

        Cart cart = this.cartRepository.getCart("2001");
        assertNotNull(cart);
        assertEquals("2001", cart.getId());
    }

    @Test
    public void testGetNonExistingCart() {

        assertNull(this.cartRepository.getCart("0"));
    }

    @Test
    public void testUpdateNewCart() {

        Cart newCart = new Cart("2002", List.of());
        this.cartRepository.updateCart(newCart);
        assertNotNull(this.cartRepository.getCart(newCart.getId()));
    }

    @Test
    public void testUpdateExistingCart() {

        Cart updatedCart = new Cart("2001", List.of(PRODUCT_BRUSH, PRODUCT_SHAMPOO));
        this.cartRepository.updateCart(updatedCart);
        assertCartContainsProduct(this.cartRepository.getCart("2001"), PRODUCT_BRUSH.getId());
    }

    @Test
    public void deleteExistingCart() {

        assertTrue(this.cartRepository.deleteCart("2001"));
        assertNull(this.cartRepository.getCart("2001"));
    }

    @Test
    public void deleteNonExistingCart() {

        assertFalse(this.cartRepository.deleteCart("0"));
    }

    private void assertCartContainsProduct(Cart cart, String productId) {

        assertTrue(cart.getProducts().stream().anyMatch(product -> productId.equals(product.getId())));
    }

}
