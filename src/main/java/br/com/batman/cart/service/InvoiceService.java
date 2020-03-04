package br.com.batman.cart.service;

import br.com.batman.cart.model.Currency;
import br.com.batman.cart.model.Invoice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "invoiceService", url = "http://zup-hackathon-financial.zup.com.br")
public interface InvoiceService {

    @PostMapping(path = "/invoices", consumes = "application/json")
    @ResponseBody
    void createInvoice(@RequestBody Invoice invoice);
}
