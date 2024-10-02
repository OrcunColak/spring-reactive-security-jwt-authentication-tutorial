package com.colak.springtutorial.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // In-memory user storage
    private final Map<String, UserDetails> users = new HashMap<>();

    public CustomUserDetailsService() {

        users.put("user1", User.withUsername("user1")
                .password("password1")
                .roles("USER")
                .build());
        users.put("admin", User.withUsername("admin")
                .password("admin123")
                .roles("ADMIN")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = users.get(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails;
    }
}

