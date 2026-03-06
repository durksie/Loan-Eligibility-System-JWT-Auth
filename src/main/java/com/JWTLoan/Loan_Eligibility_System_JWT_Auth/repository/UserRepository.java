package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.repository;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.loanApplications WHERE u.username = :username")
    Optional<UserEntity> findByUsernameWithApplications(@Param("username") String username);
}
