package Interfaces;

import Products.Product;

public interface ICommand {
	IShippingReceiver execute();
	void setNewProduct(Product product);
}
