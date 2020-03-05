package br.com.batman.cart.client;

import br.com.batman.cart.model.Invoice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "analyticsClient", url = "https://7qsoyqlued.execute-api.us-east-1.amazonaws.com/latest")
public interface AnalyticsClient {

    @RequestMapping(value = "/analytics", method = RequestMethod.POST)
    @Async
    void postEvent(@RequestParam("action") String action, @RequestBody Object payload);
}
