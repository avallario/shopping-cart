package avallario.shoppingcart.repository;

import avallario.shoppingcart.model.Cart;
import avallario.shoppingcart.model.Product;

public interface CartRepository {

    Cart getCart(String id);
    void updateCart(Cart cart);
    boolean deleteCart(String id);

}
