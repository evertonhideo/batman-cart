package br.com.batman.cart.service;

import br.com.batman.cart.client.AnalyticsClient;
import br.com.batman.cart.client.ProductClient;
import br.com.batman.cart.model.*;
import br.com.batman.cart.model.request.CartItemRequest;
import br.com.batman.cart.model.request.CartRequest;
import br.com.batman.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    @Autowired
    ProductClient productClient;

    @Autowired
    CartRepository repository;

    @Autowired
    AnalyticsClient analyticsClient;

    @Autowired
    CurrencyService currencyService;

    public Cart createCart(CartRequest cartRequest) {

        Cart cartResponse = null;

        try {
            Cart cart = Cart.builder()
                    .id(UUID.randomUUID().toString())
                    .customerId(cartRequest.getCustomerId())
                    .status(Status.PENDING)
                    .items(new ArrayList<CartItem>())
                    .total(new Price())
                    .build();

            Product product = productClient.findProductBySky(cartRequest.getItem().getSku()).get(0);

            cart.getItems().add(CartItem.builder()
                    .id(product.getId())
                    .price(product.getPrice().getAmount())
                    .quantity(cartRequest.getItem().getQuantity())
                    .currencyCode(product.getPrice().getCurrencyCode())
                    .scale(product.getPrice().getScale())
                    .imageUrl(product.getImageUrl())
                    .name(product.getName())
                    .build());

            cart.setTotal(calculateTotalPrice(cart.getItems()));

            analyticsClient.postEvent("create_cart", cart);

            cartResponse = repository.save(cart);
        } catch (Exception ex) {
            LOGGER.error("Erro ao criar carrinho!", ex);
        }

        return cartResponse;
    }

    public Cart addNewItem(String id, CartItemRequest cartItemRequest) throws Exception {

        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        Product product = productClient.findProductBySky(cartItemRequest.getSku()).get(0);

        Boolean foundedProduct = false;

        for (CartItem it : cart.getItems()) {
            if (it.getId().equals(product.getId())) {
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
                    .name(product.getName())
                    .imageUrl(product.getImageUrl())
                    .scale(product.getPrice().getScale())
                    .build());
        }

        cart.setTotal(calculateTotalPrice(cart.getItems()));

        analyticsClient.postEvent("add_product", cart);

        return repository.save(cart);
    }


    public Cart cancelCart(String id) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        cart.setStatus(Status.CANCEL);

        analyticsClient.postEvent("cancel_cart", cart);

        return repository.save(cart);
    }

    public Cart removeCartItem(String id, String itemId) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        cart.getItems().removeIf(it -> it.getId().equals(itemId));

        cart.setTotal(calculateTotalPrice(cart.getItems()));

        analyticsClient.postEvent("remove_item", cart);

        return repository.save(cart);
    }

    public Cart getCartById(String id) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));
        return cart;
    }

    public Price calculateTotalPrice(List<CartItem> cartItems){
        Price totalPrice = new Price();
        double totalAmount = 0;
        for (CartItem item : cartItems) {
            double priceInUsd = currencyService.getPrice((double) item.getPrice(), item.getCurrencyCode(), "USD");
            totalAmount += (priceInUsd * item.getQuantity());
            totalPrice.setAmount((long) totalAmount);
            totalPrice.setScale(item.getScale());
            totalPrice.setCurrencyCode("USD");
        }
        return totalPrice;
    }

}

