package Shipping;

import Interfaces.IShippingReceiver;

public class FedExReceiver implements IShippingReceiver {
	private double price;
	
	public FedExReceiver(double price) {
		this.price = price;
	}
	
	@Override
	public String getName() {
		return "FedEx";
	}

	@Override
	public double getPrice() {
		return this.price;
	}
}