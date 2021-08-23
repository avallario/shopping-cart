package avallario.shoppingcart.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    public String id;
    public List<Product> products;

    public Cart() {
    }

    public Cart(String id, List<Product> products) {
        this.id = id;
        this.products = products;
    }

    public String getId() {
        return this.id;
    }

    public List<Product> getProducts() {
        return this.products;
    }

}
