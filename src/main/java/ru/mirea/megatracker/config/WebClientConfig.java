package ru.mirea.megatracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private String ApiKey = "cb0b9f6468d47f38977dc89ec588774b38b9715028f2c91ef2969dfd70391cab";

    @Bean
    public WebClient getWebClientBuilder(){
        return WebClient.builder().
                baseUrl("https://min-api.cryptocompare.com/").
                build();
    }

}
