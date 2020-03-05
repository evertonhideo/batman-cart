package br.com.batman.cart.client;

import br.com.batman.cart.configuration.FeignClientConfig;
import br.com.batman.cart.model.Invoice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "invoiceClient", url = "http://zup-hackathon-financial.zup.com.br",
        configuration = FeignClientConfig.class)
public interface InvoiceClient {

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    void createInvoice(@RequestHeader("x-team-control") String teamControl, @RequestBody Invoice invoice);
}
