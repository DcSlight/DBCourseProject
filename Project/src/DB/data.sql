--Customer Data--
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Yossi', '050-6007070', '123 Main St, Tel Aviv', 1);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Amit', '054-1234567', '456 Elm St, New York', 2);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Lior', '052-7654321', '789 Oak St, Berlin', 3);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Dina', '053-9876543', '101 Pine St, Paris', 4);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Noa', '050-1122334', '202 Maple St, London', 5);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Tal', '054-5566778', '303 Birch St, Toronto', 6);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Ron', '052-8899000', '404 Cedar St, Tokyo', 7);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Maya', '053-6677889', '505 Palm St, Sydney', 8);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Eli', '050-4455667', '606 Willow St, Beijing', 9);
INSERT INTO Customer(full_name, phone_number, address, country_id) VALUES ('Sara', '054-9988776', '707 Spruce St, Mumbai', 10);


--Contact Data--
INSERT INTO Contact(full_name, whats_app) VALUES ('John Smith', '050-6007070');
INSERT INTO Contact(full_name, whats_app) VALUES ('Jane Doe', '054-1234567');
INSERT INTO Contact(full_name, whats_app) VALUES ('Michael Johnson', '052-7654321');
INSERT INTO Contact(full_name, whats_app) VALUES ('Emily Davis', '053-9876543');
INSERT INTO Contact(full_name, whats_app) VALUES ('William Brown', '050-1122334');
INSERT INTO Contact(full_name, whats_app) VALUES ('Olivia Taylor', '054-5566778');
INSERT INTO Contact(full_name, whats_app) VALUES ('James Miller', '052-8899000');
INSERT INTO Contact(full_name, whats_app) VALUES ('Sophia Wilson', '053-6677889');
INSERT INTO Contact(full_name, whats_app) VALUES ('Benjamin Anderson', '050-4455667');
INSERT INTO Contact(full_name, whats_app) VALUES ('Mia Thomas', '054-9988776');


--Product--
INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('AAB12', 'Iphone 15 protector', 7.5, 87.58, 400, 0.25, 'eProductWebsite'::product_type);

INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('199BA', 'TV', 1200, 2750, 10, 13.6, 'eProductWebsite'::product_type);

INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('78FHC', 'JBL', 210.5, 453.2, 62, 1.23, 'eProductWebsite'::product_type);

INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('AHDHB2', 'Battery', 12.5, 25, 120, 0.2, 'eProductStore'::product_type);

INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('AFCHP7', 'Coat', 45.6, 350, 62, 1.7, 'eProductStore'::product_type);

INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('PDKSU2', 'T-Shirt', 12.5, 98, 158, 1.1, 'eProductStore'::product_type);


INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('P3MCJU', 'Coca Cola', 0.5, 10, 2500, 0.6, 'eProductWholesalers'::product_type);

INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('MXJQXT', 'Mayo', 15, 17, 1230, 5, 'eProductWholesalers'::product_type);

INSERT INTO Product (serial, product_name, cost_price, selling_price, stock, weight, type) 
VALUES ('MPXL2K', 'Toilet Paper', 5, 45, 5800, 3, 'eProductWholesalers'::product_type);

--Country--
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Israel', 17.00, 'ILS', 3.70);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('United States', 0.00, 'USD', 1.00);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Germany', 19.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('France', 20.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('United Kingdom', 20.00, 'GBP', 0.74);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Canada', 5.00, 'CAD', 1.25);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Japan', 10.00, 'JPY', 109.45);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Australia', 10.00, 'AUD', 1.35);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('China', 13.00, 'CNY', 6.45);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('India', 18.00, 'INR', 74.50);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Brazil', 17.00, 'BRL', 5.20);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Mexico', 16.00, 'MXN', 20.00);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Russia', 20.00, 'RUB', 73.50);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('South Africa', 15.00, 'ZAR', 14.50);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('South Korea', 10.00, 'KRW', 1150.00);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Turkey', 18.00, 'TRY', 8.50);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Saudi Arabia', 15.00, 'SAR', 3.75);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Argentina', 21.00, 'ARS', 98.50);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Switzerland', 7.70, 'CHF', 0.92);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Norway', 25.00, 'NOK', 8.70);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Sweden', 25.00, 'SEK', 8.60);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Denmark', 25.00, 'DKK', 6.30);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Finland', 24.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Netherlands', 21.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Belgium', 21.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Austria', 20.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Italy', 22.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Spain', 21.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Portugal', 23.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Greece', 24.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Ireland', 23.00, 'EUR', 0.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Poland', 23.00, 'PLN', 3.85);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Hungary', 27.00, 'HUF', 310.00);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Czech Republic', 21.00, 'CZK', 21.70);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Romania', 19.00, 'RON', 4.10);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('New Zealand', 15.00, 'NZD', 1.45);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Singapore', 7.00, 'SGD', 1.35);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Malaysia', 6.00, 'MYR', 4.15);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Indonesia', 10.00, 'IDR', 14200.00);
INSERT INTO countries (country, vat_rate, currency_type, currency_change) VALUES ('Thailand', 7.00, 'THB', 33.00);




