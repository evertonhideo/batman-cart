package br.com.batman.cart.client;

import br.com.batman.cart.configuration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "analyticsClient", url = "https://7qsoyqlued.execute-api.us-east-1.amazonaws.com/latest",
        configuration = FeignClientConfig.class)
public interface AnalyticsClient {

    @RequestMapping(value = "/analytics", method = RequestMethod.POST)
    @Async
    void postEvent(@RequestParam("action") String action, @RequestBody Object payload);
}
