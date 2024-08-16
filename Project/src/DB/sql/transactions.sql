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


--website order--
BEGIN;

INSERT INTO order_website (order_id, ship_type)
VALUES ('ORD123', 'eExpress');

WITH inserted_status AS (
    INSERT INTO shipping_status (company_id, shipping_fee, status)
    VALUES (1, 100.00, 'eOnTheWay')
    RETURNING status_code
),

inserted_tracks AS (
    INSERT INTO tracks (shippingType, from_country_id, date_departure, to_country_id, date_arrive)
    VALUES 
        ('eShip', 1, '2024-08-15 10:00:00', 2, '2024-08-20 18:00:00'),
        ('ePlane', 1, '2024-08-16 12:00:00', 3, '2024-08-21 14:00:00'),
        ('eTruck', 2, '2024-08-17 14:00:00', 4, '2024-08-22 16:00:00')
    RETURNING track_id, shippingType
)

INSERT INTO shippment_route (order_id, status_id, tracks_id)
SELECT 'ORD123', inserted_status.status_code, track_id
FROM inserted_tracks, inserted_status;

COMMIT;

