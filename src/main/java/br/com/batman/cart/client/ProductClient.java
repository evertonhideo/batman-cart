package br.com.batman.cart.client;

import br.com.batman.cart.model.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@FeignClient(name = "productClient", url = "https://kpdzblu5u5.execute-api.us-east-1.amazonaws.com/v1")
@FeignClient(name = "productClient", url = "${hackathon.product.url}")
public interface ProductClient {

    @GetMapping(path = "/products", consumes = "application/json")
    @ResponseBody
    @Cacheable("products")
    List<Product> findProductBySky(@RequestParam("sku") String sku);
}
