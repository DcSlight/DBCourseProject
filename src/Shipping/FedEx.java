package Shipping;

import Components.Contact;

public class FedEx extends ShippingCompany{
	protected final int PRODUCT_WEIGHT = 10;
	private final int FEE_EXPRESS = 50;
	private final int FEE_STANDARD = 10;
	
	public FedEx(Contact contact,int importTax) {
		super(contact,importTax);
	}
	
	public double calculateExpressShippingFee(double weight) {
    	return (weight / PRODUCT_WEIGHT)*FEE_EXPRESS + importTax;
    }
	
	public double calculateStandardShippingFee(double weight) {
    	return (weight / PRODUCT_WEIGHT)*FEE_STANDARD;
    }
}
