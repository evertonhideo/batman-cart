package br.com.batman.cart.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {

    private String id;
    private String name;
    private String imageUrl;
    private long price;
    private int scale;
    private String currencyCode;
}
