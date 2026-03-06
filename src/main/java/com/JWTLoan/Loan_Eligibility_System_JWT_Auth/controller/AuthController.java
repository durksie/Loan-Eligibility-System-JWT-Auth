package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.controller;


import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.config.JwtService;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.LoginRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response.AuthResponse;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.service.JwtUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;

    private final JwtService jwtService;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(AuthResponse.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .message("Login successful")
                .expiresIn(jwtService.extractExpiration(jwt).getTime())
                .build());
    }


}
