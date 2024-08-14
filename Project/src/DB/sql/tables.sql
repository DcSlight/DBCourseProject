--Customer--
CREATE TABLE Customer (
    customer_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL,
    country_id INT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES countries(country_id) ON DELETE RESTRICT 
);

select * from customer

--Contact--
CREATE TABLE Contact (
    contact_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    whats_app VARCHAR(15) NOT NULL
);

select * from contact

--Product--
CREATE TYPE product_type AS ENUM ('eProductWebsite', 'eProductStore', 'eProductWholesalers');

CREATE TABLE Product (
    serial VARCHAR(255) PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    type product_type NOT NULL,
    cost_price DECIMAL(10, 2) NOT NULL CHECK (cost_price > 0),
    selling_price DECIMAL(10, 2) NOT NULL CHECK (selling_price > 0),
    stock INT NOT NULL,
    weight DECIMAL(10, 2) NOT NULL
);

select * from Product

--Countries--
CREATE TABLE countries (
    country_id SERIAL PRIMARY KEY,
    country VARCHAR(255) UNIQUE NOT NULL,
    vat_rate DECIMAL(5, 2) NOT NULL, 
    currency_type VARCHAR(3) NOT NULL, 
    currency_change DECIMAL(10, 2) NOT NULL 
);

select * from countries


--Orders--
CREATE TABLE Orders (
    order_id VARCHAR(255) UNIQUE NOT NULL,
    amount INT NOT NULL,
    profit DECIMAL(10, 2) NOT NULL,
    product_serial VARCHAR(255) NOT NULL,
    customer_id INT NOT NULL,
    order_datetime TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (product_serial) REFERENCES Product(serial) ON DELETE RESTRICT,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE RESTRICT
);

select * from orders


CREATE TABLE Shipping_Company (
    company_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

select * from Shipping_Company

CREATE TABLE shipping_company_contacts (
    company_id INT NOT NULL,
    contact_id INT NOT NULL,
    PRIMARY KEY (company_id, contact_id),
    FOREIGN KEY (company_id) REFERENCES Shipping_Company(company_id),
    FOREIGN KEY (contact_id) REFERENCES contact(contact_id)
);

-- Shipping Status --
CREATE TYPE shipping_status_enum AS ENUM ('eArrive', 'eOnTheWay');

CREATE TABLE shipping_status (
    status_code SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    order_id VARCHAR(255) NOT NULL,
    status shipping_status_enum NOT NULL DEFAULT 'eOnTheWay',
    FOREIGN KEY (company_id) REFERENCES Shipping_Company(company_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

select * from orders

-- Order Website --

CREATE TABLE order_website (
    order_id VARCHAR(255) NOT NULL,
    status_code INT NOT NULL,
    shipping_price DOUBLE PRECISION DEFAULT 0,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE casade,
    FOREIGN KEY (status_code) REFERENCES shipping_status(status_code) ON DELETE RESTRICT
);


















