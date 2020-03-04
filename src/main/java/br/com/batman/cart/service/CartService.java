package br.com.batman.cart.service;

import br.com.batman.cart.model.*;
import br.com.batman.cart.model.request.CartCheckoutRequest;
import br.com.batman.cart.model.request.CartItemRequest;
import br.com.batman.cart.model.request.CartRequest;
import br.com.batman.cart.repository.CartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    ProductService productService;

    @Autowired
    CartRepository repository;

    @Autowired
    CurrencyService currencyService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    TimelineProducer producer;

    public Cart createCart(CartRequest cartRequest) {

        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .customerId(cartRequest.getCustomerId())
                .status(Status.PENDING)
                .items(new ArrayList<CartItem>())
                .build();

        Product product = productService.findProductBySky(cartRequest.getItem().getSku()).get(0);

        cart.getItems().add(CartItem.builder()
                .id(product.getId())
                .price(product.getPrice().getAmount())
                .quantity(cartRequest.getItem().getQuantity())
                .currencyCode(product.getPrice().getCurrencyCode())
                .scale(product.getPrice().getScale())
                .imageUrl(product.getImageUrl())
                .name(product.getName())
                .build());

        return repository.save(cart);
    }

    public Cart addNewItem(String id, CartItemRequest cartItemRequest) throws Exception {

        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        Product product = productService.findProductBySky(cartItemRequest.getSku()).get(0);

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

        return repository.save(cart);
    }


    public Cart cancelCart(String id) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        cart.setStatus(Status.CANCEL);
        return repository.save(cart);
    }

    public Cart removeCartItem(String id, String itemId) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        cart.getItems().removeIf(it -> it.getId().equals(itemId));
        return repository.save(cart);
    }

    public Cart checkout(String id, CartCheckoutRequest cartCheckoutRequest, String teamName) throws Exception {
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
        long totalAmount = 0;


        Invoice invoice = Invoice.builder()
                .customerId(cart.getCustomerId())
                .status(Status.DONE)
                .id(cart.getId())
                .items(new ArrayList<InvoiceItem>())
                .build();

        for (CartItem it : cart.getItems()) {
            String currencyCode = it.getCurrencyCode() + "_TO_" + cartCheckoutRequest.getCurrencyCode();
            Currency currency = currencies.stream().filter(i -> i.getCurrencyCode().equals(currencyCode)).findFirst().get();
            long finalPrice = currency.getCurrencyValue() * it.getPrice() / 100;
            totalAmount += (finalPrice * it.getQuantity());

            invoice.getItems().add(
                    InvoiceItem.builder()
                            .currencyCode(cartCheckoutRequest.getCurrencyCode())
                            .imageUrl(it.getImageUrl())
                            .name(it.getName())
                            .scale(it.getScale())
                            .id(it.getId())
                            .price(finalPrice)
                            .build());
        }

        invoice.setTotal(Price.builder()
                .amount(totalAmount)
                .currencyCode(cartCheckoutRequest.getCurrencyCode())
                .scale(2)
                .build());

        Timeline timeline = Timeline.builder()
                .headers(TimelineHeader.builder().teamControl(cart.getTeamName()).build())
                .payload(TimelinePayload.builder()
                        .cartId(cart.getId())
                        .price(Price.builder().amount(totalAmount)
                                .currencyCode(cartCheckoutRequest.getCurrencyCode())
                                .scale(2)
                                .build())
                        .build())
                .build();

        producer.sendMessage(new Gson().toJson(timeline));

        invoiceService.createInvoice(cart.getTeamName(), invoice);
    }
}

