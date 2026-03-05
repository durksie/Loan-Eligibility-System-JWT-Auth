package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.service;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.LoanApplicationRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response.LoanDecisionResponse;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.exception.ResourceNotFoundException;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.exception.UnauthorizedException;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.LoanApplication;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.UserEntity;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.repository.LoanApplicationRepository;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.repository.UserRepository;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.rules.DecisionEngine;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanApplicationRepository loanRepository;
    private final UserRepository userRepository;
    private final DecisionEngine decisionEngine;

    @Transactional
    public LoanDecisionResponse applyForLoan(String username, LoanApplicationRequest request) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Get decision from engine
        LoanDecisionResponse decision = decisionEngine.evaluate(userEntity, request);

        // Create loan application
        LoanApplication application = new LoanApplication();
        application.setUserEntity(userEntity);
        application.setAmount(request.getAmount());
        application.setTermMonths(request.getTermMonths());
        application.setStatus(decision.getDecision());
        application.setRiskLevel(decision.getRiskLevel());
        application.setDecisionReason(decision.getReason());
        application.setDtiRatio(decision.getDtiRatio());
        application.setDisposableIncome(decision.getDisposableIncome());

        LoanApplication saved = loanRepository.save(application);

        // Update decision with application ID
        return LoanDecisionResponse.builder()
                .applicationId(saved.getId())
                .decision(saved.getStatus())
                .riskLevel(saved.getRiskLevel())
                .reason(saved.getDecisionReason())
                .dtiRatio(saved.getDtiRatio())
                .disposableIncome(saved.getDisposableIncome())
                .requestedAmount(saved.getAmount())
                .termMonths(saved.getTermMonths())
                .applicationDate(saved.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }

    @Transactional(readOnly = true)
    public LoanDecisionResponse getLoanApplication(String username, Long applicationId) {
        LoanApplication application = loanRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found"));

        if (!application.getUserEntity().getUsername().equals(username)) {
            throw new UnauthorizedException("You don't have permission to view this application");
        }

        return mapToResponse(application);
    }

    @Transactional
    public LoanDecisionResponse updateLoanApplication(String username, Long id, LoanApplicationRequest request) {
        LoanApplication application = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found"));

        if (!application.getUserEntity().getUsername().equals(username)) {
            throw new UnauthorizedException("You don't have permission to update this application");
        }

        // Re-evaluate with updated information
        UserEntity userEntity = application.getUserEntity();
        LoanDecisionResponse decision = decisionEngine.evaluate(userEntity, request);

        application.setAmount(request.getAmount());
        application.setTermMonths(request.getTermMonths());
        application.setStatus(decision.getDecision());
        application.setRiskLevel(decision.getRiskLevel());
        application.setDecisionReason(decision.getReason());
        application.setDtiRatio(decision.getDtiRatio());
        application.setDisposableIncome(decision.getDisposableIncome());

        LoanApplication updated = loanRepository.save(application);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteLoanApplication(String username, Long id) {
        LoanApplication application = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found"));

        if (!application.getUserEntity().getUsername().equals(username)) {
            throw new UnauthorizedException("You don't have permission to delete this application");
        }

        loanRepository.delete(application);
    }

    private LoanDecisionResponse mapToResponse(LoanApplication application) {
        return LoanDecisionResponse.builder()
                .applicationId(application.getId())
                .decision(application.getStatus())
                .riskLevel(application.getRiskLevel())
                .reason(application.getDecisionReason())
                .dtiRatio(application.getDtiRatio())
                .disposableIncome(application.getDisposableIncome())
                .requestedAmount(application.getAmount())
                .termMonths(application.getTermMonths())
                .applicationDate(application.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }
}
