package br.com.batman.cart.model.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {

    @NotNull
    private String customerId;
    @NotNull
    private CartItemRequest item;
}
