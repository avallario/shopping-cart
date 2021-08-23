package avallario.shoppingcart.service;

import avallario.shoppingcart.exception.ResourceNotFoundException;
import avallario.shoppingcart.model.Cart;
import avallario.shoppingcart.model.Product;
import avallario.shoppingcart.repository.CartRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCart(String cartId) {

        Cart cart = this.cartRepository.getCart(cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("No cart found with id " + cartId);
        }

        return cart;
    }

    public void removeProductFromCart(String cartId, String productId) {

        Cart cart = this.cartRepository.getCart(cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("No cart found with id " + cartId);
        }

        cart.getProducts()
                .stream()
                .filter(product -> productId.equals(product.getId()))
                .findFirst()
                .ifPresent(product -> cart.getProducts().remove(product));

        this.cartRepository.updateCart(cart);
    }

    public void addProductToCart(String cartId, Product product) {

        Cart cart = this.cartRepository.getCart(cartId);

        if (cart == null) {
            cart = new Cart(cartId, new ArrayList<>());
        }

        cart.getProducts().add(product);

        this.cartRepository.updateCart(cart);
    }

    public void deleteCart(String cartId) {

        if (!this.cartRepository.deleteCart(cartId)) {
            throw new ResourceNotFoundException("No cart found with id " + cartId);
        }
    }
}
