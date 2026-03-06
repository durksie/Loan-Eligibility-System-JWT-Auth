package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.rules;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanDecision {
    private String status;
    private RiskLevel riskLevel;
    private String reason;
    private BigDecimal dtiRatio;
    private BigDecimal disposableIncome;
    private boolean approved;
}
