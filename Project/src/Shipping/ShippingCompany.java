package Shipping;


import java.util.Objects;

import Components.Contact;
import Interfaces.IObserver;

public abstract class ShippingCompany implements IObserver{
	protected int id;
	private static int counter=0;
	protected Contact contact;
	protected int importTax;
	
	public ShippingCompany(Contact contact,int importTax) {
		this.contact=contact;
		this.importTax=importTax;
		counter++;
		this.id=counter;
	}	
	
	public abstract String getName();
	
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public int getImportTax() {
		return importTax;
	}

	public void setImportTax(int importTax) {
		this.importTax = importTax;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShippingCompany other = (ShippingCompany) obj;
		return contact.equals(other.contact) && importTax == other.importTax;
	}

	@Override
	public String toString() {
		return "Shipping Company: " + getName() + "\n" + contact + "\nImport tax: " + importTax;
	}
	
}
