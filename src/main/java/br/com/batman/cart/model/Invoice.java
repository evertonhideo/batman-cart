package br.com.batman.cart.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    private String id;
    private String customerId;
    private Status status;
    private Price total;
    private List<InvoiceItem> items;

}
