-- Transactions --

BEGIN;

-- Insert into the order_website table
INSERT INTO order_website (order_id, status_code)
VALUES (
    'ORD011', 
    (SELECT status_code FROM shipping_status WHERE order_id = 'ORD011' AND company_id = 1)
);

-- Insert into the shipping_status table and get the generated status_code
INSERT INTO shipping_status (company_id, order_id)
VALUES (1, 'ORD011')
RETURNING status_code;

-- The above insertion returns the `status_code` which can then be used in `order_website`

COMMIT;