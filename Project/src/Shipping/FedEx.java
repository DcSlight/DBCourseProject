package Shipping;

import Components.Contact;
import Observer.ObserverManagment;

public class FedEx extends ShippingCompany{
	protected final int PRODUCT_WEIGHT = 10;
	private final int FEE_EXPRESS = 50;
	private final int FEE_STANDARD = 10;
	
	public FedEx(int companyId) {
		super(companyId);
	}
	
	//TODO: fix
	public double calculateExpressShippingFee(double weight) {
    	return (weight / PRODUCT_WEIGHT)*FEE_EXPRESS + importTax;
    }
	
	public double calculateStandardShippingFee(double weight) {
    	return (weight / PRODUCT_WEIGHT)*FEE_STANDARD;
    }

	@Override
	public String getName() {
		return "FedEx";
	}

	@Override
	public void update(ObserverManagment obs) {
		System.out.println("FedEx get message: " + obs.getMsg());
	}
}
