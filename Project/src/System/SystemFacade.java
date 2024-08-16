package System;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import Components.Contact;
import Components.Customer;
import DB.DatabaseConnection;
import DB.Entities.OrderTable;
import DB.Entities.ProductTable;
import DB.Entities.ShippingCompanyTable;
import DB.Entities.WebsiteOrderTracksView;
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
import Shipping.Track;
import Utils.FormatsUtils;
import eNums.eProduct;
import eNums.eShipType;
import eNums.eStatus;


public class SystemFacade {
	private static SystemFacade instance;
	private Set<Product> products;//TODO : remove
	private ShippingInvoker shippingInvoker;
	private ObserverManagment obs;
	private OrderController orderContorller;
	private Connection conn;
	
	public static final int IMPORT_TAX = 20;
	
	
	private SystemFacade() throws SQLException, Exception {
		this.conn = DatabaseConnection.getConnection();
		this.products = new TreeSet<>();
		this.shippingInvoker = new ShippingInvoker();
		this.orderContorller = new OrderController();
		initCompanies();
	
		obs = new ObserverManagment();
		initObservers();
	}
	
	public int removeOrder(String serialOrder) throws Exception {
		WebsiteOrderTracksView wv = new WebsiteOrderTracksView(conn);
		wv.deleteOrderWebsite(serialOrder);
		OrderTable orderTable = new OrderTable(conn);
		return orderTable.deleteOrder(serialOrder);
	}
	
	 
	// Generic method to get a random value from any enum
	public static <T extends Enum<?>> T getRandomEnumValueFromSubset(T[] subset) {
        Random random = new Random();
        int randomIndex = random.nextInt(subset.length);
        return subset[randomIndex];
    }
	
	private void initObservers() throws Exception {
		ShippingCompanyTable companyTable = new ShippingCompanyTable(this.conn);
		Set<ShippingCompany> companies=companyTable.findAllCompanies();
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
	
	public String getAllProductsByType(eProduct productType) throws SQLException {
		ProductTable pt = new ProductTable(conn);
		Set<Product> products = pt.findAllProductByType(productType);
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
	
	public Product getProductBySerialAndType(String serial) throws Exception {
		ProductTable productTable = new ProductTable(this.conn);
		return productTable.findProductBySerial(serial);
	}

	
	public void makeOrder(Product product,Customer customer, int amount,eShipType type,String destCountry,String serial) throws StockException ,SQLException, Exception{
		Order order;
		OrderTable ot = new OrderTable(this.conn);
		if(product instanceof ProductSoldThroughWebsite) {
			 IShippingReceiver receiver = shippingInvoker.calculateShippingFee(type, product);
	         WebsiteOrder websiteOrder = new WebsiteOrder(product,customer,amount,serial,eStatus.eOnTheWay,
	        		  receiver.getCompany().getCompanyID(),type,receiver.getPrice());
	         ot.createOrder(websiteOrder);//create order 
	         websiteOrder.createWebsiteOrder(); //create the shipments related to the order
	         obs.sendProductSold(product);//Observers
			 System.out.println(FormatsUtils.ANSI_YELLOW + "\n"+receiver.getCompany().getName()+" offers the cheapest shipping at $"
									+(float)receiver.getPrice() + "\n" + FormatsUtils.ANSI_RESET);
			 order=websiteOrder;
		}else {
			 order = new Order(product,customer,amount,serial);
	         ot.createOrder(order);//create order 
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
	
	public Product getProductBySerial(String serial) throws Exception {
		ProductTable pt = new ProductTable(conn);
		return pt.findProductBySerial(serial);
	}
	
	public boolean isSerialProductExist(String Serial) {
		for (Product product : products) {
			if (product.getSerial().equals(Serial))
				return true;
		}
		return false;
	}
	
	public boolean isSerialOrderExist(String serial) throws Exception {
		OrderTable ot = new OrderTable(this.conn);
		Order o = ot.findOrderBySerial(serial);
		if(o!= null)
			return true;
		return false;
	}
	
	public String getProductOrders(Product product) {
		return product.getAllOrders();
	}
	
	
	public void addShippment(eShipType type,ICommand c) {
		shippingInvoker.addCommand(type,c);
	}
	
	public void addProductToDB(Product product) throws Exception {
		ProductTable pt = new ProductTable(this.conn);
		pt.createProduct(product);
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
	
	public static SystemFacade getInstance() throws SQLException, Exception {
		if (instance == null) {
            instance = new SystemFacade();
        }
        return instance;
	}
	
	public static List<String> generateRandomDates(int numberOfDates) {
        List<LocalDateTime> randomDates = new ArrayList<>();
        Random random = new Random();
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Calculate the date one month from now
        LocalDateTime oneMonthLater = now.plusMonths(1);

        // Generate random dates and times between now and one month from now
        for (int i = 0; i < numberOfDates; i++) {
            // Generate a random number of days, hours, minutes, and seconds to add
            int randomDays = random.nextInt(31);  // Up to 31 days (1 month)
            int randomHours = random.nextInt(24);  // Up to 24 hours
            int randomMinutes = random.nextInt(60);  // Up to 60 minutes
            int randomSeconds = random.nextInt(60);  // Up to 60 seconds
            
            // Generate the random date and time
            LocalDateTime randomDate = now
                .plusDays(randomDays)
                .plusHours(randomHours)
                .plusMinutes(randomMinutes)
                .plusSeconds(randomSeconds);
            
            // Ensure the date is within the next month
            if (randomDate.isAfter(oneMonthLater)) {
                randomDate = oneMonthLater.minusSeconds(random.nextInt(60));
            }
            
            randomDates.add(randomDate);
        }
        // Sort the list of dates and times
        Collections.sort(randomDates);
        // Define the date format (example: "yyyy-MM-dd HH:mm:ss")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Convert the sorted dates and times to the desired format
        List<String> formattedDates = new ArrayList<>();
        for (LocalDateTime date : randomDates) {
            formattedDates.add(date.format(formatter));
        }
        return formattedDates;
    }
	
	private void initCompanies() throws Exception {
		ShippingCompanyTable companyTable = new ShippingCompanyTable(this.conn);
		ShippingCompany fedEx = companyTable.findCompany("FedEx");
		ShippingCompany dhl = companyTable.findCompany("DHL");
		ICommand dhlExpress = ShippingFactory.createShippingCommand(eShipType.eExpress, dhl);
		addShippment(eShipType.eExpress, dhlExpress);
		ICommand dhlStandard = ShippingFactory.createShippingCommand(eShipType.eStandard, dhl);
		addShippment(eShipType.eStandard,dhlStandard);
		ICommand fedExExpress = ShippingFactory.createShippingCommand(eShipType.eExpress, fedEx);
		addShippment(eShipType.eExpress,fedExExpress);
		ICommand fedExStandard = ShippingFactory.createShippingCommand(eShipType.eStandard, fedEx);
		addShippment(eShipType.eStandard,fedExStandard);
	}
	
}
