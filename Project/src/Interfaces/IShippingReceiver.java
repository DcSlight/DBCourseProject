package Interfaces;

import Shipping.ShippingCompany;

public interface IShippingReceiver {
	ShippingCompany getCompany();
	double getPrice();
}
