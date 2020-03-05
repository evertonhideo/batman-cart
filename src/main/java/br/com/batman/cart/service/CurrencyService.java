package br.com.batman.cart.service;

import br.com.batman.cart.client.CurrencyClient;
import br.com.batman.cart.model.ConversionFactor;
import br.com.batman.cart.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    CurrencyClient currencyClient;

    @PostConstruct
    private void postConstruct() {
        prepareFactors();
    }

    private List<ConversionFactor> factors = null;

    private void prepareFactors() {

        List<Currency> currencies = currencyClient.findCurrencies();
        factors = new ArrayList<ConversionFactor>();
        ConversionFactor factor = null;
        List<ConversionFactor> invertedFactors = new ArrayList<ConversionFactor>();

        for (Currency it : currencies) {
            String code = it.getCurrencyCode();
            String from = code.substring(0, code.indexOf("_TO_"));
            String to = code.replace(from + "_TO_", "");
            Long value = it.getCurrencyValue();
            factor = ConversionFactor.builder()
                    .from(from)
                    .to(to)
                    .factorFrom(100L)
                    .factorTo(value)
                    .build();
            factors.add(factor);
        }

        for (ConversionFactor fc : factors) {
            factor = ConversionFactor.builder()
                    .from(fc.getTo())
                    .to(fc.getFrom())
                    .factorFrom(fc.getFactorTo())
                    .factorTo(fc.getFactorFrom())
                    .build();
            invertedFactors.add(factor);
        }

        factors.addAll(invertedFactors);

        factor = ConversionFactor.builder()
                .from("USD")
                .to("USD")
                .factorFrom(100L)
                .factorTo(100L)
                .build();
        factors.add(factor);
    }

    public double getPrice(double price, String from, String to) {

        double returnPrice = 0;

        ConversionFactor factor = factors.stream()
                .filter(i -> i.getFrom().equals(from)
                        && i.getTo().equals(to))
                .findFirst()
                .orElse(null);

        if (factor != null) {
            returnPrice = price / ((double) factor.getFactorFrom() / (double) factor.getFactorTo());
        }

        return returnPrice;
    }
}
