package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.example.apigateway.config.HandleToken.getRole;

@Component
public class RoleFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleFilterGatewayFilterFactory.Config> {

    public RoleFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String authHeader= exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String role= getRole(authHeader);
            if(!(config.getRole()).equals(role)){
                return this.onError(exchange, "You don't have permit", HttpStatus.UNAUTHORIZED);
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
        private String role;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}