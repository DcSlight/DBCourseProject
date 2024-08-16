--Triggers --

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