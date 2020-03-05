package br.com.batman.cart.configuration;

import feign.RetryableException;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public Retryer retryer() {
        return new Custom();
    }

}

class Custom implements Retryer {

    @Value("${app.client.maxAttempts}")
    private final int maxAttempts;
    @Value("${app.client.backoff}")
    private final long backoff;
    int attempt;

    public Custom() {
        this(2000, 2);
    }

    public Custom(long backoff, int maxAttempts) {
        this.backoff = backoff;
        this.maxAttempts = maxAttempts;
        this.attempt = 1;
    }

    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            throw e;
        }

        try {
            Thread.sleep(backoff);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Retryer clone() {
        return new Custom(backoff, maxAttempts);
    }
}