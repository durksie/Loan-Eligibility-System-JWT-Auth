package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.rules;

public enum RiskLevel {
    LOW("Low Risk"),
    MEDIUM("Medium Risk"),
    HIGH("High Risk");

    private final String description;

    RiskLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
