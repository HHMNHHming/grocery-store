package com.gwm.fifter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//使用GatewayFilterFactory接口的时候，好像是实现getConfigClass()方法，现在我不会实现这个接口

//所以我这里使用继承(extends)抽象类(abstractGateFilterFactory),这个抽象类不用实现那个getConfigClass()方法
@Component
public class MyCustomerGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return new MyCustomerGatewayFilter();
    }
}
