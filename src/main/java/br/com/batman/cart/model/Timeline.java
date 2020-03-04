package br.com.batman.cart.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Timeline {
    private TimelineHeader headers;
    private TimelinePayload payload;

}
