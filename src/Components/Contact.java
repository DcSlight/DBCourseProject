package Components;

import java.util.Objects;

public class Contact {
	private String name;
	private String whatsApp;
	
	public Contact(String name,String whatsApp){
		this.name=name;
		this.whatsApp = whatsApp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWhatsApp() {
		return whatsApp;
	}

	public void setWhatsApp(String whatsApp) {
		this.whatsApp = whatsApp;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, whatsApp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		return name.equals( other.name) && whatsApp.equals(whatsApp);
	}

	@Override
	public String toString() {
		return "Contact [name=" + name + ", whatsApp=" + whatsApp + "]";
	}

	
	
	
}
