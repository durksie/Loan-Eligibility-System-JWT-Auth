package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanApplicationRequest {
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "1000.0", message = "Loan amount must be at least 1000")
    @DecimalMax(value = "1000000.0", message = "Loan amount cannot exceed 1,000,000")
    @Digits(integer = 15, fraction = 2, message = "Loan amount must have at most 15 digits and 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Loan term is required")
    @Min(value = 1, message = "Loan term must be at least 1 month")
    @Max(value = 360, message = "Loan term cannot exceed 360 months (30 years)")
    private Integer termMonths;
}
