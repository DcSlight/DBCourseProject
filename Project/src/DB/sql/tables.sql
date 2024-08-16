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
    profit DECIMAL(10, 2),
    order_datetime TIMESTAMP DEFAULT NOW(),
);

select * from product

-- Make Order --
CREATE TABLE make_order (
    customer_id INT,
    product_id VARCHAR(255),
    order_id VARCHAR(255),
    PRIMARY KEY (customer_id, product_id, order_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(serial) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- Shipping_Company --
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
	shipping_fee DECIMAL(10, 2) NOT NULL,
    status shipping_status_enum NOT NULL DEFAULT 'eOnTheWay',
    FOREIGN KEY (company_id) REFERENCES Shipping_Company(company_id) ON DELETE CASCADE,
);

-- Order Website --
CREATE TYPE eShipType AS ENUM ('eExpress', 'eStandard', 'eNone');

CREATE TABLE order_website (
    order_id VARCHAR(255) NOT NULL,
	ship_type eShipType NOT NULL DEFAULT 'eNone',
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
);

-- ship_type enum --
CREATE TYPE ship_type AS ENUM ('eShip', 'ePlane', 'eTruck');
-- Tracks --
CREATE TABLE tracks (
    track_id SERIAL PRIMARY KEY, 
    shippingType ship_type NOT NULL,
    from_country_id INT NOT NULL,
    date_departure TIMESTAMP NOT NULL,
    to_country_id INT NOT NULL,
    date_arrive TIMESTAMP NOT NULL,
    FOREIGN KEY (from_country_id) REFERENCES countries(country_id) ON DELETE CASCADE,
    FOREIGN KEY (to_country_id) REFERENCES countries(country_id) ON DELETE CASCADE,
);
SELECT * FROM orders
	
-- VIEW FOR ORDERS --	
CREATE OR REPLACE VIEW shippment_route_view AS
SELECT 
    shippment_route.order_id,
    orders.order_datetime,
    shippment_route.status_id,
    shipping_status.company_id,
    shipping_status.status, 
    shippment_route.tracks_id,
    tracks.shippingType, 
    tracks.from_country_id,
    tracks.to_country_id,
    tracks.date_departure,
    tracks.date_arrive,
	NOW() > tracks.date_arrive AS has_arrive
FROM 
    shippment_route
JOIN 
    orders ON shippment_route.order_id = orders.order_id
JOIN 
    shipping_status ON shippment_route.status_id = shipping_status.status_code
JOIN 
    tracks ON shippment_route.tracks_id = tracks.track_id;

-- View for make_order --

CREATE OR REPLACE VIEW make_order_view AS
SELECT 
    make_order.customer_id,
    customer.full_name,
	customer.address,
	customer.country_id,
    make_order.product_id,
    product.product_name,
    make_order.order_id,
    orders.order_datetime
FROM 
    make_order
JOIN 
    customer ON make_order.customer_id = customer.customer_id
JOIN 
    product ON make_order.product_id = product.serial
JOIN 
    orders ON make_order.order_id = orders.order_id;

-- Shippment Route --
CREATE TABLE shippment_route (
    order_id VARCHAR(255),
    status_id INT,
    tracks_id INT,
    PRIMARY KEY (order_id, status_id, tracks_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES shipping_status(status_code) ON DELETE CASCADE,
    FOREIGN KEY (tracks_id) REFERENCES tracks(track_id) ON DELETE CASCADE
);
