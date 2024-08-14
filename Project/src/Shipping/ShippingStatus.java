package Shipping;

import eNums.eStatus;

public class ShippingStatus {
	private int statusCode;
	private int companyId;
	private String orderID;
	private eStatus shippingStatus;
	
	public ShippingStatus(int statusCode, int companyId, String orderID, eStatus shippingStatus) {
		this.statusCode = statusCode;
		this.companyId = companyId;
		this.orderID = orderID;
		this.shippingStatus = shippingStatus;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public eStatus getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(eStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	@Override
	public String toString() {
		return "ShippingStatus [statusCode=" + statusCode + ", companyId=" + companyId + ", orderID=" + orderID
				+ ", shippingStatus=" + shippingStatus + "]";
	}
	
	
	
	
	
}
