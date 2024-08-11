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