package br.com.batman.cart.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversionFactor {

    private String from;
    private String to;
    private Long factorFrom;
    private Long factorTo;

}
