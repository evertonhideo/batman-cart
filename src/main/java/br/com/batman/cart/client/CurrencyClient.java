package br.com.batman.cart.client;

import br.com.batman.cart.configuration.FeignClientConfig;
import br.com.batman.cart.model.Currency;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(name = "currencyClient", url = "http://zup-hackathon-financial.zup.com.br",
        configuration = FeignClientConfig.class)
public interface CurrencyClient {

    @GetMapping(path = "/currencies", consumes = "application/json")
    @ResponseBody
    List<Currency> findCurrencies();
}
