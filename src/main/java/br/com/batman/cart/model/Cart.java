package br.com.batman.cart.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "carts")
public class Cart {

    @Id
    private long id;
    @NotNull
    private  String customerId;
    @NotNull
    private Status status;
    @NotNull
    private List<CartItem> items;
    private String teamName;

}
