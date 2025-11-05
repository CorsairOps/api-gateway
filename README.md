# API Gateway
This repository contains the API Gateway for CorsairOps. The API Gateway serves as the single entry point for all client requests, routing them to the appropriate microservices and handling cross-cutting concerns such as authentication, logging, and rate limiting.

## Features
- Request Routing: Routes incoming requests to the appropriate microservices based on the request path and method.
- Authentication: Integrates with authentication services to validate user credentials and manage sessions.
- Authorization: Enforces access control policies to ensure users can only access resources they are permitted to.
- Rate Limiting: Implements rate limiting to protect backend services from excessive requests.
- Logging: Logs incoming requests and outgoing responses for monitoring and debugging purposes.
- Error Handling: Provides standardized error responses for client requests.

## Technologies Used
- Java 21
- Spring Boot
- Spring Cloud Gateway
- Maven

## API Documentation
To view the API documentation, start the API Gateway and navigate to `http://localhost:9000/swagger-ui.html`.
API Documentation is aggregated for all services behind the gateway.

## Environment Variables
```
JWT_ISSUER_URI=
API_GATEWAY_URL=
ASSET_SERVICE_URL=
USER_SERVICE_URL=
MISSION_SERVICE_URL=
AUTH_SERVICE_URL=
CLIENT_URL=
MAINTENANCE_SERVICE_URL=
KAFKA_BOOTSTRAP_SERVERS=
```