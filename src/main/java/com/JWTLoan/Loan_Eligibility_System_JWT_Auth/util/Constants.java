package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.util;

import java.math.BigDecimal;

public final class Constants {

    private Constants() {
        // Private constructor to prevent instantiation
    }

    // API Endpoints
    public static final String API_BASE = "/api";
    public static final String API_AUTH = API_BASE + "/auth";
    public static final String API_LOAN = API_BASE + "/loan";

    // Loan Decision Constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_REVIEW = "REVIEW";

    // Risk Levels
    public static final String RISK_LOW = "LOW";
    public static final String RISK_MEDIUM = "MEDIUM";
    public static final String RISK_HIGH = "HIGH";

    // Financial Thresholds
    public static final BigDecimal MIN_DISPOSABLE_INCOME = new BigDecimal("3000");
    public static final BigDecimal MAX_DTI_RATIO = new BigDecimal("60");
    public static final BigDecimal SAFE_DTI_RATIO = new BigDecimal("40");
    public static final int MIN_CREDIT_SCORE = 650;
    public static final int GOOD_CREDIT_SCORE = 700;
    public static final int EXCELLENT_CREDIT_SCORE = 720;

    // Validation Messages
    public static final String MSG_CREDIT_SCORE_LOW = "Credit score below minimum requirement";
    public static final String MSG_DTI_HIGH = "Debt-to-Income ratio exceeds maximum allowed limit";
    public static final String MSG_DISPOSABLE_INCOME_LOW = "Disposable income below minimum requirement";
    public static final String MSG_APPROVED = "Loan approved";
    public static final String MSG_REVIEW = "Application requires manual review";
}
