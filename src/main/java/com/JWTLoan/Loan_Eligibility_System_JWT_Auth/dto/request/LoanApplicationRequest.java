package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanApplicationRequest {
    @NotNull(message = "Loan amount is required")
    @Positive(message = "Loan amount must be positive")
    @Max(value = 1000000, message = "Loan amount cannot exceed 1,000,000")
    private BigDecimal amount;

    @NotNull(message = "Loan term is required")
    @Min(value = 1, message = "Loan term must be at least 1 month")
    @Max(value = 360, message = "Loan term cannot exceed 360 months")
    private Integer termMonths;
}
