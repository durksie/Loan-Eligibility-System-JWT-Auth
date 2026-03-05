package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.rules;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.LoanApplicationRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response.LoanDecisionResponse;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.UserEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DecisionEngine {
    private static final BigDecimal MIN_DISPOSABLE_INCOME = new BigDecimal("3000");
    private static final BigDecimal MAX_DTI_RATIO = new BigDecimal("60");
    private static final BigDecimal SAFE_DTI_RATIO = new BigDecimal("40");
    private static final int MIN_CREDIT_SCORE = 650;
    private static final int EXCELLENT_CREDIT_SCORE = 720;

    public LoanDecisionResponse evaluate(UserEntity userEntity, LoanApplicationRequest request) {
        // Calculate financial metrics
        BigDecimal dti = calculateDTI(userEntity.getExpenses(), userEntity.getSalary());
        BigDecimal disposableIncome = userEntity.getSalary().subtract(userEntity.getExpenses());

        // Apply decision rules
        if (userEntity.getCreditScore() < MIN_CREDIT_SCORE) {
            return buildResponse(null, "REJECTED", "HIGH",
                    "Credit score below minimum requirement of " + MIN_CREDIT_SCORE,
                    dti, disposableIncome, request);
        }

        if (dti.compareTo(MAX_DTI_RATIO) > 0) {
            return buildResponse(null, "REJECTED", "HIGH",
                    "Debt-to-Income ratio exceeds maximum allowed limit of " + MAX_DTI_RATIO + "%",
                    dti, disposableIncome, request);
        }

        if (disposableIncome.compareTo(MIN_DISPOSABLE_INCOME) < 0) {
            return buildResponse(null, "REJECTED", "HIGH",
                    "Disposable income below minimum requirement of ₹" + MIN_DISPOSABLE_INCOME,
                    dti, disposableIncome, request);
        }

        if (userEntity.getCreditScore() > EXCELLENT_CREDIT_SCORE && dti.compareTo(SAFE_DTI_RATIO) < 0) {
            return buildResponse(null, "APPROVED", "LOW",
                    "Excellent credit score and healthy DTI ratio",
                    dti, disposableIncome, request);
        }

        // Calculate risk level based on metrics
        String riskLevel = calculateRiskLevel(userEntity.getCreditScore(), dti, disposableIncome);
        return buildResponse(null, "REVIEW", riskLevel,
                "Application requires manual review",
                dti, disposableIncome, request);
    }

    private BigDecimal calculateDTI(BigDecimal expenses, BigDecimal salary) {
        if (salary.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(100);
        }
        return expenses.multiply(new BigDecimal("100"))
                .divide(salary, 2, RoundingMode.HALF_UP);
    }

    private String calculateRiskLevel(int creditScore, BigDecimal dti, BigDecimal disposableIncome) {
        if (creditScore >= 700 && dti.compareTo(new BigDecimal("50")) < 0) {
            return "LOW";
        } else if (creditScore >= 650 && dti.compareTo(new BigDecimal("55")) < 0) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    private LoanDecisionResponse buildResponse(Long applicationId, String decision,
                                               String riskLevel, String reason,
                                               BigDecimal dti, BigDecimal disposableIncome,
                                               LoanApplicationRequest request) {
        return LoanDecisionResponse.builder()
                .applicationId(applicationId)
                .decision(decision)
                .riskLevel(riskLevel)
                .reason(reason)
                .dtiRatio(dti)
                .disposableIncome(disposableIncome)
                .requestedAmount(request.getAmount())
                .termMonths(request.getTermMonths())
                .applicationDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }
}
