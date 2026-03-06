package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String message;
    private long expiresIn;
}
