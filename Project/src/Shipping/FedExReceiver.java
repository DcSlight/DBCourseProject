package Shipping;

import Interfaces.IShippingReceiver;

public class FedExReceiver implements IShippingReceiver {
	private double price;
	private FedEx fedEx;
	
	public FedExReceiver(double price,FedEx fedEx) {
		this.price = price;
		this.fedEx = fedEx;
	}

	@Override
	public double getPrice() {
		return this.price;
	}

	@Override
	public ShippingCompany getCompany() {
		return this.fedEx;
	}
}