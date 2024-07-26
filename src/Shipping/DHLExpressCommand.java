package Shipping;

import Products.Product;
import Interfaces.ICommand;
import Interfaces.IShippingReceiver;


public class DHLExpressCommand implements ICommand {
	private DHL dhl;

    public DHLExpressCommand(DHL dhl) {
        this.dhl = dhl;
    }
    
    @Override
	public void setNewProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public IShippingReceiver execute() {
    	double price =  dhl.calculateExpressShippingFee();
    	IShippingReceiver receiver = new DHLReceiver(price);
        return receiver;
    }

}
