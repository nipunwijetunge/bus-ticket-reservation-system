# Bus Ticketing System

A simple REST API for bus ticket reservation system built using Core Java (Java Net HttpHandler) and Maven.
This simple REST API does not use any frameworks like Spring Boot, JAX-RS or even Jakarta Servlets. It is built using Java's built-in HTTP server capabilities.

## Features

- Check seat availability for bus routes
- Make seat reservations
- Simple web interface
- RESTful API endpoints

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Running the Application

1. Go to the directory where `BusTicketingSystem.war` is located.
2. On Mac, Open terminal and run `java -xvf BusTicketingSystem.war`
3. On Windows, `jar -xvf BusTicketingSystem.war`
4. Then run `java -cp "WEB-INF/classes:WEB-INF/lib/*" com.example.bus.MainServer`
5. On Windows, `java -cp "WEB-INF\classes;WEB-INF\lib\*" com.example.bus.MainServer`


The server will start on the default port. Access the web interface at `http://localhost:8080`

## API Endpoints

### 1. GUI
- **GUI is accessible at:** `http://localhost:8080`

### 2. Check Availability
- **URL:** `/api/availability`
- **Method:** POST
- **Request Body:**
```json
{
    "origin": "string",
    "destination": "string",
    "passengers": number
}
```
- **Success Response:** HTTP 200
```json
{
    "status": number,
    "message": "string",
    "data": {}
}
```
- **Error Response:** HTTP 400 (Bad Request)

### 3. Make Reservation
- **URL:** `/api/reserve`
- **Method:** POST
- **Request Body:**
```json
{
    "origin": "string",
    "destination": "string",
    "passengers": number
}
```
- **Success Response:** HTTP 200
```json
{
    "status": number,
    "message": "string",
    "data": {}
}
```
- **Error Response:** HTTP 400 (Bad Request)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/bus/
│   │       ├── dto/         # Data Transfer Objects
│   │       ├── handler/     # HTTP Request Handlers
│   │       ├── model/       # Business Models
│   │       ├── service/     # Business Logic
│   │       └── util/        # Utility Classes
│   └── resources/
│       ├── static/         # Static Web Resources
│       └── log4j2.xml      # Logging Configuration
```

## Technologies Used

- Core Java
- Maven
- JUnit 5 & Mockito for testing
- Log4j2 for logging
- GSON for JSON processing
- Project Lombok for reducing boilerplate code

## Suggested Improvements

1. **Security Enhancements**
   - Add authentication and authorization
   - Implement rate limiting
   - Add input validation and sanitization

2. **Feature Additions**
   - Add user management system
   - Implement payment gateway integration
   - Add email notifications for reservations
   - Support for multiple bus operators

3. **Technical Improvements**
   - Add database integration for persistent storage
   - Implement caching mechanism
   - Add API documentation using Swagger/OpenAPI
   - Implement circuit breakers for fault tolerance
   - Add monitoring and metrics

4. **UI/UX Improvements**
   - Develop a modern frontend using React/Angular
   - Add mobile responsive design
   - Implement real-time seat mapping
   - Add booking history and management interface

5. **Operational Improvements**
   - Containerize the application using Docker
   - Set up CI/CD pipeline
   - Implement automated testing
   - Add performance monitoring
   - Set up proper logging and monitoring infrastructure
