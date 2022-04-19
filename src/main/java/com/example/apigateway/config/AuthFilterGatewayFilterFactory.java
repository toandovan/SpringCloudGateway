package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import static com.example.apigateway.config.HandleToken.validateToken;


@Component
public class AuthFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthFilterGatewayFilterFactory.Config> {

    public AuthFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                throw  new RuntimeException("UnAuthorization");
            }
            String authHeader= exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts=authHeader.split(" ");
            if(parts.length!=2 || !"Bearer".equals(parts[0])){
                    throw new RuntimeException("Incorrect format");
            }
            if(validateToken(parts[1])==false){
                throw new RuntimeException("UnAuthorization");
            }
            return chain.filter(exchange);
        });
    }


    public static class Config{

    }
}
