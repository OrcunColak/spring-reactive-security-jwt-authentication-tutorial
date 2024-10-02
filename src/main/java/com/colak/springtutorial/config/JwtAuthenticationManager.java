package com.colak.springtutorial.config;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationManager(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        if (token == null || token.isEmpty()) {
            return Mono.empty(); // Return empty if no token
        }

        String username = jwtUtil.extractUsername(token);
        if (username == null || jwtUtil.isTokenExpired(token)) {
            return Mono.empty(); // Return empty if token is expired or username is null
        }

        return Mono.just(username)
                .flatMap(this::loadUserDetails) // Load user details
                .filter(userDetails -> jwtUtil.validateToken(token, userDetails.getUsername())) // Validate token
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities())) // Create auth token
                .cast(Authentication.class)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Invalid token or user details"))); // Handle errors
    }

    private Mono<UserDetails> loadUserDetails(String username) {
        return Mono.fromCallable(() -> userDetailsService.loadUserByUsername(username))
                .onErrorResume(throwable -> Mono.empty()); // Handle potential UsernameNotFoundException
    }
}
