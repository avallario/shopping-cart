package avallario.shoppingcart.model;

import java.util.List;

public class Cart {

    private String id;
    private List<Product> products;

    public Cart(String id, List<Product> products) {
        this.id = id;
        this.products = products;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
