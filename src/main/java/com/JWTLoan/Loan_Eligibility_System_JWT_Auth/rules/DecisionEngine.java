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
    private static final int GOOD_CREDIT_SCORE = 700;
    private static final int EXCELLENT_CREDIT_SCORE = 720;

    public LoanDecision evaluate(UserEntity userEntity, LoanApplicationRequest request) {
        // Calculate financial metrics
        BigDecimal dti = calculateDTI(userEntity.getExpenses(), userEntity.getSalary());
        BigDecimal disposableIncome = userEntity.getSalary().subtract(userEntity.getExpenses());

        // Check for automatic rejection
        if (userEntity.getCreditScore() < MIN_CREDIT_SCORE) {
            return buildRejection("Credit score below minimum requirement of " + MIN_CREDIT_SCORE,
                    dti, disposableIncome);
        }

        if (dti.compareTo(MAX_DTI_RATIO) > 0) {
            return buildRejection("Debt-to-Income ratio exceeds maximum allowed limit of " +
                    MAX_DTI_RATIO + "%", dti, disposableIncome);
        }

        if (disposableIncome.compareTo(MIN_DISPOSABLE_INCOME) < 0) {
            return buildRejection("Disposable income below minimum requirement of ₹" +
                    MIN_DISPOSABLE_INCOME, dti, disposableIncome);
        }

        // Check for automatic approval
        if (userEntity.getCreditScore() >= EXCELLENT_CREDIT_SCORE && dti.compareTo(SAFE_DTI_RATIO) <= 0) {
            return buildApproval("Excellent credit score and healthy DTI ratio",
                    RiskLevel.LOW, dti, disposableIncome);
        }

        // Determine risk level for review cases
        RiskLevel riskLevel = calculateRiskLevel(userEntity.getCreditScore(), dti, disposableIncome);
        String reason = generateReviewReason(userEntity.getCreditScore(), dti, disposableIncome);

        return LoanDecision.builder()
                .status("REVIEW")
                .riskLevel(riskLevel)
                .reason(reason)
                .dtiRatio(dti)
                .disposableIncome(disposableIncome)
                .approved(false)
                .build();
    }

    private BigDecimal calculateDTI(BigDecimal expenses, BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(100);
        }
        return expenses.multiply(new BigDecimal("100"))
                .divide(salary, 2, RoundingMode.HALF_UP);
    }

    private RiskLevel calculateRiskLevel(int creditScore, BigDecimal dti, BigDecimal disposableIncome) {
        if (creditScore >= GOOD_CREDIT_SCORE && dti.compareTo(new BigDecimal("50")) < 0) {
            return RiskLevel.LOW;
        } else if (creditScore >= MIN_CREDIT_SCORE && dti.compareTo(new BigDecimal("55")) < 0) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.HIGH;
        }
    }

    private String generateReviewReason(int creditScore, BigDecimal dti, BigDecimal disposableIncome) {
        StringBuilder reason = new StringBuilder("Application requires manual review: ");

        if (creditScore < GOOD_CREDIT_SCORE) {
            reason.append("Credit score (").append(creditScore).append(") is below excellent threshold. ");
        }

        if (dti.compareTo(SAFE_DTI_RATIO) > 0) {
            reason.append("DTI ratio (").append(dti).append("%) is above safe threshold. ");
        }

        if (disposableIncome.compareTo(new BigDecimal("5000")) < 0) {
            reason.append("Disposable income is relatively low. ");
        }

        return reason.toString().trim();
    }

    private LoanDecision buildRejection(String reason, BigDecimal dti, BigDecimal disposableIncome) {
        return LoanDecision.builder()
                .status("REJECTED")
                .riskLevel(RiskLevel.HIGH)
                .reason(reason)
                .dtiRatio(dti)
                .disposableIncome(disposableIncome)
                .approved(false)
                .build();
    }

    private LoanDecision buildApproval(String reason, RiskLevel riskLevel,
                                       BigDecimal dti, BigDecimal disposableIncome) {
        return LoanDecision.builder()
                .status("APPROVED")
                .riskLevel(riskLevel)
                .reason(reason)
                .dtiRatio(dti)
                .disposableIncome(disposableIncome)
                .approved(true)
                .build();
    }
}
