package Interfaces;

import Products.Product;

public interface ICommand {
	IShippingReceiver execute(double importTax);
	void setNewProduct(Product product);
}
