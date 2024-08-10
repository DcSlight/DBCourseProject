package Shipping;

import Interfaces.IShippingReceiver;

public class DHLReceiver implements IShippingReceiver {
	private double price;
	private DHL dhl;
	
	public DHLReceiver(double price,DHL dhl) {
		this.price = price;
		this.dhl = dhl;
	}

	
	@Override
	public double getPrice() {
		return this.price;
	}


	@Override
	public ShippingCompany getCompany() {
		return dhl;
	}
	
}
