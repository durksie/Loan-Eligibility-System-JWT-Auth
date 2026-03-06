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
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.rules.LoanDecision;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanApplicationRepository loanRepository;
    private final UserRepository userRepository;
    private final DecisionEngine decisionEngine;

    @Transactional
    public LoanDecisionResponse applyForLoan(String username, LoanApplicationRequest request) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Get decision from engine
        LoanDecision decision = decisionEngine.evaluate(user, request);

        // Create loan application
        LoanApplication application = new LoanApplication();
        application.setUserEntity(user);
        application.setAmount(request.getAmount());
        application.setTermMonths(request.getTermMonths());
        application.setStatus(decision.getStatus());
        application.setRiskLevel(decision.getRiskLevel() != null ?
                decision.getRiskLevel().name() : null);
        application.setDecisionReason(decision.getReason());
        application.setDtiRatio(decision.getDtiRatio());
        application.setDisposableIncome(decision.getDisposableIncome());

        LoanApplication saved = loanRepository.save(application);

        return mapToResponse(saved);
    }

    @Transactional
    public LoanDecisionResponse getLoanApplication(String username, Long applicationId) {
        LoanApplication application = loanRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found"));

        if (!application.getUserEntity().getUsername().equals(username)) {
            throw new UnauthorizedException("You don't have permission to view this application");
        }

        return mapToResponse(application);
    }

    @Transactional
    public List<LoanDecisionResponse> getUserLoanApplications(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return loanRepository.findByUserEntity(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LoanDecisionResponse updateLoanApplication(String username, Long id, LoanApplicationRequest request) {
        LoanApplication application = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found"));

        if (!application.getUserEntity().getUsername().equals(username)) {
            throw new UnauthorizedException("You don't have permission to update this application");
        }

        // Only allow updates if application is still pending
        if (!"PENDING".equals(application.getStatus()) && !"REVIEW".equals(application.getStatus())) {
            throw new IllegalStateException("Cannot update application that is already " + application.getStatus());
        }

        // Re-evaluate with updated information
        UserEntity user = application.getUserEntity();
        LoanDecision decision = decisionEngine.evaluate(user, request);

        application.setAmount(request.getAmount());
        application.setTermMonths(request.getTermMonths());
        application.setStatus(decision.getStatus());
        application.setRiskLevel(decision.getRiskLevel() != null ?
                decision.getRiskLevel().name() : null);
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
                .status(application.getStatus())
                .build();
    }

}
