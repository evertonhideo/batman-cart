package br.com.batman.cart.model.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {

    @NotNull
    private String sku;
    @NotNull
    private long quantity;

}
