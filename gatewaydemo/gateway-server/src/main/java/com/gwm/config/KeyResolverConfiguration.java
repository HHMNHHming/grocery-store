package com.gwm.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
@Configuration
public class KeyResolverConfiguration {
    @Bean
    public KeyResolver ipKeyResolver(){
    // return exchange -> Mono.just(exchange.getRequest().getURI().getPath());//URI限流
    //Mono.just(exchange.getRequest().getQueryParams().getFirst("token")); // 参数限流
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());//IP限流
    }
}