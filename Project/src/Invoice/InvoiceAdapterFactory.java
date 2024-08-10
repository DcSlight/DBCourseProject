package Invoice;

import Components.Customer;
import Interfaces.IInvoice;
import Products.Product;
import Products.ProductSoldInStore;
import eNums.eInvoice;

public class InvoiceAdapterFactory {
	
	public static final char SHEKEL='₪';
	public static final char DOLLAR='$';
	
	public static IInvoice createAdapterInvoice(eInvoice type,Customer customer,Product product,int amount) {
		char currency = 0;
		if(product instanceof ProductSoldInStore ) {
			currency=SHEKEL;
		}else {
			currency=DOLLAR;
		}
		
		switch(type) {
		case eAccountantInvoice:
			return new AccountantInvoiceAdapter(new AccountantInvoice(customer,product.getProductName(),product.getSellingPrice(),product.getCostPrice(),amount,currency));
		case eCustomerInvoice:
			return new CustomerInvoiceAdapter(new CustomerInvoice(customer,product.getProductName(),product.getSellingPrice(),amount,currency));
		case eNone: //sold in Website
			return null;
		default:
			throw new IllegalArgumentException();
		}
	}
}
