package br.com.batman.cart.model;

import lombok.Data;

@Data
public class Currency {

    private String currencyCode;
    private Long currencyValue;
    private int scale;
}
