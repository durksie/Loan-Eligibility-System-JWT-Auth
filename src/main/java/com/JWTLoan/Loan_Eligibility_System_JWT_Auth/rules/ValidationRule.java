package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.rules;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.UserEntity;
import org.antlr.v4.runtime.misc.Predicate;

import java.math.BigDecimal;

public enum ValidationRule {
    CREDIT_SCORE_MIN(
            userEntity -> userEntity.getCreditScore() >= 650,
            "Credit score must be at least 650"
    ),
    DTI_MAX(
            user -> {
                BigDecimal dti = user.getExpenses()
                        .multiply(new BigDecimal("100"))
                        .divide(user.getSalary(), 2, BigDecimal.ROUND_HALF_UP);
                return dti.compareTo(new BigDecimal("60")) <= 0;
            },
            "Debt-to-Income ratio must not exceed 60%"
    ),
    DISPOSABLE_INCOME_MIN(
            user -> user.getSalary().subtract(user.getExpenses())
                    .compareTo(new BigDecimal("3000")) >= 0,
            "Monthly disposable income must be at least 3000"
    ),
    SALARY_POSITIVE(
            user -> user.getSalary().compareTo(BigDecimal.ZERO) > 0,
            "Salary must be positive"
    ),
    EXPENSES_LESS_THAN_SALARY(
            user -> user.getExpenses().compareTo(user.getSalary()) < 0,
            "Expenses must be less than salary"
    );

    private final Predicate<UserEntity> validator;
    private final String message;

    ValidationRule(Predicate<UserEntity> validator, String message) {
        this.validator = validator;
        this.message = message;
    }

    public boolean validate(UserEntity user) {
        return validator.test(user);
    }

    public String getMessage() {
        return message;
    }
}
