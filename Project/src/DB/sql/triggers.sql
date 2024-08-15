--Triggers --

-- Orders --
CREATE OR REPLACE FUNCTION calculate_profit()
RETURNS TRIGGER AS $$
BEGIN
    -- Calculate the profit based on the product's selling price and cost price
    NEW.profit := (SELECT (p.selling_price - p.cost_price) * NEW.amount
                   FROM Product p
                   WHERE p.serial = NEW.product_serial);
    
    -- Return the modified row to be inserted
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_insert_order
BEFORE INSERT ON Orders
FOR EACH ROW
EXECUTE FUNCTION calculate_profit();

--Website Order--
--DELETE Website order will delte it from shipping_status and tracks--
CREATE OR REPLACE FUNCTION delete_related_shipping_and_tracks() 
RETURNS TRIGGER AS $$
BEGIN
    -- Step 1: Delete records from tracks where the status_code matches
    DELETE FROM tracks 
    WHERE shipping_status_id IN (
        SELECT status_code FROM shipping_status WHERE order_id = OLD.order_id
    );
    
    -- Step 2: Delete the corresponding records from shipping_status
    DELETE FROM shipping_status 
    WHERE order_id = OLD.order_id;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER delete_related_on_order_delete
AFTER DELETE ON order_website
FOR EACH ROW
EXECUTE FUNCTION delete_related_shipping_and_tracks();


