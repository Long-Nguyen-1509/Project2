# Exam management system

## Project objectives

The key goals of this project are:

1. To develop a responsive web application that handles exam management for student and teacher, including uploading, viewing exam class details, and export files.
2. To implement a secure user authentication system with role-based access control, ensuring that only authorized users can perform certain actions.
3. To explore web development process.

## Technologies used

- **Frontend**: HTML, CSS, JavaScript, tailwindcss
- **Backend**: Java, Spring Boot
- **Database**: SQL Server
- **Authentication**: JWT (JSON Web Tokens)
- **Other Tools**: REST API, Project Lombok, Apache POI for Word and Excel files input and output

## Features

- Secure user authentication: JWT-based authentication with role-based access (Admin/User).
- Exam management:
  - For admin: Import exam list from pre-formatted Excel file, automatically process data to create exam classes and save into database, view exam class details and export.
  - For student: View exam class list and export.
