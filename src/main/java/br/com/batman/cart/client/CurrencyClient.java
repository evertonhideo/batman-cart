package br.com.batman.cart.client;

import br.com.batman.cart.model.Currency;
import br.com.batman.cart.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(name = "currencyClient", url = "http://zup-hackathon-financial.zup.com.br")
public interface CurrencyClient {

    @GetMapping(path = "/currencies", consumes = "application/json")
    @ResponseBody
    List<Currency> findCurrencies();
}
