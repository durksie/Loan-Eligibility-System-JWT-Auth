package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.repository;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.LoanApplication;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByUser(UserEntity user);
    List<LoanApplication> findByStatus(String status);
}
