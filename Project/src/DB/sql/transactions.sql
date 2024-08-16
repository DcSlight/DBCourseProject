-- transactions --
-- example for inserting new order --
DO $$
DECLARE
    generated_status_code INT;
BEGIN
    -- Step 1: Insert into shipping_status and capture the generated status_code
    INSERT INTO shipping_status (company_id, order_id)
    VALUES (1, 'ORD035')
    RETURNING status_code INTO generated_status_code;

    -- Step 2: Insert into order_website using the captured status_code
    INSERT INTO order_website (order_id, status_code)
    VALUES ('ORD035', generated_status_code);

    -- Step 3: Insert two records into the tracks table using the captured status_code
    INSERT INTO tracks (shippingType, from_country_id, date_departure, to_country_id, date_arrive, shipping_status_id)
    VALUES 
    ('eShip', 1, '2024-08-15 10:00:00', 2, '2024-08-20 18:00:00', generated_status_code),
    ('ePlane', 2, '2024-08-21 06:00:00', 3, '2024-08-22 12:00:00', generated_status_code);

    -- Commit the transaction
    COMMIT;
END $$;



--make order ---
BEGIN;
INSERT INTO orders (order_id, amount, profit, order_datetime)
VALUES ('ORD123', 10, 200.00, NOW());
UPDATE product
SET stock = stock - 10
WHERE serial = 'PDKSU2';
DO $$
BEGIN
    IF (SELECT stock FROM product WHERE serial = 'PDKSU2') < 0 THEN
        RAISE EXCEPTION 'Not enough stock for product PDKSU2';
    END IF;
END $$;
INSERT INTO make_order (customer_id, product_id, order_id)
VALUES (1, 'PDKSU2', 'ORD123');

COMMIT;


