package br.com.batman.cart.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimelineHeader {
    @SerializedName("x-team-control")
    private String teamControl;
}
