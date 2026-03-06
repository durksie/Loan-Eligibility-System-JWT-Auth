package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
