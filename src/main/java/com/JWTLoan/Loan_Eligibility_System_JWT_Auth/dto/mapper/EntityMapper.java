package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.mapper;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.UserRegistrationRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response.UserResponse;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {
    public UserEntity toEntity(UserRegistrationRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setEmail(request.getEmail());
        userEntity.setFullName(request.getFullName());
        userEntity.setSalary(request.getSalary());
        userEntity.setExpenses(request.getExpenses());
        userEntity.setCreditScore(request.getCreditScore());
        return userEntity;
    }

    public UserResponse toResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .salary(user.getSalary())
                .expenses(user.getExpenses())
                .creditScore(user.getCreditScore())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
