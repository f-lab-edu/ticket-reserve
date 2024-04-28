package com.kjh.ticketreserve.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final SecurityUserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;

    public JwtAuthenticationProvider(SecurityUserDetailsService userDetailsService, JwtProvider jwtProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        Claims claims = jwtProvider.parseClaims(token);
        String subject = claims.getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        if (userDetails == null) {
            throw new UsernameNotFoundException("Username not found: " + subject);
        }

        return new JwtAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
