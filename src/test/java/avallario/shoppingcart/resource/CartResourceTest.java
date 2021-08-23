package avallario.shoppingcart.resource;

import avallario.shoppingcart.model.Cart;
import avallario.shoppingcart.model.Product;
import avallario.shoppingcart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartResourceTest {

    private CartResource cartResource;
    private CartService cartService;

    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

    @BeforeEach
    public void setup() {

        this.cartService = mock(CartService.class);
        this.cartResource = new CartResource(this.cartService);
    }

    @Test
    public void testGetCart() {

        when(this.cartService.getCart("2000")).thenReturn(new Cart("2000", List.of()));

        Cart cart = this.cartResource.getCart("2000");

        verify(this.cartService, times(1)).getCart("2000");
        assertEquals("2000", cart.getId());
    }

    @Test
    public void testRemoveProductFromCart() {

        this.cartResource.removeProductFromCart("2000", "1000");
        verify(this.cartService, times(1)).removeProductFromCart("2000", "1000");
    }

    @Test
    public void testAddProductToCart() {

        this.cartResource.addProductToCart("2000", new Product("1000", "Shampoo", 5.49));
        verify(this.cartService, times(1)).addProductToCart(eq("2000"), productCaptor.capture());
        assertEquals("1000", productCaptor.getValue().getId());
    }

    @Test
    public void testDeleteCart() {

        this.cartResource.deleteCart("2000");
        verify(this.cartService, times(1)).deleteCart("2000");
    }

}
