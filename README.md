# Solea Cargo Dispatcher

A **Spring Boot application** for managing cargo dispatching to planets.  
The application allows management of **products**, **planets**, **vehicles**, and **orders**, including assigning the **optimal vehicle** based on capacity and distance.

---

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Built With](#built-with)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Database Configuration](#database-configuration)
- [Testing](#testing)
- [Sample Request Payloads](#-sample-request-payloads)
- [Contact](#contact)

---

## About the Project

**Solea Cargo Dispatcher** is designed to simulate an interplanetary cargo dispatch system.  
It provides REST APIs for managing products, planets, and vehicles, and supports creating and processing orders automatically using the most suitable vehicle.

---

## Features

- Manage **Products** with size information
- Manage **Planets** with distance from the origin
- Manage **Vehicles** with speed and capacity
- Place **Orders** for products to planets
- Automatically assign the **suitable vehicle** based on order volume and distance
- Retrieve order details, including assigned vehicle and travel time
- REST API endpoints with JSON input/output
- Database-backed persistence using JPA/Hibernate and H2.

---

## Built With

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Data JPA / Hibernate**
-  **H2 Database** (in-memory for dev/testing)
-  **Lombok**
-  **JUnit 5 + Mockito**
-  **Maven**

---

## Getting Started

Follow these steps to set up the project locally.

### Prerequisites

- Install **Java 21+**
- Install **Maven**
- (Optional) Install **IntelliJ IDEA** or your preferred IDE

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/vjay154-droid/solea-cargo-dispatcher-db.git
   cd solea-cargo-dispatcher-db
2. Build the project:
    ````bash
   mvn clean install

3. Run the application:
    ````bash
    mvn spring-boot:run

The server will start at:
    http://localhost:3004

You can change the port in src/main/resources/application.yaml.

---
## API Documentation

All APIs are documented using Swagger/OpenAPI.  
You can access the Swagger UI after starting the application:

**Swagger UI:** http://localhost:3004/swagger-ui/index.html

It provides interactive documentation for all endpoints including request/response examples.

---
## API Endpoints
### Products

- `GET /products` — list all products

- `GET /products/{id}` — get product by ID

- `POST /products` — create a new product

- `PATCH /products/{id}` — update an existing product (one or more fields)

### Planets

- `GET /planets` — list all planets

- `GET /planets/{id}` — get planet details

### Vehicles

- `GET /vehicles` — list all vehicles

- `GET /vehicles/{id}` — get vehicle details

### Orders

- `POST /orders` — place an order (payload includes planet ID + items). Automatically assigns the best vehicle.

- `GET /orders` — list all orders

- `GET /orders/{id}` — get order details

---
## Database Configuration

The project uses an H2 in-memory database by default.

Configuration file:
`src/main/resources/application.yaml`

You can easily switch to another database by updating the JPA datasource properties.

### H2 database console setup
We can access the h2 console here: http://localhost:3004/h2-console 
JDBC url : jdbc:h2:mem:soleadb
username: sa
password:
(password is empty)

---
## Testing
Run all unit tests with:
```bash
  mvn test
```
The project uses JUnit 5 and Mockito for unit testing.

---
## 🧪 Sample Request Payloads

### Create Product
```json
  {
    "name": "New Product",
    "size": 5.0
  }
```
### Place Order
```json
    {
        "planetId": 1,
        "orderItemDTOList": [
            { "productId": 1, "quantity": 5 },
            { "productId": 3, "quantity": 2 }
        ]
    }
```
---
## Contact
    Author: Vijay
    📧 vjay154@gmail.com
    🔗 https://github.com/vjay154-droid