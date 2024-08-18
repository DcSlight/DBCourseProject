-- Table Definitions --

-- Countries --
CREATE TABLE countries (
    country_id SERIAL PRIMARY KEY,
    country VARCHAR(255) UNIQUE NOT NULL,
    vat_rate DECIMAL(5, 2) NOT NULL, 
    currency_type VARCHAR(3) NOT NULL, 
    currency_change DECIMAL(10, 2) NOT NULL 
);

-- Customer --
CREATE TABLE Customer (
    customer_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL,
    country_id INT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES countries(country_id) ON DELETE RESTRICT 
);

-- Contact --
CREATE TABLE Contact (
    contact_id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    whats_app VARCHAR(15) NOT NULL
);

-- Product --
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

-- Orders --
CREATE TABLE Orders (
    order_id VARCHAR(255) UNIQUE NOT NULL,
    amount INT NOT NULL,
    profit DECIMAL(10, 2),
    order_datetime TIMESTAMP DEFAULT NOW()
);

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

-- Shipping Company --
CREATE TABLE Shipping_Company (
    company_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

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
    FOREIGN KEY (company_id) REFERENCES Shipping_Company(company_id) ON DELETE CASCADE
);

-- Order Website --
CREATE TYPE eShipType AS ENUM ('eExpress', 'eStandard', 'eNone');

CREATE TABLE order_website (
    order_id VARCHAR(255) UNIQUE NOT NULL,
    ship_type eShipType NOT NULL DEFAULT 'eNone',
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
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
    FOREIGN KEY (to_country_id) REFERENCES countries(country_id) ON DELETE CASCADE
);

-- Shippment Route --
CREATE TABLE shippment_route (
    order_id VARCHAR(255),
    status_id INT,
    tracks_id INT,
    PRIMARY KEY (order_id, status_id, tracks_id),
    FOREIGN KEY (order_id) REFERENCES order_website(order_id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES shipping_status(status_code) ON DELETE CASCADE,
    FOREIGN KEY (tracks_id) REFERENCES tracks(track_id) ON DELETE CASCADE
);

-- Views --

-- View for shippment_route --
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
    customer.phone_number,
    customer.address,
    customer.country_id,
    make_order.product_id,
    orders.profit,
    product.type,
    product.product_name,
    product.cost_price,
    product.selling_price,
    product.stock,
    product.weight,
    make_order.order_id,
    orders.amount,
    orders.order_datetime
FROM 
    make_order
JOIN 
    customer ON make_order.customer_id = customer.customer_id
JOIN 
    product ON make_order.product_id = product.serial
JOIN 
    orders ON make_order.order_id = orders.order_id;

-- Triggers --

-- Orders calc the profit --
CREATE OR REPLACE FUNCTION calculate_order_profit()
RETURNS TRIGGER AS $$
BEGIN
    -- Update the profit in the orders table based on the product's selling price and cost price
    UPDATE orders
    SET profit = (SELECT (product.selling_price - product.cost_price) * orders.amount
                  FROM product, orders
                  WHERE product.serial = NEW.product_id AND orders.order_id = NEW.order_id)
    WHERE orders.order_id = NEW.order_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_insert_make_order
AFTER INSERT ON make_order
FOR EACH ROW
EXECUTE FUNCTION calculate_order_profit();

-- Deleting website order --
CREATE OR REPLACE FUNCTION cascade_delete_from_shippment_route()
RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM order_website WHERE order_id = OLD.order_id;
    
    DELETE FROM tracks WHERE track_id = OLD.tracks_id;
    
    DELETE FROM shipping_status WHERE status_code = OLD.status_id;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_delete_shippment_route
AFTER DELETE ON shippment_route
FOR EACH ROW
EXECUTE FUNCTION cascade_delete_from_shippment_route();

-- Sample Data --

-- Countries --
INSERT INTO countries (country_id, country, vat_rate, currency_type, currency_change) VALUES
(1, 'USA', 7.00, 'USD', 1.00),
(2, 'Israel', 17.00, 'ILS', 3.50),
(3, 'Germany', 19.00, 'EUR', 0.90);

-- Customer --
INSERT INTO customer (customer_id, full_name, phone_number, address, country_id) VALUES
(1, 'John Doe', '123-456-7890', '123 Main St, New York, NY', 1),
(2, 'Jane Smith', '987-654-3210', '456 Rothschild Blvd, Tel Aviv', 2),
(3, 'Max Mustermann', '555-123-4567', '789 Berliner Str, Berlin', 3);

-- Contact --
INSERT INTO contact (contact_id, full_name, whats_app) VALUES
(1, 'Alice Johnson', '+15551234567'),
(2, 'Bob Brown', '+972556789012');

-- Product --
INSERT INTO product (serial, product_name, type, cost_price, selling_price, stock, weight) VALUES
('P001', 'Laptop', 'eProductWebsite', 800.00, 1200.00, 50, 2.50),
('P002', 'Smartphone', 'eProductStore', 300.00, 600.00, 150, 0.40),
('P003', 'Headphones', 'eProductWholesalers', 20.00, 50.00, 500, 0.15);

-- Orders --
INSERT INTO orders (order_id, amount, profit, order_datetime) VALUES
('O001', 2, NULL, '2024-08-18 10:30:00'),
('O002', 3, NULL, '2024-08-18 11:00:00'),
('O003', 1, NULL, '2024-08-18 11:30:00');

-- Make Order --
INSERT INTO make_order (customer_id, product_id, order_id) VALUES
(1, 'P001', 'O001'),
(2, 'P002', 'O002'),
(3, 'P003', 'O003');

-- Shipping Company --
INSERT INTO shipping_company (company_id, name) VALUES
(1, 'DHL'),
(2, 'FedEx');

-- Shipping Company Contacts --
INSERT INTO shipping_company_contacts (company_id, contact_id) VALUES
(1, 1),
(2, 2);

-- Shipping Status --
INSERT INTO shipping_status (status_code, company_id, shipping_fee, status) VALUES
(1, 1, 25.00, 'eOnTheWay'),
(2, 2, 30.00, 'eOnTheWay');

-- Order Website --
INSERT INTO order_website (order_id, ship_type) VALUES
('O001', 'eExpress'),
('O002', 'eStandard'),
('O003', 'eNone');

-- Tracks --
INSERT INTO tracks (track_id, shippingType, from_country_id, date_departure, to_country_id, date_arrive) VALUES
(1, 'eShip', 1, '2024-08-17 08:00:00', 2, '2024-08-18 08:00:00'),
(2, 'ePlane', 2, '2024-08-17 09:00:00', 3, '2024-08-18 10:00:00');

-- Shippment Route --
INSERT INTO shippment_route (order_id, status_id, tracks_id) VALUES
('O001', 1, 1),
('O002', 2, 2);
