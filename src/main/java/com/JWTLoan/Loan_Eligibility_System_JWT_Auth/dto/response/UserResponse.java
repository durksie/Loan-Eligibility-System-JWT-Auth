package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private BigDecimal salary;
    private BigDecimal expenses;
    private Integer creditScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
