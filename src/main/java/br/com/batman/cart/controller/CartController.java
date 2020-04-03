package br.com.batman.cart.controller;

import br.com.batman.cart.model.Cart;
import br.com.batman.cart.model.request.CartCheckoutRequest;
import br.com.batman.cart.model.request.CartItemRequest;
import br.com.batman.cart.model.request.CartRequest;
import br.com.batman.cart.service.CartService;
import br.com.batman.cart.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    CheckoutService checkoutService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createCart(@Valid @RequestBody CartRequest cartRequest) {
        Cart createdCart = cartService.createCart(cartRequest);
        return ResponseEntity.ok(createdCart);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public  ResponseEntity<?> getCart(@PathVariable String id) throws  Exception {
        Cart getCart = cartService.getCartById(id);
        return ResponseEntity.ok(getCart);
    }

    @PatchMapping("/{id}/items")
    @ResponseBody
    public ResponseEntity<?> addItem(@PathVariable String id, @Valid @RequestBody CartItemRequest cartItemRequest) throws Exception {
        Cart updatedCart = cartService.addNewItem(id, cartItemRequest);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCart(@PathVariable String id) throws Exception {
        Cart canceledCart = cartService.cancelCart(id);
        return ResponseEntity.ok(canceledCart);
    }

    @DeleteMapping("/{id}/items/{item_id}")
    @ResponseBody
    public ResponseEntity<?> deleteCartItem(@PathVariable(value = "id") String id,
                                            @PathVariable(value = "item_id") String itemId) throws Exception {
        Cart updatedCart = cartService.removeCartItem(id, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<?> checkout(@PathVariable String id, @Valid @RequestBody CartCheckoutRequest cartCheckoutRequest, @RequestHeader("x-team-control") String teamName) throws Exception {
        Cart cart = checkoutService.checkout(id, cartCheckoutRequest, teamName);
        return ResponseEntity.ok(cart);
    }
}
