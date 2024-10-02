package com.colak.springtutorial.controller;

import com.colak.springtutorial.config.CustomUserDetailsService;
import com.colak.springtutorial.config.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/auth/login")
    public Mono<String> login(@RequestBody AuthRequest authRequest) {
        return Mono.just(authRequest.getUsername())
                .flatMap(username -> {
                    try {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        // Validate password
                        if (userDetails.getPassword().equals(authRequest.getPassword())) {
                            return Mono.just(jwtUtil.generateToken(userDetails.getUsername()));
                        }
                        return Mono.error(new RuntimeException("Invalid Credentials")); // Invalid credentials
                    } catch (UsernameNotFoundException e) {
                        return Mono.error(new RuntimeException("User not found")); // Handle user not found
                    }
                });
    }

    @GetMapping("/protected")
    public Mono<String> protectedResource(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        return Mono.just("Welcome " + username + "! You have access to this protected resource.");
    }

    @Getter
    @Setter
    public static class AuthRequest {
        private String username;
        private String password;
    }
}

