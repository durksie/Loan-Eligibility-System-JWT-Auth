# Loan Eligibility System JWT Auth – Production-Ready Backend API

##  Project Overview
The Loan Eligibility System is a Spring Boot backend API that automates credit assessments by evaluating financial metrics like salary, expenses, 
credit scores, and debt-to-income ratios. To ensure data integrity, the application implements JWT authentication, requiring users to register and authenticate before accessing
protected endpoints or submitting applications. Security is further bolstered by role-based access control, which restricts sensitive operations, 
such as loan approval reviews or application deletions, to authorized personnel only. Based on these secure inputs, the system instantly generates an approval status, 
assigns a risk level, and provides a transparent justification for the final verdict. 
This architecture provides a secure, data-driven environment that streamlines the lending process while ensuring financial decisions are consistent, efficient, and protected.

This project demonstrates a deep understanding of **Secure Software Development Lifecycles (SDLC)**, financial modeling, and scalable RESTful architecture.

---

##  Enterprise-Grade Security
Built with a "Security-First" mindset, the platform implements a stateless authentication architecture to ensure data integrity and user privacy.

### **Security Features:**
* **Stateless Authentication**: Leverages **JWT (JSON Web Tokens)** for secure, scalable user sessions.
* **Identity Management**: Secure user registration and login flows with **BCrypt password hashing**.
* **Role-Based Access Control (RBAC)**: Distinct permissions for `USER` and `ADMIN` roles to protect sensitive financial operations.
* **Request Sanitization**: Robust input validation using **Bean Validation (JSR 380)** and global exception handling to prevent injection and malformed data.

---

##  Core Business Logic & Decision Engine
The engine acts as a "Robotic Loan Officer," applying structured financial rules to determine creditworthiness:

### **Key Evaluation Metrics:**
*  **Monthly Salary & Expenses**: Analyzes cash flow liquidity.
*  **Debt-to-Income (DTI) Ratio**: Calculates the percentage of gross monthly income that goes toward paying debts.
*  **Credit Score Analysis**: Benchmarks historical reliability (300-850 scale).
  
### **Decision Rules Summary**
1. Automatic Rejection
* Credit score < 650

* DTI ratio > 60%

* Disposable income < ₹3000

2. Automatic Approval
* Credit score > 720 AND DTI ratio < 40%

3. Manual Review
All other cases with risk assessment:

* LOW Risk: Credit ≥700, DTI <50%

*  MEDIUM Risk: Credit ≥650, DTI <55%

* HIGH Risk: All other cases

### **Automated Outputs:**
*  **Approval Status**: (Approved / Rejected / Manual Review)
*  **Risk Level**: (Low / Medium / High / Very High)
*  **Decision Traceability**: Detailed reasons explaining the logic behind every outcome.

---

##  Technical Stack
* **Language**: Java 17+
* **Framework**: Spring Boot 3.x (Spring Security, Spring Data JPA)
* **Authentication**: JJWT (JSON Web Token)
* **Database**: PostgreSQL
* **Architecture**: Layered Clean Architecture (Controller, Service, Repository)
* **DevOps/Tools**: Maven, Hibernate, Postman, Git

---

##  Key Features
* **Secure Onboarding**: Complete user registration and profile management.
* **Auth Gateway**: Secure login with JWT generation and token validation filters.
* **Eligibility Engine**: Highly decoupled service layer for financial rule evaluation.
* **Resource Protection**: Ownership-based application management (Users can only access/delete their own data).
* **Production Readiness**: Global error handling providing consistent, professional API responses.

---

##  Project Purpose & Learning Outcomes
This system was developed to showcase proficiency in:
1.  **Enterprise Backend Design**: Implementing a clean, maintainable layered architecture.
2.  **Security Implementation**: Integrating Spring Security with modern token-based protocols.
3.  **Domain Modeling**: Translating complex financial requirements into functional code.
4.  **API Standards**: Adhering to RESTful best practices and documentation standards.

---
##  Security Architecture

### ### Authentication Mechanism
```text
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│                 │     │                  │     │                 │
│   Client App    │────▶│   JWT Filter     │────▶│   Controller    │
│   (Postman)     │     │ (Auth Check)     │     │ (Business Logic)│
│                 │◀────│                  │◀────│                 │
└─────────────────┘     └──────────────────┘     └─────────────────┘
         │                       │                        │
         │                       ▼                        │
         │              ┌──────────────────┐              │
         └─────────────▶│   JWT Utils      │◀─────────────┘
                        │  • Sign Token    │
                        │  • Extract Claims│
                        │  • Validate Expiry│
                        └──────────────────┘
```
----
#  JWT Explained
## A JSON Web Token acts as a digital "Theme Park Wristband":

* Header: Defines the algorithm (e.g., HS256).

* Payload: Contains "Claims" (User ID, Username, Roles).

* Signature: Prevents tampering by signing the token with a secret key.

* Note: Never share your jwt.secret key. If compromised, attackers can forge admin tokens.
---
# Loan Eligibility System API Documentation
---
## Base URL: http://localhost:8080
---
## Authentication 
The API uses JWT (JSON Web Token) for authentication. Most endpoints require a valid JWT token in the Authorization header.
```
Headers for Protected Endpoints

Authorization: Bearer <your_jwt_token>
Content-Type: application/json
```

##  Table of Contents

| Section | API / Feature |
|---------|---------------|
| **Public APIs (No Auth Required)** | Register User |
|  | Login |
| **User Management APIs** | Get User Profile |
|  | Update User |
| **Loan Application APIs** | Apply for Loan |
|  | Get Loan Application |
|  | Get All User Loans |
|  | Update Loan Application |
|  | Delete Loan Application |


# Public APIs (No Auth Required)
### 1. Register User
Creates a new user account in the system.
```
Endpoint: POST /api/loan/user
```
Request Body:
```
json
{
    "username": "john_doe",
    "password": "Test@123",
    "email": "john.doe@email.com",
    "fullName": "John Doe",
    "salary": 60000,
    "expenses": 25000,
    "creditScore": 750
}
```
## Validation Rules

| Field       | Rules                                             |
|------------|--------------------------------------------------|
| `username`  | 3-50 chars, alphanumeric + `._-`                |
| `password`  | Min 6 chars, 1 digit, 1 lowercase, 1 uppercase, 1 special char |
| `email`     | Valid email format                               |
| `fullName`  | Max 100 chars                                    |
| `salary`    | Positive number                                  |
| `expenses`  | Positive number                                  |
| `creditScore` | Between 300-850                               |

#### Success Response (201 Created):

json:
```
{
    "id": 1,
    "username": "john_doe",
    "email": "john.doe@email.com",
    "fullName": "John Doe",
    "salary": 60000,
    "expenses": 25000,
    "creditScore": 750,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```
### Error Responses:

### json:
// 400 Bad Request - Validation Error
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Invalid input parameters",
    "details": [
        "Password must contain at least one digit, one lowercase, one uppercase, one special character",
        "Email invalid format"
    ],
    "path": "/api/loan/user"
}
```
// 400 Bad Request - Duplicate User
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Username already exists",
    "path": "/api/loan/user"
}
```
### 2. Login
Authenticates a user and returns a JWT token.
```
Endpoint: POST /api/auth/login
```
Request Body:
```
{
    "username": "john_doe",
    "password": "Test@123"
}
```
Success Response (200 OK):
```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTYyMzQ1Njc4OSwiZXhwIjoxNjIzNTQzMTg5fQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
    "username": "john_doe",
    "message": "Login successful",
    "expiresIn": 1623543189000
}
```
Error Response (401 Unauthorized):
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid username or password",
    "path": "/api/auth/login"
}
```
# User Management APIs
### 3. Get User Profile
Retrieves the authenticated user's profile.
```
Endpoint: GET /api/loan/user/profile
```

Headers Required:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
Success Response (200 OK):
```
{
    "id": 1,
    "username": "john_doe",
    "email": "john.doe@email.com",
    "fullName": "John Doe",
    "salary": 60000,
    "expenses": 25000,
    "creditScore": 750,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```
Error Response (403 Forbidden):
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Access denied",
    "path": "/api/loan/user/profile"
}
```
### 4. Update User
Updates the authenticated user's information.
```
Endpoint: PUT /api/loan/user/{id}
```
Example: PUT /api/loan/user/1

Headers Required:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json
```
Request Body (all fields optional):
```
{
    "fullName": "John Updated Doe",
    "salary": 65000,
    "expenses": 20000,
    "creditScore": 780
}
```
Success Response (200 OK):
```
{
    "id": 1,
    "username": "john_doe",
    "email": "john.doe@email.com",
    "fullName": "John Updated Doe",
    "salary": 65000,
    "expenses": 20000,
    "creditScore": 780,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:45:00"
}
```
Error Response (403 Forbidden):
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 403,
    "error": "Forbidden",
    "message": "You can only update your own profile",
    "path": "/api/loan/user/2"
}
```
# Loan Application APIs
### 5. Apply for Loan
Submits a new loan application for evaluation.
```
Endpoint: POST /api/loan/apply
```
Headers Required:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json
```
Request Body:
```
{
    "amount": 50000,
    "termMonths": 24
}
```
 Loan Validation Rules

| Field       | Rules                   |
|------------|------------------------|
| `amount`     | Min: 1,000, Max: 1,000,000 |
| `termMonths` | Min: 1, Max: 360          |

Success Response - APPROVED (200 OK):
```
{
    "applicationId": 1,
    "decision": "APPROVED",
    "riskLevel": "LOW",
    "reason": "Excellent credit score and healthy DTI ratio",
    "dtiRatio": 30.77,
    "disposableIncome": 45000,
    "requestedAmount": 50000,
    "termMonths": 24,
    "applicationDate": "2024-01-15T11:50:00",
    "status": "APPROVED"
}
```
Success Response - REJECTED (200 OK):
```
{
    "applicationId": 2,
    "decision": "REJECTED",
    "riskLevel": "HIGH",
    "reason": "Credit score below minimum requirement of 650",
    "dtiRatio": 50.00,
    "disposableIncome": 20000,
    "requestedAmount": 50000,
    "termMonths": 24,
    "applicationDate": "2024-01-15T11:55:00",
    "status": "REJECTED"
}
```
Success Response - REVIEW (200 OK):
```
{
    "applicationId": 3,
    "decision": "REVIEW",
    "riskLevel": "MEDIUM",
    "reason": "Application requires manual review: DTI ratio (66.67%) is above safe threshold",
    "dtiRatio": 66.67,
    "disposableIncome": 15000,
    "requestedAmount": 75000,
    "termMonths": 36,
    "applicationDate": "2024-01-15T12:05:00",
    "status": "REVIEW"
}
```

# 6. Get Loan Application
Retrieves a specific loan application by ID.
```
Endpoint: GET /api/loan/{applicationId}
```

Example: GET /api/loan/1

Headers Required:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
Success Response (200 OK):
```
{
    "applicationId": 1,
    "decision": "APPROVED",
    "riskLevel": "LOW",
    "reason": "Excellent credit score and healthy DTI ratio",
    "dtiRatio": 30.77,
    "disposableIncome": 45000,
    "requestedAmount": 50000,
    "termMonths": 24,
    "applicationDate": "2024-01-15T11:50:00",
    "status": "APPROVED"
}
```
Error Response (404 Not Found):
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Loan application not found",
    "path": "/api/loan/999"
}
```
Error Response (403 Forbidden):
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 403,
    "error": "Forbidden",
    "message": "You don't have permission to view this application",
    "path": "/api/loan/2"
}
```
# 7. Get All User Loans
Retrieves all loan applications for the authenticated user.
```
Endpoint: GET /api/loan/user/all
```

Headers Required:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
Success Response (200 OK):
```
[
    {
        "applicationId": 1,
        "decision": "APPROVED",
        "riskLevel": "LOW",
        "reason": "Excellent credit score and healthy DTI ratio",
        "dtiRatio": 30.77,
        "disposableIncome": 45000,
        "requestedAmount": 50000,
        "termMonths": 24,
        "applicationDate": "2024-01-15T11:50:00",
        "status": "APPROVED"
    },
    {
        "applicationId": 3,
        "decision": "REVIEW",
        "riskLevel": "MEDIUM",
        "reason": "Application requires manual review",
        "dtiRatio": 66.67,
        "disposableIncome": 15000,
        "requestedAmount": 75000,
        "termMonths": 36,
        "applicationDate": "2024-01-15T12:05:00",
        "status": "REVIEW"
    }
]
```
# 8. Update Loan Application
Updates an existing loan application (only PENDING or REVIEW status applications can be updated).
```
Endpoint: PUT /api/loan/application/{id}
```
Example: PUT /api/loan/application/3

Headers Required:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json
```
Request Body:
```
{
    "amount": 30000,
    "termMonths": 24
}
```
Success Response (200 OK):
```
{
    "applicationId": 3,
    "decision": "APPROVED",
    "riskLevel": "LOW",
    "reason": "Excellent credit score and healthy DTI ratio",
    "dtiRatio": 66.67,
    "disposableIncome": 15000,
    "requestedAmount": 30000,
    "termMonths": 24,
    "applicationDate": "2024-01-15T12:05:00",
    "status": "APPROVED"
}
```
Error Response (400 Bad Request):
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Cannot update application that is already APPROVED",
    "path": "/api/loan/application/1"
}
```
# 9. Delete Loan Application
Deletes a loan application.
```
Endpoint: DELETE /api/loan/application/{id}
```
Example: DELETE /api/loan/application/3

Headers Required:
** Success Response: 204 No Content

Error Response (404 Not Found):
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Loan application not found",
    "path": "/api/loan/application/999"
}
```
#### !Common Error Responses
400 Bad Request - Validation Error
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Validation Failed",
    "message": "Invalid input parameters",
    "details": [
        "Field error message 1",
        "Field error message 2"
    ],
    "path": "/api/endpoint"
}
```
401 Unauthorized - Invalid/Expired Token
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid username or password",
    "path": "/api/auth/login"
}
```
403 Forbidden - Access Denied
```
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 403,
    "error": "Forbidden",
    "message": "You don't have permission to access this resource",
    "path": "/api/loan/2"
}
```


## 👨‍💻 Author
**Ntlemo Durksie** *Junior Software Engineer | Aspiring AI Engineer* *Specializing in Java, Spring Boot, and Secure API Development.*

---

