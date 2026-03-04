# Loan Eligibility System JWT Auth – Production-Ready Backend API

## 📝 Project Overview
The Loan Eligibility System is a Spring Boot backend API that automates credit assessments by evaluating financial metrics like salary, expenses, 
credit scores, and debt-to-income ratios. To ensure data integrity, the application implements JWT authentication, requiring users to register and authenticate before accessing
protected endpoints or submitting applications. Security is further bolstered by role-based access control, which restricts sensitive operations, 
such as loan approval reviews or application deletions, to authorized personnel only. Based on these secure inputs, the system instantly generates an approval status, 
assigns a risk level, and provides a transparent justification for the final verdict. 
This architecture provides a secure, data-driven environment that streamlines the lending process while ensuring financial decisions are consistent, efficient, and protected.

This project demonstrates a deep understanding of **Secure Software Development Lifecycles (SDLC)**, financial modeling, and scalable RESTful architecture.

---

## 🔐 Enterprise-Grade Security
Built with a "Security-First" mindset, the platform implements a stateless authentication architecture to ensure data integrity and user privacy.

### **Security Features:**
* **Stateless Authentication**: Leverages **JWT (JSON Web Tokens)** for secure, scalable user sessions.
* **Identity Management**: Secure user registration and login flows with **BCrypt password hashing**.
* **Role-Based Access Control (RBAC)**: Distinct permissions for `USER` and `ADMIN` roles to protect sensitive financial operations.
* **Request Sanitization**: Robust input validation using **Bean Validation (JSR 380)** and global exception handling to prevent injection and malformed data.

---

## 🧠 Core Business Logic & Decision Engine
The engine acts as a "Robotic Loan Officer," applying structured financial rules to determine creditworthiness:

### **Key Evaluation Metrics:**
* ✅ **Monthly Salary & Expenses**: Analyzes cash flow liquidity.
* ✅ **Debt-to-Income (DTI) Ratio**: Calculates the percentage of gross monthly income that goes toward paying debts.
* ✅ **Credit Score Analysis**: Benchmarks historical reliability (300-850 scale).

### **Automated Outputs:**
* 📌 **Approval Status**: (Approved / Rejected / Manual Review)
* 📊 **Risk Level**: (Low / Medium / High / Very High)
* 📝 **Decision Traceability**: Detailed reasons explaining the logic behind every outcome.

---

## 🛠️ Technical Stack
* **Language**: Java 17+
* **Framework**: Spring Boot 3.x (Spring Security, Spring Data JPA)
* **Authentication**: JJWT (JSON Web Token)
* **Database**: PostgreSQL
* **Architecture**: Layered Clean Architecture (Controller, Service, Repository)
* **DevOps/Tools**: Maven, Hibernate, Postman, Git

---

## 🚀 Key Features
* **Secure Onboarding**: Complete user registration and profile management.
* **Auth Gateway**: Secure login with JWT generation and token validation filters.
* **Eligibility Engine**: Highly decoupled service layer for financial rule evaluation.
* **Resource Protection**: Ownership-based application management (Users can only access/delete their own data).
* **Production Readiness**: Global error handling providing consistent, professional API responses.

---

## 💡 Project Purpose & Learning Outcomes
This system was developed to showcase proficiency in:
1.  **Enterprise Backend Design**: Implementing a clean, maintainable layered architecture.
2.  **Security Implementation**: Integrating Spring Security with modern token-based protocols.
3.  **Domain Modeling**: Translating complex financial requirements into functional code.
4.  **API Standards**: Adhering to RESTful best practices and documentation standards.

---
## 🏗️ Security Architecture

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
# 🔑 JWT Explained
## A JSON Web Token acts as a digital "Theme Park Wristband":

* Header: Defines the algorithm (e.g., HS256).

* Payload: Contains "Claims" (User ID, Username, Roles).

* Signature: Prevents tampering by signing the token with a secret key.

* Note: Never share your jwt.secret key. If compromised, attackers can forge admin tokens.

# 🔌 API Endpoints
## POST /api/loan/apply | Allowed Roles: USER, ADMIN

## JSON
// Header: Authorization: Bearer <token>
// Request
```
{
  "amount": 5000,
  "termMonths": 12,
  "purpose": "debt_consolidation"
}
```

## // Response (202 Accepted)
```
{
  "applicationId": "loan-67890",
  "status": "PENDING_REVIEW"
}
```
## GET /api/loan/my-applications | Allowed Roles: USER
// Header: Authorization: Bearer <token>
// Response (200 OK)
```
  {
    "applicationId": "loan-67890",
    "amount": 5000,
    "status": "APPROVED",
    "createdAt": "2026-02-01"
  }
```
## GET /api/admin/all | Allowed Roles: ADMIN
// Header: Authorization: Bearer <admin_token>
// Response (200 OK)
```
{
  "totalApplications": 154,
  "pendingCount": 12,
  "applications": [ 
    { "id": "loan-67890", "user": "jdoe_99", "status": "PENDING" } 
  ]
}
```


## 👨‍💻 Author
**Ntlemo Durksie** *Junior Software Engineer | Aspiring AI Engineer* *Specializing in Java, Spring Boot, and Secure API Development.*

---

