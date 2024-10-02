package com.colak.springtutorial.config;

import org.springframework.http.HttpHeaders;
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

        // Set the converter that will convert authorization header to UsernamePasswordAuthenticationToken
        setServerAuthenticationConverter(this::extractAuthentication);
    }

    // This is a ServerAuthenticationConverter
    private Mono<Authentication> extractAuthentication(ServerWebExchange exchange) {
        String token = extractToken(exchange);
        if (token == null || token.isEmpty()) {
            return Mono.empty();
        }

        // Put token as credentials
        return Mono.just(new UsernamePasswordAuthenticationToken(null, token));
    }

    private String extractToken(ServerWebExchange exchange) {
        List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);
        if (authHeaders.isEmpty()) {
            return null;
        }
        // This is same as substring(7)
        return authHeaders.getFirst().replace("Bearer ", "");
    }
}
