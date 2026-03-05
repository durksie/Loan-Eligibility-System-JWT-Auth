package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.controller;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.config.JwtService;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.LoginRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response.AuthResponse;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(AuthResponse.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .message("Login successful")
                .build());
    }

}
