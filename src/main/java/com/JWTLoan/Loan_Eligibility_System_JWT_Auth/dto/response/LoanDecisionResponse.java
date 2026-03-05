package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanDecisionResponse {
    private Long applicationId;
    private String decision;
    private String riskLevel;
    private String reason;
    private BigDecimal dtiRatio;
    private BigDecimal disposableIncome;
    private BigDecimal requestedAmount;
    private Integer termMonths;
    private String applicationDate;
}
