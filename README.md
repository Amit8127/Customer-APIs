# Customer Application REST APIs
* **Create a Customer:** User can create a customer with valid details.
* **Update a Customer:** User can update an existing customer's details by customerId.
* **Retrieve Customers:** User can retrieve specific customer by customerId.
* **Retrieve Customers Pages:** User can retrieve customers based on pageSize, pageNumber, ascending and descending order.
* **Delete a Customer:** User can delete a customer by customerId.
* **Sync Customers data from SUNBASE API:** User can Sync Customers data from SUNBASE database.
## Technologies Used
* **Java 8+**
* **Spring Boot**
* **Spring MVC**
* **Spring Data JPA**
* **JWT Authentication**
* **MySQL (as the database)**
* **Maven (for dependency management)**

## Prerequisites
* Java Development Kit (JDK) version 8 or higher
* Spring Boot
* Maven

## Getting Started
To set up the project on your local machine, follow these steps:

1. Clone the repository: `git clone https://github.com/Amit8127/Customer-APIs.git`
2. Navigate to the project directory: `cd Customer-APIs`
3. Configure the database settings in `application.properties` file.
4. Build the project using Maven: `mvn clean install`
5. Run the application: `mvn spring-boot:run`
6. The application will be accessible at `http://localhost:8080`.

## Database Setup
This project uses MySQL as the database. Follow these steps to set up the database:
1. Install MySQL on your local machine.
2. Create a new database named customerData.
3. Update the database configuration in `application.properties` file.

## Authentication APIs
### User Signup
* Method: POST
* Endpoint: `/auth/signup`
* Request Body:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
#### Response:
* Success (HTTP 201 Created):
```json
{
  "id": 1,
  "email": "user@example.com",
  "password": "$2a$10$MLPghyk5df82cUI8QGrCiOQXV8t/ES6q2Jt4LXPAT043xCIDFlaAy"
}
```
* Failure (HTTP 400 Bad Request):
```text
User already Present with email : user@example.com
```

### User Login
* Method: POST
* Endpoint: `/auth/login`
* Request Body:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
#### Response:
* Success (HTTP 200 OK):
```json
{
  "jwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "username": "user@example.com"
}
```
* Failure (HTTP 401 UNAUTHORIZED):
```text
Access Denied !!  Invalid Username or Password  !!
```

## Customer APIs
### Create Customer
* Method: POST
* Endpoint: `/home/customer/createACustomer`
* Request Body:
```json
{
  "first_name": "first_name",
  "last_name": "last_name",
  "street": "street",
  "address": "address",
  "city": "city",
  "state": "state",
  "email": "email@gmail.com",
  "phone": "1234567891"
}
```
#### Response:
* Success (HTTP 201 Created):
```json
{
  "id": 1,
  "uuid": "0b999b0c-5d4f-487a-b55d-b5d2a7114722",
  "first_name": "first_name",
  "last_name": "last_name",
  "street": "street",
  "address": "address",
  "city": "city",
  "state": "state",
  "email": "email@gmail.com",
  "phone": "1234567891"
}
```
* Failure (HTTP 400 Bad Request):
```text
Customer Already Exist With This EmailId: email@gmail.com
```
### Update Customer
* Method: PUT
* Endpoint: `/home/customer/updateACustomer/{id}`
* Request Body:
```json
{
  "first_name": "update_first_name",
  "last_name": "update_last_name",
  "street": "street",
  "address": "address",
  "city": "city",
  "state": "state",
  "email": "update_email@gmail.com",
  "phone": "1234567891"
}
```
#### Response:
* Success (HTTP 200 OK):
```json
{
  "id": 1,
  "uuid": "0b999b0c-5d4f-487a-b55d-b5d2a7114722",
  "first_name": "update_first_name",
  "last_name": "update_last_name",
  "street": "street",
  "address": "address",
  "city": "city",
  "state": "state",
  "email": "update_email@gmail.com",
  "phone": "1234567891"
}
```
* Failure (HTTP 400 Bad Request):
```text
Customer Is Not Exist With This Id: 1
```

### Get Customers Page
* Method: GET
* Endpoint: `/home/customer/getCustomerPages`
* Params: 
  * pageNum: 1
  * pageSize: 2
  * search: id
  * direction: ASC
#### Response:
* Success (HTTP 200 OK):
```json
[
  {
    "id": 1,
    "uuid": "0b999b0c-5d4f-487a-b55d-b5d2a7114722",
    "first_name": "first_name",
    "last_name": "last_name",
    "street": "street",
    "address": "address",
    "city": "city",
    "state": "state",
    "email": "email@gmail.com",
    "phone": "1234567891"
  },
  {
    "id": 2,
    "uuid": "0b555b0c-5d4f-487a-b55d-b5d2a0014722",
    "first_name": "first_name1",
    "last_name": "last_name1",
    "street": "street",
    "address": "address",
    "city": "city",
    "state": "state",
    "email": "email1@gmail.com",
    "phone": "1987654321"
  }
]
```
* Failure (HTTP 400 Bad Request):
```text
Error Message (Dynamic)
```


### Get Customer by ID
* Method: GET
* Endpoint: `/home/customer/getCustomerById/{id}`
#### Response:
* Success (HTTP 200 OK):
```json
{
  "first_name": "first_name1",
  "last_name": "last_name1",
  "street": "street",
  "address": "address",
  "city": "city",
  "state": "state",
  "phone": "1987654321",
  "email": "email1@gmail.com"  
}
```
* Failure (HTTP 400 Bad Request):
```text
Customer Is Not Exist With This Id: 1
```

### Delete Customer by ID
* Method: DELETE
* Endpoint: `/home/customer/deleteACustomerById/{id}`
#### Response:
* Success (HTTP 200 OK):
```text
Customer Has Been Successfully Deleted
```
* Failure (HTTP 400 Bad Request):
```text
Customer Is Not Exist With This Id: 1
```

### Sync Customers data from Sunbase database
* Method: GET
* Endpoint: `/home/customer/getDataFromSunbase`
#### Response:
* Success (HTTP 200 OK):
```text
Data has been synced Successfully but Error found in some data validation

or 

Error found in data validation

or 

Data has been synced Successfully
```
* Failure (HTTP 400 Bad Request):
```text
Error Message (Dynamic)
```

## Project Doc Link
https://docs.google.com/document/d/1xb1WFJN8S1xjx0gNxR3PX3ah13LNgySKBJv9FS3Pzq8/edit?usp=sharing

## SignUp ScreenShot
![Img1.png](src%2Fmain%2Fjava%2Fcom%2Fdriver%2FImages%2FImg1.png)

## Login ScreenShot
![Img2.png](src%2Fmain%2Fjava%2Fcom%2Fdriver%2FImages%2FImg2.png)

## Table ScreenShot
![Img3.png](src%2Fmain%2Fjava%2Fcom%2Fdriver%2FImages%2FImg3.png)

## Add Customer Form
![Img4.png](src%2Fmain%2Fjava%2Fcom%2Fdriver%2FImages%2FImg4.png)

