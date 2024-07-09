# Coder-Hack
This project is a RESTful API service developed using Spring Boot to manage the leaderboard for a coding platform. MongoDB is used to persist user data. The API supports CRUD operations for user registrations, score updates, and badge management based on predefined rules.

## Postman Collection
You can access the Postman Collection for this project using the link below:
[![Run in Postman](https://run.pstmn.io/button.svg)](https://elements.getpostman.com/redirect?entityId=30015848-0d23b59b-1588-4831-bc6c-ef87e17e62af&entityType=collection)

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Validation and Error Handling](#validation-and-error-handling)
- [Contact](#contact)

## Features
- User registration with unique user ID and username.
- CRUD operations for managing users.
- Score updates and badge assignments based on score thresholds.
- User retrieval sorted by score with O(nlogn) complexity.
- Basic JUnit test cases for verifying operations.
- Error handling and validation for user inputs.

## Technologies Used
- **Backend**: Java, Spring Boot
- **Database**: MongoDB
- **Tools**: Gradle, Git, Postman
- **Testing**: JUnit

## Installation
### Prerequisites
- Java Development Kit (JDK) 11 or higher
- MongoDB
- Gradle
- Git

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/amit130391/Coder-Hack.git
   cd Coder-Hack
2. Configure MongoDB:
   Ensure MongoDB is running on your local machine or update the connection details in application.properties if using a remote MongoDB instance.
3. Build and run the application:
   ./gradlew clean build
   ./gradlew bootRun

## Usage
1. The application runs on http://localhost:8080.
2. Use the API endpoints to manage users and their scores.
3. Use Postman or any other API client to interact with the endpoints.

## API Endpoints
### User Endpoints
1. GET /users - Retrieve a list of all registered users.
2. GET /users/{userId} - Retrieve the details of a specific user.
3. POST /users - Register a new user to the contest.
       * Request Body: { "userId": "string", "username": "string" }
4. PUT /users/{userId} - Update the score of a specific user.
       * Request Param: score
5. DELETE /users/{userId} - Deregister a specific user from the contest.

## Validation and Error Handling
1. User ID: Must be unique.
2. Username: Must be provided.
3. Score: Must be between 0 and 100.
4. Badges: Automatically assigned based on score thresholds.
      - 1 <= Score < 30 -> Code Ninja
      - 30 <= Score < 60 -> Code Champ
      - 60 <= Score <= 100 -> Code Master
5. Error Codes:
      - 404 Not Found: User not found.
      - 400 Bad Request: Validation errors or invalid input.
      - 500 Internal Server Error: Server errors.
   
## Contact
If you have any questions or suggestions, feel free to contact me:

    Name: Amit Sharma
    Email: amit130391@gmail.com
    GitHub: amit130391 



