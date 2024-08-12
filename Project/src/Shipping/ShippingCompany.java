package Shipping;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import Components.Contact;
import Interfaces.IObserver;

public abstract class ShippingCompany implements IObserver{
	protected int companyId;
	protected Set<Contact> contactList;
	
	public ShippingCompany(int companyId) {
		this.companyId=companyId;
		this.contactList = new HashSet<>();
	}	
	
	public int getCompanyID() {
		return this.companyId;
	}
	
	public abstract String getName();
	
	public Set<Contact> getContactList() {
		return contactList;
	}

	public void addContact(Contact contact) {
		this.contactList.add(contact);
		//TODO: ADD TO DB
	}
	
	public void deleteContact(Contact contact) {
		this.contactList.remove(contact);
		//TODO: REMOVE FROM DB
	}

	@Override
	public int hashCode() {
		return Objects.hash(companyId);
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
		return companyId == other.companyId;
	}

	@Override
	public String toString() {
		//TODO: modify
		return "Shipping Company: " + getName() + "\n" + contactList.toString() ;
	}
	
}
