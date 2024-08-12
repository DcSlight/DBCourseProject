package Shipping;

import Interfaces.ICommand;
import Interfaces.IShippingReceiver;
import Products.Product;

public class FedExStandardCommand implements ICommand {
	private FedEx fedEx;
	private double weight;

    public FedExStandardCommand(FedEx fedEx) {
        this.fedEx = fedEx;
        this.weight = 0;
    }
    
    @Override
	public void setNewProduct(Product product) {
    	this.weight = product.getWeight();
	}

    @Override
    public IShippingReceiver execute(double importTax) {
        double price = fedEx.calculateStandardShippingFee(weight,importTax);
        IShippingReceiver receiver = new FedExReceiver(price,fedEx);
        return receiver;
    }
	
}
