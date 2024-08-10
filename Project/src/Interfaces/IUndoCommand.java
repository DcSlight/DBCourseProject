package Interfaces;

import Exception.StockException;
import Products.Product;

public interface IUndoCommand {
	void execute() throws StockException;
	void undo();
	Product getProduct();
}
