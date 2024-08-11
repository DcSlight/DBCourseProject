package Components;

import java.util.Objects;

public class Customer{
	private String customerName;
	private String mobile;
	private String address;
	private int countryID;
	
	public Customer(String customerName, String mobile, String address, int countryID) {
		this.customerName = customerName;
		this.mobile = mobile;
		this.address = address;
		this.countryID = countryID;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public Integer getCountryID() {
		return this.countryID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerName, mobile);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return customerName.equals(other.customerName)
			   && mobile.equals(other.mobile);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	//TODO: fix the print
	public String toString() {
		return "Customer Name: " + customerName + "\tmobile: " + mobile;
	}
	
}
