# Merchant-order-desk-and-catalogue-engine
PayNest is a South African fintech building lightweight commerce tools for small merchants. This project implements a minimal commerce kernel in Java, enabling merchants to manage products, customers, and orders with consistent totals and receipts — without relying on expensive platforms.

## Project Overview
PayNest is a fictional platform that allows merchants to:
Customer.java: Represents customer details.
Order.java: Represents an order entity.
OrderItem.java: Represents items within an order.
Product.java: Represents product details.
OrderService.java: Contains business logic for order handling.
OrderItemTest.java: Unit test for the OrderItem domain class.

## How to Run the Project
### Prerequisites

-Java 21
-Maven 3.6+
-JUnit 5 


### Build and Run
```bash
#Compile the project

cd merchant-order-desk-and-catalogue-engine-Tshifhiwa13-main
cd paynestsystem 
mvn clean compile

# Run unit tests

mvn test

# Run the application

mvn exec:java
```


expected outcome

************* PAYMENT INVOICE *********

Order ID: 35
Customer: Tshifhiwa Makherana
Email: fifi@example.co.za

Product              Qty        Subtotal

Hammer               1          149,90
Electric Drill       1          1999,99
Box of Nails         2          240,10

Total Amount:                   2389,99


-Classes: Product, Customer, OrderItem, Order.

-Service: OrderService for creating orders and adding products.

-PayNestApplication  demonstrates the full flow: create products, create a customer, create an order, add products, and print a summary.

Project Structure


MERCHANT-ORDER-DESK-AND-CATALOGUE-ENGINE
└── paynestsystem
    ├── src
    │   ├── main/java/com/paynestsystem
    │   │   ├── app
    │   │   │   └── PayNestApplication.java
    │   │   ├── domain
    │   │   │   ├── Customer.java
    │   │   │   ├── Order.java
    │   │   │   ├── OrderItem.java
    │   │   │   └── Product.java
    │   │   └── service
    │   │       └── OrderService.java
    │   └── test/java/com/paynestsystem
    │       └── OrderItemTest.java
    ├── target
    ├── pom.xml
    └── README.md

