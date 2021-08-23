package avallario.shoppingcart.resource;

import avallario.shoppingcart.model.Cart;
import avallario.shoppingcart.model.Product;
import avallario.shoppingcart.service.CartService;

import javax.ws.rs.*;
import java.util.List;

@Path("/carts/{cartId}")
public class CartResource {

    private final CartService cartService;

    public CartResource(CartService cartService) {

        this.cartService = cartService;
    }

    @GET
    public Cart getCart(@PathParam("cartId") String cartId) {

        return this.cartService.getCart(cartId);
    }

    @DELETE
    @Path("/products/{productId}")
    public void removeProductFromCart(@PathParam("cartId") String cartId, @PathParam("productId") String productId) {

        this.cartService.removeProductFromCart(cartId, productId);
    }

    @POST
    @Path("/products")
    public void addProductToCart(@PathParam("cartId") String cartId, Product product) {

        this.cartService.addProductToCart(cartId, product);
    }

    @DELETE
    public void deleteCart(@PathParam("cartId") String cartId) {

        this.cartService.deleteCart(cartId);
    }
}
