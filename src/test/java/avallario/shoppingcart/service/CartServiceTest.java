package avallario.shoppingcart.service;

import avallario.shoppingcart.exception.ResourceNotFoundException;
import avallario.shoppingcart.model.Cart;
import avallario.shoppingcart.model.Product;
import avallario.shoppingcart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartServiceTest {

    private CartService cartService;
    private CartRepository cartRepository;

    private ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);

    @BeforeEach
    public void setup() {

        this.cartRepository = mock(CartRepository.class);
        this.cartService = new CartService(this.cartRepository);
    }

    @Test
    public void testGetCart() {

        when(this.cartRepository.getCart("2000")).thenReturn(new Cart("2000", List.of()));
        Cart cart = this.cartService.getCart("2000");

        assertNotNull(cart);
        assertEquals("2000", cart.getId());
        verify(this.cartRepository, times(1)).getCart("2000");
    }

    @Test
    public void testGetCartNotFound() {

        assertThrows(ResourceNotFoundException.class, () -> this.cartService.getCart("0"));
    }

    @Test
    public void testRemoveProductFromCart() {

        Product product = new Product("1000", "Shampoo", 5.49);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Cart cart = new Cart("2000", productList);

        when(this.cartRepository.getCart("2000")).thenReturn(cart);

        this.cartService.removeProductFromCart("2000", "1000");

        verify(this.cartRepository, times(1)).getCart("2000");
        verify(this.cartRepository, times(1)).updateCart(cartCaptor.capture());
        assertTrue(cartCaptor.getValue().getProducts().isEmpty());
    }

    @Test
    public void testRemoveProductFromCartRemovesOnlyFirstInstance() {

        Product product = new Product("1000", "Shampoo", 5.49);
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product);
        Cart cart = new Cart("2000", productList);

        when(this.cartRepository.getCart("2000")).thenReturn(cart);

        this.cartService.removeProductFromCart("2000", "1000");

        verify(this.cartRepository, times(1)).getCart("2000");
        verify(this.cartRepository, times(1)).updateCart(cartCaptor.capture());
        assertFalse(cartCaptor.getValue().getProducts().isEmpty());
    }

    @Test
    public void testRemoveProductFromCartNotFound() {

        assertThrows(ResourceNotFoundException.class, () -> this.cartService.removeProductFromCart("0", "1"));
    }

    @Test
    public void testAddProductToCart() {

        Product product = new Product("1000", "Shampoo", 5.49);
        List<Product> productList = new ArrayList<>();
        Cart cart = new Cart("2000", productList);

        when(this.cartRepository.getCart("2000")).thenReturn(cart);

        this.cartService.addProductToCart("2000", product);

        verify(this.cartRepository, times(1)).getCart("2000");
        verify(this.cartRepository, times(1)).updateCart(cartCaptor.capture());
        assertFalse(cartCaptor.getValue().getProducts().isEmpty());
    }

    @Test
    public void testAddProductToCartNotFoundCreatesNewCart() {

        Product product = new Product("1000", "Shampoo", 5.49);

        when(this.cartRepository.getCart("2000")).thenReturn(null);

        this.cartService.addProductToCart("2000", product);

        verify(this.cartRepository, times(1)).getCart("2000");
        verify(this.cartRepository, times(1)).updateCart(cartCaptor.capture());
        assertEquals("2000", cartCaptor.getValue().getId());
    }

    @Test
    public void testDeleteCart() {

        when(this.cartRepository.deleteCart("2000")).thenReturn(true);
        this.cartService.deleteCart("2000");
        verify(this.cartRepository, times(1)).deleteCart("2000");
    }

    @Test
    public void testDeleteCartNotFound() {

        assertThrows(ResourceNotFoundException.class, () -> this.cartService.deleteCart("0"));
    }

}
