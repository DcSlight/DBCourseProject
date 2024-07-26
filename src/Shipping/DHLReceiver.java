package Shipping;

import Interfaces.IShippingReceiver;

public class DHLReceiver implements IShippingReceiver {
	private double price;
	
	public DHLReceiver(double price) {
		this.price = price;
	}
	
	@Override
	public String getName() {
		return "DHL";
	}
	
	@Override
	public double getPrice() {
		return this.price;
	}
	
}
