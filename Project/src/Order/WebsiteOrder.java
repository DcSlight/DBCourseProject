package Order;

import java.util.Objects;

import Components.Customer;
import Products.Product;
import Shipping.ShippingCompany;
import eNums.eShipType;

public class WebsiteOrder extends Order{
	
	private ShippingCompany company;
	private eShipType type;
	private double shippingPrice;
	private String destCountry;

	public WebsiteOrder(Product product, Customer customer, int amount,ShippingCompany company, eShipType type,double shippingPrice,String destCountry,String serial) {
		super(product, customer, amount,serial);
		this.company = company;
		this.type = type;
		this.shippingPrice = shippingPrice;
		this.destCountry= destCountry;
	}
	
	public String getCountry() {
		return destCountry;
	}
	
	public void setCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public ShippingCompany getCompany() {
		return company;
	}

	public void setCompany(ShippingCompany company) {
		this.company = company;
	}

	public eShipType getType() {
		return type;
	}

	public void setType(eShipType type) {
		this.type = type;
	}

	public double getShippingPrice() {
		return shippingPrice;
	}

	public void setShippingPrice(double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(company, destCountry, shippingPrice, type);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof WebsiteOrder)) {
			return false;
		}
		return true;
	}

	public String toString() {
		StringBuffer st = new StringBuffer();
		st.append(super.toString());
		st.append("Dest Country: " + destCountry + "\n");
		st.append(company + "\ntype: " + type.name() + "\nshipping price: " + shippingPrice );
		return st.toString();
	}
	
	
	
}
