package br.com.batman.cart.service;

import br.com.batman.cart.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "productService", url = "https://bu6pphkfl9.execute-api.us-east-1.amazonaws.com/latest")
public interface ProductService {

    @GetMapping(path = "/products", consumes = "application/json")
    @ResponseBody
    Product findProductBySky(@RequestParam("sku") String sku);
}
