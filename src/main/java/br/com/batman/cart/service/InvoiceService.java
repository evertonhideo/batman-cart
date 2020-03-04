package br.com.batman.cart.service;

import br.com.batman.cart.model.Currency;
import br.com.batman.cart.model.Invoice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "invoiceService", url = "http://zup-hackathon-financial.zup.com.br")
public interface InvoiceService {

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    void createInvoice(@RequestHeader("x-team-control") String teamControl, @RequestBody Invoice invoice);
}
