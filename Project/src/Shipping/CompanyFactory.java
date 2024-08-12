package Shipping;

public class CompanyFactory {
	public static ShippingCompany createCompany(int id, String name) {
		switch(name) {
		case "DHL":
			return new DHL(id);
		case "FedEx":
			return new FedEx(id);
		default:
			throw new IllegalArgumentException();
		}
	}
}
