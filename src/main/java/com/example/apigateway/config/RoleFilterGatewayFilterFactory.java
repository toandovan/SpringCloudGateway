package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import static com.example.apigateway.config.HandleToken.getRole;

@Component
public class RoleFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleFilterGatewayFilterFactory.Config> {

    public RoleFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            //implement
            String authHeader= exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String role= getRole(authHeader);
            if(!(config.getRole()).equals(role)){
                throw new RuntimeException("UnAuthorization");
            }
            return chain.filter(exchange);
        });
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
