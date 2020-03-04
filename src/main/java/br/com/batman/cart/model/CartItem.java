package br.com.batman.cart.model;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @NotNull
    private String id;
    @NotNull
    private long price;
    @NotNull
    private int scale;
    @NotNull
    private String currencyCode;
    @NotNull
    private long quantity;
}
