package br.com.batman.cart.service;

import br.com.batman.cart.model.*;
import br.com.batman.cart.model.request.CartCheckoutRequest;
import br.com.batman.cart.model.request.CartItemRequest;
import br.com.batman.cart.model.request.CartRequest;
import br.com.batman.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    ProductService productService;

    @Autowired
    CartRepository repository;

    @Autowired
    CurrencyService currencyService;

    public Cart createCart(CartRequest cartRequest) {

        Cart cart = Cart.builder()
                .customerId(cartRequest.getCustomerId())
                .status(Status.PENDING)
                .items(new ArrayList<CartItem>())
                .build();

        //Product product = productService.findProductBySky(cartRequest.getItem().getSku());
        Product product = new Product();
        product.setId("123");
        product.setSku(cartRequest.getItem().getSku());
        product.setPrice(new Price());
        product.getPrice().setAmount(12);
        product.getPrice().setCurrencyCode("USD");
        product.getPrice().setScale(2);

        cart.getItems().add(CartItem.builder()
                .id(product.getId())
                .price(product.getPrice().getAmount())
                .quantity(cartRequest.getItem().getQuantity())
                .currencyCode(product.getPrice().getCurrencyCode())
                .scale(product.getPrice().getScale())
                .build());

        return repository.save(cart);
    }

    public Cart addNewItem(Long id, CartItemRequest cartItemRequest) throws Exception {

        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        Product product = productService.findProductBySky(cartItemRequest.getSku());
        Boolean foundedProduct = false;

        for (CartItem it : cart.getItems()) {
            if (it.getId() == product.getId()) {
                it.setQuantity(it.getQuantity() + cartItemRequest.getQuantity());
                foundedProduct = true;
                break;
            }
        }

        if (!foundedProduct) {
            cart.getItems().add(CartItem.builder()
                    .id(product.getId())
                    .price(product.getPrice().getAmount())
                    .quantity(cartItemRequest.getQuantity())
                    .currencyCode(product.getPrice().getCurrencyCode())
                    .scale(product.getPrice().getScale())
                    .build());
        }

        return repository.save(cart);
    }


    public Cart cancelCart(Long id) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        cart.setStatus(Status.CANCEL);
        return repository.save(cart);
    }

    public Cart removeCartItem(Long id, Long itemId) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        cart.getItems().removeIf(it -> it.getId().equals(itemId));
        return repository.save(cart);
    }

    public Cart checkout(Long id, CartCheckoutRequest cartCheckoutRequest, String teamName) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        cart.setTeamName(teamName);

        processCart(cart, cartCheckoutRequest);

        return cart;
    }

    private void processCart(Cart cart, CartCheckoutRequest cartCheckoutRequest) {
        List<Currency> currencies = currencyService.findCurrencies();
        




    }


}

