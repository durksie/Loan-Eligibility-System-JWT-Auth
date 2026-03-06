package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.repository;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.LoanApplication;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByUser(UserEntity userEntity);
    List<LoanApplication> findByStatus(String status);

    @Query("SELECT l FROM LoanApplication l WHERE l.user.id = :userId ORDER BY l.createdAt DESC")
    List<LoanApplication> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT COUNT(l) FROM LoanApplication l WHERE l.user.id = :userId AND l.status = 'APPROVED'")
    long countApprovedApplicationsByUserId(@Param("userId") Long userId);
}
