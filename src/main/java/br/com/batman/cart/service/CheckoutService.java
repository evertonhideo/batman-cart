package br.com.batman.cart.service;

import br.com.batman.cart.client.AnalyticsClient;
import br.com.batman.cart.client.InvoiceClient;
import br.com.batman.cart.model.*;
import br.com.batman.cart.model.request.CartCheckoutRequest;
import br.com.batman.cart.producer.TimelineProducer;
import br.com.batman.cart.repository.CartRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CheckoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutService.class);

    @Autowired
    InvoiceClient invoiceClient;

    @Autowired
    TimelineProducer producer;

    @Autowired
    CartRepository repository;

    @Autowired
    AnalyticsClient analyticsClient;

    @Autowired
    CurrencyService currencyService;

    public Cart checkout(String id, CartCheckoutRequest cartCheckoutRequest, String teamName) throws Exception {
        Cart cart = repository.findById(id).orElseThrow(() -> new Exception("Cart not found!"));

        if (cart.getStatus() != Status.PENDING) {
            throw new Exception("Status is not PENDING!");
        }

        cart.setTeamName(teamName);
        processCart(cart, cartCheckoutRequest);
        cart.setStatus(Status.DONE);

        analyticsClient.postEvent("checkout", cart);

        return repository.save(cart);
    }

    private void processCart(Cart cart, CartCheckoutRequest cartCheckoutRequest) {

        double totalAmount = 0;

        Invoice invoice = Invoice.builder()
                .customerId(cart.getCustomerId())
                .status(Status.DONE)
                .id(cart.getId())
                .items(new ArrayList<InvoiceItem>())
                .build();

        for (CartItem it : cart.getItems()) {

            double priceInUsd = currencyService.getPrice((double) it.getPrice(), it.getCurrencyCode(), "USD");
            double finalPrice = currencyService.getPrice(priceInUsd, "USD", cartCheckoutRequest.getCurrencyCode());
            totalAmount += (finalPrice * it.getQuantity());

            invoice.getItems().add(
                    InvoiceItem.builder()
                            .currencyCode(cartCheckoutRequest.getCurrencyCode())
                            .imageUrl(it.getImageUrl())
                            .name(it.getName())
                            .scale(it.getScale())
                            .quantity(it.getQuantity())
                            .id(it.getId())
                            .price((long) finalPrice)
                            .build());
        }

        invoice.setTotal(Price.builder()
                .amount((long) totalAmount)
                .currencyCode(cartCheckoutRequest.getCurrencyCode())
                .scale(2)
                .build());

        Timeline timeline = Timeline.builder()
                .headers(TimelineHeader.builder().teamControl(cart.getTeamName()).build())
                .payload(TimelinePayload.builder()
                        .cartId(cart.getId())
                        .price(Price.builder().amount((long) totalAmount)
                                .currencyCode(cartCheckoutRequest.getCurrencyCode())
                                .scale(2)
                                .build())
                        .build())
                .build();

        producer.sendMessage(new Gson().toJson(timeline));
        analyticsClient.postEvent("timeline", timeline);

        invoiceClient.createInvoice(cart.getTeamName(), invoice);
        analyticsClient.postEvent("invoice", invoice);
    }
}
