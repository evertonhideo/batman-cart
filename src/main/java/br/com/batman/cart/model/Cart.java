package br.com.batman.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String id;
    @NotNull
    private  String customerId;
    @NotNull
    private Status status;
    @NotNull
    private List<CartItem> items;
    @NotNull
    private Price total;
    @JsonIgnore
    private String teamName;

}
