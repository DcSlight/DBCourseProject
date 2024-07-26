package Shipping;

import Interfaces.ICommand;
import Interfaces.IShippingReceiver;
import Products.Product;

public class DHLStandardCommand implements ICommand {
	private DHL dhl;
	private double sellingPrice;

    public DHLStandardCommand(DHL dhl) {
        this.dhl = dhl;
        this.sellingPrice = 0;
    }
    
    @Override
	public void setNewProduct(Product product) {
    	this.sellingPrice = product.getSellingPrice();
	}

    @Override
    public IShippingReceiver execute() {
        double price = dhl.calculateStandardShippingFee(sellingPrice);
        IShippingReceiver receiver = new DHLReceiver(price);
        return receiver;
    }

}
