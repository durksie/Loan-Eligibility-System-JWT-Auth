package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.controller;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.LoanApplicationRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response.LoanDecisionResponse;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<LoanDecisionResponse> applyForLoan(
            Authentication authentication,
            @Valid @RequestBody LoanApplicationRequest request) {
        String username = authentication.getName();
        LoanDecisionResponse response = loanService.applyForLoan(username, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDecisionResponse> getLoanApplication(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        LoanDecisionResponse response = loanService.getLoanApplication(username, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<LoanDecisionResponse>> getUserLoanApplications(
            Authentication authentication) {
        String username = authentication.getName();
        List<LoanDecisionResponse> responses = loanService.getUserLoanApplications(username);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/application/{id}")
    public ResponseEntity<LoanDecisionResponse> updateLoanApplication(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody LoanApplicationRequest request) {
        String username = authentication.getName();
        LoanDecisionResponse response = loanService.updateLoanApplication(username, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/application/{id}")
    public ResponseEntity<Void> deleteLoanApplication(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        loanService.deleteLoanApplication(username, id);
        return ResponseEntity.noContent().build();
    }
}
