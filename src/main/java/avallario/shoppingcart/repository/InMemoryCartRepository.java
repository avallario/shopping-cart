package avallario.shoppingcart.repository;

import avallario.shoppingcart.model.Cart;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class InMemoryCartRepository implements CartRepository {

    private Map<String, Cart> cartMap = Collections.synchronizedMap(new HashMap<>());

    public InMemoryCartRepository() {
    }

    public InMemoryCartRepository(Map<String, Cart> cartMap) {

        this.cartMap.putAll(cartMap);
    }

    @Override
    public Cart getCart(String id) {

        return this.cartMap.get(id);
    }

    @Override
    public void updateCart(Cart cart) {

        this.cartMap.put(cart.getId(), cart);
    }

    @Override
    public boolean deleteCart(String id) {

        Cart cart = this.cartMap.remove(id);
        return cart != null;
    }
}
