package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.controller;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.UserRegistrationRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.UserUpdateRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response.UserResponse;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse response = userService.registerUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> updateUser(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        String username = authentication.getName();
        UserResponse response = userService.updateUser(username, id, request);
        return ResponseEntity.ok(response);
    }
}
