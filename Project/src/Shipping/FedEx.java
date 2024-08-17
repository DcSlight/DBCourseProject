package Shipping;
import Observer.ObserverManagment;

public class FedEx extends ShippingCompany{
	protected final int PRODUCT_WEIGHT = 10;
	private final int FEE_EXPRESS = 50;
	private final int FEE_STANDARD = 10;
	
	public FedEx(int companyId) {
		super(companyId);
	}
	
	public double calculateExpressShippingFee(double weight,double importTax) {
    	return (weight / PRODUCT_WEIGHT)*FEE_EXPRESS + importTax;
    }
	
	public double calculateStandardShippingFee(double weight,double importTax) {
		//TODO: add the import tax
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
