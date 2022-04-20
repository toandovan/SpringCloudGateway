package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.example.apigateway.config.HandleToken.validateToken;


@Component
public class AuthFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthFilterGatewayFilterFactory.Config> {

    public AuthFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthFilterGatewayFilterFactory.Config config) {

        return ((exchange, chain) -> {
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return this.onError(exchange, "No API KEY header", HttpStatus.UNAUTHORIZED);
            }
            String authHeader= exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts=authHeader.split(" ");
            if(parts.length!=2 || !"Bearer".equals(parts[0])){
                return this.onError(exchange, "Invalid API KEY", HttpStatus.UNAUTHORIZED);
            }
            if(validateToken(parts[1])==false){
                return this.onError(exchange, "Invalid API KEY", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response= exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
    public static class Config{

    }
}
