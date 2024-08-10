CREATE TABLE Customer (
    customer_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL
);

select * from customer

CREATE TABLE Contact (
    contact_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    whats_app VARCHAR(15) NOT NULL
);

select * from contact

CREATE TYPE product_type AS ENUM ('eProductWebsite', 'eProductStore', 'eProductWholesalers');

CREATE TABLE Product (
    serial VARCHAR(255) PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    type product_type NOT NULL,
    cost_price DECIMAL(10, 2) NOT NULL,
    selling_price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    weight DECIMAL(10, 2) NOT NULL
);

select * from Product

INSERT INTO Product (selling_price, serial, weight, type, stock, product_name, cost_price) 
VALUES (87.58, 'AAB12', 0.25, 'eProductWebsite', 400, 'Iphone 15 protector', 7.5);




