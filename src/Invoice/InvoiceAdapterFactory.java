package Invoice;

import Components.Customer;
import Interfaces.IInvoice;
import eNums.eInvoice;

public class InvoiceAdapterFactory {
	public static IInvoice createAdapterInvoice(eInvoice type,Customer customer,String productName, double sellingPrice,double costPrice,int amount) {
		switch(type) {
		case eAccountantInvoice:
			return new AccountantInvoiceAdapter(new AccountantInvoice(customer,productName,sellingPrice,costPrice,amount));
		case eCustomerInvoice:
			return new CustomerInvoiceAdapter(new CustomerInvoice(customer,productName,sellingPrice,amount));
		case eNone: //sold in Website
			return null;//TODO: check
		default:
			throw new IllegalArgumentException();
		}
	}
}
