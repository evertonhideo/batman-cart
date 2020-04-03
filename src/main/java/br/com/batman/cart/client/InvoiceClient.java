package br.com.batman.cart.client;

import br.com.batman.cart.model.Currency;
import br.com.batman.cart.model.Invoice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "invoiceClient", url = "https://kpdzblu5u5.execute-api.us-east-1.amazonaws.com/v1")
@FeignClient(name = "invoiceClient", url = "${hackathon.external.url}")
public interface InvoiceClient {

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    void createInvoice(@RequestHeader("x-team-control") String teamControl, @RequestBody Invoice invoice);
}
