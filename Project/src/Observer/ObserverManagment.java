package Observer;

import java.util.HashSet;
import java.util.Set;

import Interfaces.IObserver;
import Products.Product;

public class ObserverManagment {
	private Set<IObserver> observes;
	private String msg;
	
	public ObserverManagment() {
		observes= new HashSet<>();
		this.msg = "A product has been orderd";
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

	// addListener
	public void attach(IObserver o) {
		observes.add(o);
	}
	
	public void sendProductSold(Product p) {
		if(p!=null)
			setMsg("The product " + p.getProductName() +" - has been orderd");
		myNotify();
	}

	public void detach(IObserver o) {
		observes.remove(o);
	}

	public void myNotify() {
		for (IObserver o : observes)
			o.update(this);
	}
}
