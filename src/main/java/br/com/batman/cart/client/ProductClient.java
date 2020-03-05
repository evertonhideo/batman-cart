package br.com.batman.cart.client;

import br.com.batman.cart.configuration.FeignClientConfig;
import br.com.batman.cart.model.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(name = "productClient", url = "https://7qsoyqlued.execute-api.us-east-1.amazonaws.com/latest",
        configuration = FeignClientConfig.class)
public interface ProductClient {

    @GetMapping(path = "/products", consumes = "application/json")
    @ResponseBody
    @Cacheable("products")
    List<Product> findProductBySky(@RequestParam("sku") String sku);
}
