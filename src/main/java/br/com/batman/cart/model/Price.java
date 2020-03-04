package br.com.batman.cart.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {
    private long amount;
    private int scale;
    private String currencyCode;
}
