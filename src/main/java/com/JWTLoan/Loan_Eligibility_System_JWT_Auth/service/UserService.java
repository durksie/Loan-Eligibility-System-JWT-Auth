package com.JWTLoan.Loan_Eligibility_System_JWT_Auth.service;

import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.mapper.EntityMapper;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.UserRegistrationRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.request.UserUpdateRequest;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.dto.response.UserResponse;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.exception.ResourceNotFoundException;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.exception.UnauthorizedException;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.model.UserEntity;
import com.JWTLoan.Loan_Eligibility_System_JWT_Auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        UserEntity user = entityMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return entityMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return entityMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(String authenticatedUsername, Long userId, UserUpdateRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if the authenticated user is updating their own profile
        if (!user.getUsername().equals(authenticatedUsername)) {
            throw new UnauthorizedException("You can only update your own profile");
        }

        // Update fields if provided
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getSalary() != null) {
            user.setSalary(request.getSalary());
        }

        if (request.getExpenses() != null) {
            user.setExpenses(request.getExpenses());
        }

        if (request.getCreditScore() != null) {
            user.setCreditScore(request.getCreditScore());
        }

        UserEntity updatedUser = userRepository.save(user);
        return entityMapper.toResponse(updatedUser);
    }
}
