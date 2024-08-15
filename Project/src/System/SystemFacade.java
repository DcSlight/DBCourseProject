package System;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import Components.Contact;
import Components.Customer;
import Exception.StockException;
import Interfaces.ICommand;
import Interfaces.IShippingReceiver;
import Observer.ObserverManagment;
import Order.Order;
import Order.OrderController;
import Order.WebsiteOrder;
import Products.Product;
import Products.ProductSoldInStore;
import Products.ProductSoldThroughWebsite;
import Products.ProductSoldToWholesalers;
import Shipping.DHL;
import Shipping.FedEx;
import Shipping.ShippingCompany;
import Shipping.ShippingFactory;
import Shipping.ShippingInvoker;
import Utils.FormatsUtils;
import eNums.eProduct;
import eNums.eShipType;


public class SystemFacade {
	private static SystemFacade instance;
	private Set<Product> products;
	private ShippingInvoker shippingInvoker;
	private Set<ShippingCompany> companies;
	private ObserverManagment obs;
	private OrderController orderContorller;
	private Memento memento;
	
	public static final int IMPORT_TAX = 20;
	
	
	private SystemFacade() {
		this.products = new TreeSet<>();
		this.shippingInvoker = new ShippingInvoker();
		this.companies = new HashSet<>();
		this.orderContorller = new OrderController();
		initCompanies();
	
		obs = new ObserverManagment();
		initObservers();
		backUpSystem();
	}
	
	  // Generic method to get a random value from any enum
	  public static <T extends Enum<?>> T getRandomEnumValueFromSubset(T[] subset) {
	        Random random = new Random();
	        int randomIndex = random.nextInt(subset.length);
	        return subset[randomIndex];
	    }
	
	/**
	 * Ex: 4.1
	 * @apiNote  According To the assignment - hard coded 9 products and 27 Orders
	 */
	public void initSystems() {
		initProductsAndOrders();//
	}
	
	private void initObservers() {
		for(ShippingCompany company : companies) {
			obs.attach(company);
		}
	}
	
	public float getSystemTotalProfit() {
		double sum=0;
		for(Product product : products) {
			sum+=product.getTotalProfit();
		}
		return (float)sum;
	}
	
	/**
	 * Ex: 4.8
	 */
	public String getAllProducts() {
		int num = 1;
		StringBuffer st = new StringBuffer();
		st.append(FormatsUtils.ANSI_CYAN + "\n--------------------------------------\n");
		st.append("\t  All Products\n");
		st.append("--------------------------------------\n" + FormatsUtils.ANSI_RESET);
		for(Product product : products) {
			st.append(FormatsUtils.ANSI_YELLOW_BOLD + "Product " + num + ":\n" + FormatsUtils.ANSI_RESET);
			st.append(product.toString());
			num++;
		}
		st.append(FormatsUtils.ANSI_CYAN_BRIGHT + "\nTotal profit in system: " + getSystemTotalProfit()+"$\n" + FormatsUtils.ANSI_RESET);
		return st.toString();
	}
	
	public String getAllProductsByType(eProduct productType) {
		StringBuffer st = new StringBuffer();
		int num=1;
		for(Product product: products) {
			if(product.getType() == productType) {
				st.append(FormatsUtils.ANSI_YELLOW+"Product "+num+"\n" + FormatsUtils.ANSI_RESET);
				st.append(product.getDetails() +"\n");
				num++;
			}			
		}
		return st.toString();
	}
	
	public Product getProductBySerialAndType(String serial,eProduct productType) {
		for(Product product : products) {
			if(product.getType() == productType && product.getSerial().equals(serial)) {
				return product;
			}
		}
		return null;
	}

	
	//TODO: need DB
	public void makeOrder(Product product,Customer customer, int amount,eShipType type,String destCountry,String serial) throws StockException {
		Order order;
		if(product instanceof ProductSoldThroughWebsite) {
			IShippingReceiver receiver = shippingInvoker.calculateShippingFee(type, product);
			order = new WebsiteOrder(product,customer,amount,receiver.getCompany(),type,receiver.getPrice(),destCountry,serial);
			obs.sendProductSold(product);
			System.out.println(FormatsUtils.ANSI_YELLOW + "\n"+receiver.getCompany().getName()+" offers the cheapest shipping at $"
								+(float)receiver.getPrice() + "\n" + FormatsUtils.ANSI_RESET);
		}else {
			order = new Order(product,customer,amount,serial);
		}
		orderContorller.updateOrders(order, product);
	}
	
	
	/**
	 * Ex: 4.6
	 */
	public String undoOrder() {
		if(orderContorller.haveOrders()) {
			orderContorller.undoOrder();
			return FormatsUtils.ANSI_GREEN_BRIGHT + "Undo has been success!\n" + FormatsUtils.ANSI_RESET;
		}
		return FormatsUtils.ANSI_RED_BRIGHT + "There are no orders to do undo\n" + FormatsUtils.ANSI_RESET;
	}
	
	public Product getProductBySerial(String serial) {
		for(Product product : products) {
			if(product.getSerial().equals(serial)) {
				return product;
			}
		}
		return null;
	}
	
	public boolean isSerialProductExist(String Serial) {
		for (Product product : products) {
			if (product.getSerial().equals(Serial))
				return true;
		}
		return false;
	}
	
	public boolean isSerialOrderExist(String serial) {
		for (Product product : products) {
			if(product.isSerialOrderExist(serial)){
				return true;
			}
		}
		return false;
	}
	
	public String getProductOrders(Product product) {
		return product.getAllOrders();
	}
	
	
	public void addShippment(eShipType type,ICommand c) {
		shippingInvoker.addCommand(type,c);
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
	}
	
	public boolean removeProduct(Product product) {
		orderContorller.removeOrdersOfProducts(product);
		if(products.contains(product)) {
			this.products.remove(product);
			return true;
		}
		return false;
	}
	
	public Set<Product> getProducts(){
		return this.products;
	}
	
	public Set<ShippingCompany> getCompanies(){
		return this.companies;
	}
	
	
	public void addCompany(ShippingCompany c) {
		companies.add(c);
	}
	
	public static SystemFacade getInstance() {
		if (instance == null) {
            instance = new SystemFacade();
        }
        return instance;
	}
	
	
	/**
	 * Ex: 4.10
	 */
	public void backUpSystem() {
		memento = createMemento();
	}
	
	/**
	 * Ex: 4.10
	 */
	public void restoreSystem() {
		setMemento(this.memento);
	}
	
	private Memento createMemento() {
		return new Memento(products,companies,orderContorller);
	}
	
	private void setMemento(Memento m) {
		this.companies = m.companies;
		this.products = m.products;
		for(Product product : this.products) {
			product.setMemento();
		}
		m.orderContorller.setMemento();
		this.orderContorller=m.orderContorller;
	}
	
	public static class Memento{
		private Set<Product> products;
		private Set<ShippingCompany> companies;
		private OrderController orderContorller;
		
		private Memento(Set<Product> products,Set<ShippingCompany> companies,OrderController orderContorller) {
			this.companies = new HashSet<>(companies);
			this.products = new TreeSet<>();
			for(Product product : products) {
				product.createMemento();
				this.products.add(product);
			}
			orderContorller.createMemento();
			this.orderContorller = orderContorller;
		}
	}
	
	 public static List<String> generateRandomDates(int length) {
	        // Create an ArrayList to hold the formatted dates
	        List<String> randomDates = new ArrayList<>();

	        // Create a SimpleDateFormat object with the desired format
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	        // Get the current date
	        Calendar calendar = Calendar.getInstance();

	        // Create a Random object to generate random numbers
	        Random random = new Random();

	        for (int i = 0; i < length; i++) {
	            // Reset the calendar to today's date
	            calendar.setTime(new Date());

	            // Generate a random number of days between 0 and 30
	            int randomDays = random.nextInt(31);

	            // Generate a random number of hours, minutes, and seconds
	            int randomHours = random.nextInt(24);
	            int randomMinutes = random.nextInt(60);
	            int randomSeconds = random.nextInt(60);

	            // Add the random days, hours, minutes, and seconds to the current date
	            calendar.add(Calendar.DAY_OF_MONTH, randomDays);
	            calendar.set(Calendar.HOUR_OF_DAY, randomHours);
	            calendar.set(Calendar.MINUTE, randomMinutes);
	            calendar.set(Calendar.SECOND, randomSeconds);

	            // Format the date and add it to the list
	            randomDates.add(dateFormat.format(calendar.getTime()));
	        }

	        return randomDates;
	    }
	
	//TODO: DELETE
	private void initCompanies() {
		Contact contactDHL = new Contact("Yossi-DHL","0522801897");
		ShippingCompany dhl = new DHL(contactDHL,IMPORT_TAX);
		addCompany(dhl);
		ICommand dhlExpress = ShippingFactory.createShippingCommand(eShipType.eExpress, dhl);
		addShippment(eShipType.eExpress, dhlExpress);
		ICommand dhlStandard = ShippingFactory.createShippingCommand(eShipType.eStandard, dhl);
		addShippment(eShipType.eStandard,dhlStandard);
		
		Contact contactFedEx = new Contact("Roy-FedEx","0522206896");
		ShippingCompany fedEx = new FedEx(contactFedEx,IMPORT_TAX);
		addCompany(fedEx);
		ICommand fedExExpress = ShippingFactory.createShippingCommand(eShipType.eExpress, fedEx);
		addShippment(eShipType.eExpress,fedExExpress);
		ICommand fedExStandard = ShippingFactory.createShippingCommand(eShipType.eStandard, fedEx);
		addShippment(eShipType.eStandard,fedExStandard);
	}
	
	private void initProductsAndOrders() {
		Customer c1 = new Customer("Avi","0506007070", "Jerusalem" , 1);
		Customer c2 = new Customer("Yoram","0526038971", "Tel Aviv" , 1);
		Customer c3 = new Customer("Shlomi","0526238771" , "Netanya" , 1);
		Product p1 = new ProductSoldThroughWebsite("AAB12", "Iphone 15 protector", 7.5,87.58 , 400, 0.25);
		Product p2 = new ProductSoldThroughWebsite("199BA", "TV", 1200,2750 , 10, 13.6);
		Product p3 = new ProductSoldThroughWebsite("78FHC", "JBL", 210.5,453.2 , 62, 1.23);
	
		Product p4 = new ProductSoldInStore("AHDHB2", "Battery", 12.5, 25, 120, 0.2);
		Product p5 = new ProductSoldInStore("AFCHP7", "Coat", 45.6, 350, 62, 1.7);
		Product p6 = new ProductSoldInStore("PDKSU2", "T-Shirt", 12.5,98, 158, 1.1);
		
		Product p7 = new ProductSoldToWholesalers("P3MCJU", "Coca Cola", 0.5, 10, 2500, 0.6);
		Product p8 = new ProductSoldToWholesalers("MXJQXT", "Mayo", 15, 17, 1230, 5);
		Product p9 = new ProductSoldToWholesalers("MPXL2K", "Toilet Paper", 5, 45, 5800, 3);
		try {
		addProduct(p1);
		makeOrder(p1,c1,5,eShipType.eExpress,"Israel","A1");
		makeOrder(p1,c2,10,eShipType.eStandard,"USA","A2");
		makeOrder(p1,c3,7,eShipType.eExpress,"Spain","A3");
		addProduct(p2);
		makeOrder(p2,c1,1,eShipType.eExpress,"Germany","B1");
		makeOrder(p2,c2,2,eShipType.eStandard,"England","B2");
		makeOrder(p2,c3,1,eShipType.eExpress,"Netherlands","B3");
		addProduct(p3);
		makeOrder(p3,c1,12,eShipType.eStandard,"Egypt","C1");
		makeOrder(p3,c2,7,eShipType.eStandard,"Brazil","C2");
		makeOrder(p3,c3,8,eShipType.eStandard,"Argentina","C3");
		
		addProduct(p4);
		makeOrder(p4,c1,2,eShipType.eNone,null,"D1");
		makeOrder(p4,c2,3,eShipType.eNone,null,"D2");
		makeOrder(p4,c3,4,eShipType.eNone,null,"D3");
		addProduct(p5);
		makeOrder(p5,c1,2,eShipType.eNone,null,"E1");
		makeOrder(p5,c2,3,eShipType.eNone,null,"E2");
		makeOrder(p5,c3,1,eShipType.eNone,null,"E3");
		addProduct(p6);
		makeOrder(p6,c1,12,eShipType.eNone,null,"F1");
		makeOrder(p6,c2,13,eShipType.eNone,null,"F2");
		makeOrder(p6,c3,11,eShipType.eNone,null,"F3");
		
		addProduct(p7);
		makeOrder(p7,c1,120,eShipType.eNone,null,"G1");
		makeOrder(p7,c2,130,eShipType.eNone,null,"G2");
		makeOrder(p7,c3,110,eShipType.eNone,null,"G3");
		addProduct(p8);
		makeOrder(p8,c1,220,eShipType.eNone,null,"H1");
		makeOrder(p8,c2,330,eShipType.eNone,null,"H2");
		makeOrder(p8,c3,410,eShipType.eNone,null,"H3");
		addProduct(p9);	
		makeOrder(p9,c1,750,eShipType.eNone,null,"I1");
		makeOrder(p9,c2,450,eShipType.eNone,null,"I2");
		makeOrder(p9,c3,580,eShipType.eNone,null,"I3");
		}catch(StockException e) {
			//nothing - it is built in 
		}
	}
}
