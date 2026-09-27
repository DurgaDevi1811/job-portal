# Job Portal – Backend

A Spring Boot REST API for a full-stack Job Portal Application that connects candidates with recruiters. The backend provides authentication, authorization, job management, candidate applications, recruiter management, resume handling, validation, and secure role-based access.

## Features

### Authentication & Authorization

* Candidate and recruiter registration
* Secure login using JWT authentication
* BCrypt password hashing
* Role-based authorization
* Protected REST APIs
* JWT validation through a security filter
* Automatic authentication based on the logged-in user's identity

### Candidate

* Create and update candidate profile
* Browse available jobs
* Apply for jobs
* Prevent duplicate applications
* View application history
* Upload PDF resume
* View and download own resume

### Recruiter

* Create and update recruiter profile
* Create job postings
* View own job postings
* Edit and delete own jobs
* View applicants for posted jobs
* View and download applicant resumes
* Update application status

### Job Management

* Create, read, update, and delete jobs
* Search jobs by title, location, and skills
* Pagination support
* Job ownership validation
* Recruiter-specific job listing

### Validation & Error Handling

* Request validation using Jakarta Bean Validation
* Global exception handling
* Duplicate application prevention
* Invalid application status validation
* Ownership checks for protected resources

## Tech Stack

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* JWT
* MySQL
* Maven
* REST API

## Project Structure

src/main/java/com/jobportal/
│
├── controller/
│   ├── ApplicationController.java
│   ├── AuthController.java
│   ├── CandidateController.java
│   ├── JobController.java
│   └── RecruiterController.java
│
├── dto/
│   ├── ApplicationRequest.java
│   ├── CandidateRequest.java
│   ├── JobRequest.java
│   ├── JobUpdateRequest.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── RecruiterRequest.java
│   └── RegisterRequest.java
│
├── entity/
│   ├── Application.java
│   ├── Candidate.java
│   ├── Job.java
│   ├── Recruiter.java
│   └── User.java
│
├── exception/
│   └── GlobalExceptionHandler.java
│
├── repository/
│   ├── ApplicationRepository.java
│   ├── CandidateRepository.java
│   ├── JobRepository.java
│   ├── RecruiterRepository.java
│   └── UserRepository.java
│
├── security/
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtService.java
│   └── SecurityConfig.java
│
└── service/
    ├── ApplicationService.java
    ├── CandidateService.java
    ├── JobService.java
    ├── RecruiterService.java
    └── UserService.java

## Database Design

The application uses MySQL with the following main entities:

User
 ├── Candidate
 └── Recruiter

Recruiter
 └── Job
      └── Application
           └── Candidate

### Relationships

* One User can have one Candidate profile.
* One User can have one Recruiter profile.
* One Recruiter can create multiple Jobs.
* One Candidate can submit multiple Applications.
* One Job can receive multiple Applications.
* A unique constraint prevents the same candidate from applying to the same job more than once.

## Security Architecture

Client
   ↓
JWT Authentication
   ↓
JwtAuthenticationFilter
   ↓
Spring Security
   ↓
Role-Based Authorization
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
MySQL


### JWT Authentication

After successful login, the backend generates a JWT containing:

* User email
* User role
* User name
* Issued time
* Expiration time

The frontend sends the token in the request header:

Authorization: Bearer <JWT_TOKEN>

The `JwtAuthenticationFilter` validates the token and establishes the authenticated user in Spring Security.

## API Overview

### Authentication

POST /api/auth/register
POST /api/auth/login

### Jobs

GET    /api/jobs
GET    /api/jobs/page
GET    /api/jobs/search
GET    /api/jobs/{id}

POST   /api/jobs
PUT    /api/jobs/{id}
DELETE /api/jobs/{id}

GET    /api/jobs/recruiter

### Candidate

POST /api/candidates
GET  /api/candidates/{id}
PUT  /api/candidates/{id}

GET  /api/candidates/{id}/resume
POST /api/candidates/{id}/resume
GET  /api/candidates/{id}/resume/download

### Applications

POST /api/applications
GET  /api/applications/my
GET  /api/applications/job/{jobId}
PUT  /api/applications/{id}/status

### Recruiter

POST /api/recruiters
GET  /api/recruiters/{id}
PUT  /api/recruiters/{id}

## Validation

The backend validates incoming requests using Jakarta Bean Validation.

Examples include:

* Required fields
* Valid email format
* Minimum password length
* Valid user roles
* Non-negative salary
* Required job information
* Valid application statuses

Validation errors are handled centrally through `GlobalExceptionHandler`.

## Resume Upload

Candidates can upload PDF resumes.

The backend:

1. Authenticates the candidate.
2. Verifies profile ownership.
3. Validates the uploaded file.
4. Restricts the file type to PDF.
5. Applies a file-size limit.
6. Generates a unique filename.
7. Stores the file in the local uploads directory.
8. Saves the filename against the candidate profile.

Recruiters can access resumes only for applications belonging to their own job postings.

## Running the Application

### Prerequisites

* Java 21
* Maven
* MySQL
* Git

### Database

Create a MySQL database:

CREATE DATABASE job_portal;

Configure the database connection and JWT secret in your local `application.properties`.

**Do not commit `application.properties` or credentials to GitHub.**

### Run the Backend

Using Maven:

```bash
mvn spring-boot:run
```
Or using the Maven wrapper:

```bash
mvnw spring-boot:run
```

The backend runs on:
http://localhost:8080

## Frontend

The React frontend for this project is available separately:

https://github.com/DurgaDevi1811/job-portal-frontend

## Author

**DurgaDevi Vanguri**

GitHub: https://github.com/DurgaDevi1811
