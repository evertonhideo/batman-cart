package br.com.batman.cart.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimelinePayload {
    private String cartId;
    private Price price;
}
