# Account Service - GitHub Project ReadMe
## Overview
Account Service is a secure and robust Spring Boot application that utilizes Spring Security to manage user accounts, authentication, and role-based access control. With a range of endpoints catering to different user roles, Account Service ensures sensitive operations like payment processing, user management, and security event logging are securely handled.

## Features
- Secure user registration and authentication system.
- Role-based access control for managing user permissions.
- Secure password policies, including checks against breached passwords.
- Payment processing for accountants to manage employee salaries.
- Administrative functions for managing user roles and accounts.
- Audit logging to track security-relevant events.
- API Endpoints and Access Control
- The service defines several RESTful endpoints, each requiring specific roles for access:

- POST /api/auth/signup: Open for all users for self-registration.
- POST /api/auth/changepass: For authenticated users to change their password.
- GET /api/empl/payment: For users and accountants to view payment info.
- POST /api/acct/payments: For accountants to insert salary information.
- PUT /api/acct/payments: For accountants to update salary information.
- GET /api/admin/user/: For administrators to list all users.
- PUT /api/admin/user/role: For administrators to update user roles.
- DELETE /api/admin/user/{email}: For administrators to delete non-admin users.
- GET /api/security/events: For auditors to view security events.

## User Registration
To register a new user:
POST /api/auth/signup
{
   "name": "John",
   "lastname": "Doe",
   "email": "johndoe@acme.com",
   "password": "secret"
}
Requirements:

- Email is the username.
- Passwords must not be in breached password lists.
- Passwords must be at least 12 characters long.

Response:
{
    "id": 1,
    "name": "John",
    "lastname": "Doe",
    "email": "johndoe@acme.com",
    "roles": ["ROLE_ADMINISTRATOR"]
}

## Password Change
For changing user password after login:
POST /api/auth/changepass
{
    "new_password": "newSecurePassword2024!!!"
}
Response:
{
    "email": "useremail@acme.com",
    "status": "The password has been updated successfully"
}

## Salary Management
For accountants to add salary information:
POST /api/acct/payments
[
    {
        "employee": "useremail@acme.com",
        "period": "mm-YYYY",
        "salary": 123456
    }
    // More entries...
]

Response:
{
   "status": "Added successfully!"
}

To update salary information:
PUT /api/acct/payments
{
    "employee": "useremail@acme.com",
    "period": "01-2021",
    "salary": 123457
}

Response:
{
   "status": "Updated successfully!"
}

## Viewing Payment Information

GET /api/empl/payment?period=mm-YYYY

Response with specified period:
{
   "name": "John",
   "lastname": "Doe",
   "period": "January-2021",
   "salary": "1234 dollar(s) 56 cent(s)"
}

Or, without specifying a period to get all payments for the employee:
[
    // All payment entries of logged in user...
]


## User Role Management
For administrators to update user roles:
PUT /api/admin/user/role
{
   "user": "useremail@acme.com",
   "role": "ACCOUNTANT",
   "operation": "GRANT"
}

Response:
{
    "id": 2,
    "name": "Ivan",
    "lastname": "Ivanov",
    "email": "ivanivanov@acme.com",
    "roles": ["ROLE_ACCOUNTANT", "ROLE_USER"]
}

## User Deletion
For administrators to delete a user:
DELETE /api/admin/user/{email}

Response:
{
    "user": "useremail@acme.com",
    "status": "Deleted successfully!"
}

## Security Events Logging
Auditors can access logged security events:
GET /api/security/events

Logged Events: 
![image](https://github.com/Nihad74/Account-Service/assets/113698778/eda34fa4-4f8c-4d45-93eb-d57c7cad7426)

Response : 
[
   // list of logged events 
]

## Locking Mechanism
If a user fails to login 5 times, the account is locked. An administrator can unlock the user:
PUT /api/admin/user/access
{
   "user": "useremail@acme.com",
   "operation": "UNLOCK"
}
