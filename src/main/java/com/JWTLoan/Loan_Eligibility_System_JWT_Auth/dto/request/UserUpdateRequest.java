package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserUpdateRequest {
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;

    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be positive")
    @Digits(integer = 15, fraction = 2, message = "Salary must have at most 15 digits and 2 decimal places")
    private BigDecimal salary;

    @DecimalMin(value = "0.0", inclusive = false, message = "Expenses must be positive")
    @Digits(integer = 15, fraction = 2, message = "Expenses must have at most 15 digits and 2 decimal places")
    private BigDecimal expenses;

    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score cannot exceed 850")
    private Integer creditScore;
}
