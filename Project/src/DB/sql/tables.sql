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
    cost_price DECIMAL(10, 2) NOT NULL,
    selling_price DECIMAL(10, 2) NOT NULL,
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
	FOREIGN KEY (product_serial) REFERENCES Product(serial) ON DELETE RESTRICT ,
	FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE RESTRICT
)

select * from orders


CREATE TABLE Shipping_Company (
    company_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    feeExpress DECIMAL(5, 2) CHECK (feeExpress >= 0 AND feeExpress <= 100),
    feeStandard DECIMAL(5, 2) CHECK (feeStandard >= 0 AND feeStandard <= 100)
);

select * from Shipping_Company



