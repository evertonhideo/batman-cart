package br.com.batman.cart.controller;

import br.com.batman.cart.model.Cart;
import br.com.batman.cart.model.request.CartCheckoutRequest;
import br.com.batman.cart.model.request.CartItemRequest;
import br.com.batman.cart.model.request.CartRequest;
import br.com.batman.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    CartService service;

    //@Autowired
    //private ModelMapper modelMapper;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createCart(@Valid @RequestBody CartRequest cartRequest) {
        Cart createdCart = service.createCart(cartRequest);
        return ResponseEntity.ok(createdCart);
    }

    @PatchMapping("/{id}/items")
    @ResponseBody
    public ResponseEntity<?> addItem(@PathVariable String id, @Valid @RequestBody CartItemRequest cartItemRequest) throws Exception {
        Cart updatedCart = service.addNewItem(id, cartItemRequest);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCart(@PathVariable String id) throws Exception {
        Cart canceledCart = service.cancelCart(id);
        return ResponseEntity.ok(canceledCart);
    }

    @DeleteMapping("/{id}/items/{item_id}")
    @ResponseBody
    public ResponseEntity<?> deleteCartItem(@PathVariable(value = "id") String id,
                                            @PathVariable(value = "item_id") String itemId) throws Exception {
        Cart updatedCart = service.removeCartItem(id, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<?> checkout(@PathVariable String id, @Valid @RequestBody CartCheckoutRequest cartCheckoutRequest, @RequestHeader("x-team-control") String teamName) throws Exception {
        Cart cart = service.checkout(id, cartCheckoutRequest, teamName);
        return ResponseEntity.ok(cart);
    }

}
