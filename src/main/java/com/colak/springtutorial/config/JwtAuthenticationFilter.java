package com.colak.springtutorial.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    public JwtAuthenticationFilter(JwtAuthenticationManager jwtAuthenticationManager) {
        super(jwtAuthenticationManager);
        setServerAuthenticationConverter(this::extractAuthentication);
    }

    private Mono<Authentication> extractAuthentication(ServerWebExchange exchange) {
        String token = extractToken(exchange);
        if (token == null || token.isEmpty()) {
            return Mono.empty();
        }

        return Mono.just(new UsernamePasswordAuthenticationToken(null, token));
    }

    private String extractToken(ServerWebExchange exchange) {
        List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty("Authorization");
        if (authHeaders.isEmpty()) {
            return null;
        }
        return authHeaders.get(0).replace("Bearer ", "");
    }
}
