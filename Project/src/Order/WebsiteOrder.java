package Order;

import java.sql.SQLException;
import java.util.Objects;

import Components.Customer;
import DB.DatabaseConnection;
import DB.Entities.WebsiteOrderTracksView;
import Products.Product;
import Shipping.ShippingCompany;
import eNums.eShipType;
import eNums.eStatus;

public class WebsiteOrder extends Order{
	
	private eStatus statusCode;
	//private double shippingPrice; will be calc in the controller

	public WebsiteOrder(Product product, Customer customer, 
			int amount,
			int destCountryID,String serial,int statusCode,double profit) {
		super(product,customer,amount,serial,profit);
		this.statusCode = statusCode;
	}
	
	public int getCountryID() {
		return this.getCustomer().getCountryID();
	}
	

	public ShippingCompany getCompany() {
		//TODO: need to fix
		return company;
	}

	public void setCompany(ShippingCompany company) {
		//TODO: need to fix
		this.company = company;
	}

	/*
	 * the shipping price is the average of VAT in all the country at need to visit
	 * */
	public double getShippingPrice() throws SQLException, Exception {
		WebsiteOrderTracksView w = new WebsiteOrderTracksView(DatabaseConnection.getConnection());
		double shippingPrice = w.getAverageVATRate(this.getSerial());
		return shippingPrice;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(company,  statusCode);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebsiteOrder other = (WebsiteOrder) obj;
		return Objects.equals(company, other.company)
				&& Double.doubleToLongBits(shippingPrice) == Double.doubleToLongBits(other.shippingPrice)
				&& statusCode == other.statusCode;
	}

	public String toString() {
		//TODO: need to fix
		StringBuffer st = new StringBuffer();
		st.append(super.toString());
		//st.append("Dest Country: " + destCountry + "\n");
		//st.append(company + "\ntype: " + type.name() + "\nshipping price: " + shippingPrice );
		return st.toString();
	}
	
	
	
}
