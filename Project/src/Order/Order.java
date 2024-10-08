package Order;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import Components.Country;
import Components.Customer;
import DB.DatabaseConnection;
import DB.Entities.CountryTable;
import DB.Entities.OrderTable;
import Interfaces.IInvoice;
import Invoice.InvoiceAdapterFactory;
import Products.Product;
import Products.ProductSoldInStore;
import Products.ProductSoldToWholesalers;
import eNums.eInvoice;

public class Order{
	private String serial;
	private int amount;
	private Product product;
	private Customer customer;
	private Set<IInvoice> allInvoice;
	
	public Order(Product product, Customer customer,int amount,String serial) {
		this.product = product;
		this.customer = customer;
		this.amount = amount;
		this.allInvoice = new HashSet<IInvoice>();
		this.serial = serial;
		initInvoice();	
	}
	
	private void initInvoice() {
		IInvoice invoiceAccountant = InvoiceAdapterFactory.createAdapterInvoice(eInvoice.eAccountantInvoice,
				customer,product ,amount);
		
		IInvoice invoiceCustomer = InvoiceAdapterFactory.createAdapterInvoice(eInvoice.eCustomerInvoice,
				customer, product,amount);
		
		if(product instanceof ProductSoldInStore) {
			allInvoice.add(invoiceCustomer);
			allInvoice.add(invoiceAccountant);
		}
		else if(product instanceof ProductSoldToWholesalers) {
			allInvoice.add(invoiceAccountant);
		}		
	}
	
	public int getAmount() {
		return amount;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<IInvoice> getInvoice() {
		return allInvoice;
	}
	
	public String getSerial() {
		return this.serial;
	}


	@Override
	public int hashCode() {
		return Objects.hash(serial);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return this.serial.equals(other.serial);
	}
	
	public double getProfit() {
		try {
			OrderTable ot = new OrderTable(DatabaseConnection.getConnection());
			return ot.getProfitBySerial(this.serial);
		}catch(Exception e) {
			return 0;
		}
		
	}

	@Override
	public String toString() {
		StringBuffer st = new StringBuffer();
		st.append("Order serial:" + serial + "\n");
		st.append(customer + "\n");
		st.append("Address: " + customer.getAddress()+ "\n");
		try {
		CountryTable ct = new CountryTable(DatabaseConnection.getConnection());
		Country country = ct.findCountryByID(customer.getCountryID());
		st.append("Country: " + country.getCountry()+ "\n");
		}catch(Exception e) {
			st.append("Country: Error getting country\n");
		}
		st.append("Quantity: " + amount + "\n");
		for(IInvoice invoice : allInvoice) {
			st.append(invoice.showInvoice());
		}
		return st.toString();
	}
	
}
