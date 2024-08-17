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

--deleteing website order --
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