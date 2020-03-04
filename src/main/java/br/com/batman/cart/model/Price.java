package br.com.batman.cart.model;


import lombok.*;

@Data
public class Price {
    private long amount;
    private int scale;
    private String currencyCode;
}
