package br.com.batman.cart.client;

import br.com.batman.cart.model.Currency;
import br.com.batman.cart.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@FeignClient(name = "currencyClient", url = "https://kpdzblu5u5.execute-api.us-east-1.amazonaws.com/v1")
@FeignClient(name = "currencyClient", url = "${hackathon.external.url}")
public interface CurrencyClient {

    @GetMapping(path = "/currencies", consumes = "application/json")
    @ResponseBody
    List<Currency> findCurrencies();
}
