package System;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;
import Components.Customer;
import DB.DatabaseConnection;
import DB.Entities.CountryTable;
import DB.Entities.CustomerTable;
import DB.Entities.ProductTable;
import Exception.StockException;
import Products.Product;
import Products.ProductFactory;
import Products.ProductSoldThroughWebsite;
import Utils.FormatsUtils;
import eNums.eProduct;
import eNums.eShipType;

public class Program {
	
	public static final String INIT_SYSTEM = "0";
	public static final String ADD_PRODUCT = "1";
	public static final String REMOVE_PRODUCT = "2";
	public static final String EDIT_PRODUCT_STOCK = "3";
	public static final String ADD_ORDER = "4";
	public static final String REMOVE_ORDER = "5";
	public static final String PRINT_PRODUCT_DETAILS = "6";
	public static final String PRINT_ALL_PRODUCTS = "7";
	public static final String PRINT_PRODUCT_ORDERS = "8";
	public static final String ORDER_ROUTE = "9";
	public static final String EXIT_1 = "E";
	public static final String EXIT_2 = "e";
	public static final Boolean POSITIVE = true;

	public static void main(String[] args) throws Exception  {		
		
		Scanner sc = new Scanner(System.in);
		try {
			SystemFacade systemFacade = SystemFacade.getInstance();
			boolean flag = true;
			String option;
			System.out.println(FormatsUtils.ANSI_CYAN_BOLD + "--------------------------------------");
			System.out.println("\tWelcome to the System!");
			System.out.println("--------------------------------------" + FormatsUtils.ANSI_RESET);
			do {
				System.out.println("1 - To add a product");
				System.out.println("2 - To remove a product");
				System.out.println("3 - To edit a product stock");
				System.out.println("4 - To add an order to product");
				System.out.println("5 - To remove an order");
				System.out.println("6 - Print a product details");
				System.out.println("7 - Print system profit and all products");
				System.out.println("8 - Print product orders");
				System.out.println("9 - Watch order route");
				System.out.println("E/e - To Exit");
				option = sc.nextLine();
				switch(option) {
				case ADD_PRODUCT:
					addProductMenu(sc,systemFacade);
					break;
				case REMOVE_PRODUCT:
					removeProduct(sc,systemFacade);
					break;
				case EDIT_PRODUCT_STOCK:
					editProductStock(sc,systemFacade);
					break;
				case ADD_ORDER:
					addOrderMenu(sc,systemFacade);
					break;
				case REMOVE_ORDER:
					removeOrderMenu(sc,systemFacade);
					break;
				case PRINT_PRODUCT_DETAILS:
					printSpecificProduct(sc,systemFacade);
					break;
				case PRINT_ALL_PRODUCTS:
					System.out.println(systemFacade.getAllProducts());
					break;
				case PRINT_PRODUCT_ORDERS:
					printProductOrders(sc,systemFacade);
					break;
				case ORDER_ROUTE:
					orderRouteMenu(sc,systemFacade);
					break;
				case EXIT_1:
				case EXIT_2:
					System.out.println(FormatsUtils.ANSI_CYAN_BRIGHT + "Have a good day" + FormatsUtils.ANSI_RESET);
					flag=false;
					break;
				default:
					FormatsUtils.failureMsg("Invalid Input\n");
					break;
				}
				
			}while(flag);
		}
		catch(Exception e) {
			FormatsUtils.failureMsg(e.getMessage());
		}
	}
	
	public static void orderRouteMenu(Scanner sc,SystemFacade sf) {
		String orderID = getSerialOrder(sc, sf);
		String res = sf.findAllTracksByOrderID(orderID);
		System.out.println(res);
	}
	
	public static void removeOrderMenu(Scanner sc,SystemFacade sf) {
		String serialOrder;
		System.out.println("Type order ID");
		serialOrder = sc.nextLine();
		try {
		int rows=sf.removeOrder(serialOrder);
		if(rows > 0) {
			FormatsUtils.successMsg("Order has been deleted!\n");
		}else {
			FormatsUtils.failureMsg("No order match to this serial\n");
		}}
		catch(Exception e) {
			FormatsUtils.failureMsg(e.getMessage()+"\n");
		}
	}
	
	
	/**
	 * add OrderMenu
	 * Example - customer 3, Product - 78FHC Website
	 * @param sc
	 * @param systemFacade
	 */
	public static void addOrderMenu(Scanner sc, SystemFacade systemFacade) {
		Product product;
		Customer customer;
		int amount;
		eShipType type;
		eProduct productType;
		String destCountry =null;
		String orderSerial;
		FormatsUtils.printTitle("\t  Make an order", FormatsUtils.ANSI_CYAN);
		int option=pickOrderByProductType(sc);
		switch(option) {
		case 1:
			productType=eProduct.eProductWholesalers;
			break;
		case 2:
			productType=eProduct.eProductStore;
			break;
		case 3: 
			productType=eProduct.eProductWebsite;
			break;
		default:
			FormatsUtils.failureMsg("Invalid Input\n");
			return;
		}
		try {
		System.out.println(systemFacade.getAllProductsByType(productType));
		}catch(Exception e) {
			FormatsUtils.failureMsg(e.getMessage() + "\n");
			return ;
		}
		product=getProductBySerialAndType(sc,systemFacade,productType);
		if(product == null){
			return;
		}
		orderSerial = getSerialOrder(sc,systemFacade);
		if(orderSerial == null){
			FormatsUtils.failureMsg("Order serial is already exist in the system!\n");
			return;
		}
		customer = getCustomerDetailsMenu(sc);
		if(customer == null)
			return;
		if(product instanceof ProductSoldThroughWebsite) {
			type = getShipTypeMenu(sc);
			sc.nextLine();//CLEAN BUFFER
		}
		else
			type = eShipType.eNone;
		amount = (int) getValidNumber(sc,"How many of this product so you want to order?\n", POSITIVE, Integer.class);
		try {
			systemFacade.makeOrder(product, customer, amount, type,destCountry,orderSerial);
			FormatsUtils.successMsg("Order added successfully!\n");
		}catch(StockException e ) {
			FormatsUtils.failureMsg(e.getMessage() + "\n");
		}catch(Exception e) {
			FormatsUtils.failureMsg(e.getMessage()+ "\n");
		}
	}
	
	public static String getSerialOrder(Scanner sc, SystemFacade systemFacade) {
		String serial;
		System.out.println("Please enter order serial");
		serial = sc.nextLine();
		try {
			if(!systemFacade.isSerialOrderExist(serial))
				return null;
			return serial;
		}catch(Exception e) {
			FormatsUtils.failureMsg("Error!\n" + e.getMessage());
			return null;
		}
	}
	
	
	/**
	 * Ex: 4.7
	 * @see   InputExmaple: AAB12
	 * @param sc
	 * @param systemFacade
	 */
	public static void printSpecificProduct(Scanner sc, SystemFacade systemFacade) {
		Product product;
		FormatsUtils.printTitle("\t  Product details:", FormatsUtils.ANSI_CYAN);
		product=getProductBySerial(sc,systemFacade);
		if(product == null){
			return;
		}
		System.out.println(product);
		
	}
	
	/*
	 * @see   InputExmaple: AAB12
	 * @param sc
	 * @param systemFacade
	 */
	public static void removeProduct(Scanner sc, SystemFacade systemFacade)
	{
		String serial;
		try {
			System.out.println("Please Enter a product serial");
			serial = sc.nextLine();
			ProductTable pt = new ProductTable(DatabaseConnection.getConnection());
			int rows =pt.deleteProduct(serial);
			if(rows > 0)
				FormatsUtils.successMsg("The product " + serial+ " was removed successfully!" );
			else
				FormatsUtils.failureMsg("Product is not exist\n");
		}catch(Exception e) {
			FormatsUtils.failureMsg("There was an error related to the DB\n");
		}
	}
	
	
	/**
	 * Ex: 4.4
	 * @see   InputExmaple: AAB12 20
	 * @param sc
	 * @param systemFacade
	 */
	public static void editProductStock(Scanner sc, SystemFacade systemFacade)
	{
		String serial;
		int stock;
		try {
			ProductTable pt = new  ProductTable(DatabaseConnection.getConnection());
			System.out.println("Please Enter a product serial");
			serial = sc.nextLine();
			stock = (int) getValidNumber(sc, "Enter a stock number for the product\n", POSITIVE, Integer.class);
			int rowAffected =pt.updateProductStockBySerial(serial, stock);
			if(rowAffected> 0)
				FormatsUtils.successMsg("Product stock was updated!\n");
			else
				FormatsUtils.failureMsg("No product was found with this serial");
		}catch(Exception e) {
			FormatsUtils.failureMsg("Product update failed");
		}
	}
	
	public static Product getProductBySerial(Scanner sc,SystemFacade systemFacade) {
		Product product;
		String serial;
		System.out.println("Please Enter a product serial");
		serial = sc.nextLine();
		try {
		product=systemFacade.getProductBySerial(serial);
		if(product == null) {
			FormatsUtils.failureMsg("Product was not found!\n");
			return null;
		}
		return product;
		}catch(Exception e) {
			FormatsUtils.failureMsg(e.getMessage()+"\n");
			return null;
		}
	}
	
	public static Product getProductBySerialAndType(Scanner sc,SystemFacade systemFacade,eProduct type) {
		Product product;
		String serial;
		System.out.println("Please Enter a product serial");
		serial = sc.nextLine();
		try {
			product=systemFacade.getProductBySerialAndType(serial);
			if(product == null) {
				FormatsUtils.failureMsg("Product was not found!\n");
				return null;
			}
			return product;
		}catch(Exception e) {
			FormatsUtils.failureMsg("Error!\n" + e.getMessage());
			return null;
		}
	}
	
	
	public static <T extends Number> Number getValidNumber(Scanner scanner, String prompt, boolean needToBePositive, Class<T> type) {
		Number tmp = null;
		while (true) {
            System.out.print(prompt);
            try {
            	if (type == Integer.class) {
            		tmp = scanner.nextInt();
            	}
            	else if (type == Double.class) {
            		tmp = scanner.nextDouble();
            	}                
                if(needToBePositive) {
                	if(tmp.doubleValue() > 0) {
                		scanner.nextLine();//clean buffer
                		return tmp;
                	}
                	throw new InputMismatchException();
                }
                scanner.nextLine();//clean buffer
                return tmp;	
            } catch (InputMismatchException e) {
            	FormatsUtils.failureMsg("Invalid input! Please enter a valid integer.\n");
                scanner.nextLine();//clean buffer
            }
        }
    }

	public static Customer getCustomerDetailsMenu( Scanner sc) {
		String opt;
		System.out.println("Are you a new customer? Y/N");
		opt = sc.nextLine();
		int id;
		try {
			Connection conn = DatabaseConnection.getConnection();
			if(opt.equals("N")) {
				CustomerTable customerTable = new CustomerTable(conn);
				System.out.println("Enter customer id");
				id = sc.nextInt();
				sc.nextLine();//clean buffer
				Customer c = customerTable.findCustomerByID(id);
				FormatsUtils.successMsg("Welcome back " + c.getCustomerName() + "!");
				return c;
			}
			else if(opt.equals("Y"))
			{
				CountryTable ct = new CountryTable(conn);
				Customer c;
				String name,mobile,address;
				Integer countryID;
				System.out.println("Enter Customer name");
				name= sc.nextLine();
				System.out.println("Enter phone number");
				mobile = sc.nextLine();
				System.out.println("------------------");
				ct.printAllCountries();
				System.out.println("Type Country ID");
				countryID = sc.nextInt();
				sc.nextLine();//clean Buffer
				System.out.println("Enter address");
				address = sc.nextLine();
				CustomerTable customerTable = new CustomerTable(conn);
				customerTable.createCustomer(name,mobile,address,countryID);
				int customerId =customerTable.getCustomerID(name,mobile);
				c = new Customer(name,mobile,address,countryID,customerId);
				return c;
			}
			else {
				FormatsUtils.failureMsg("wrong value\n");
				return null;
			}
		}catch(Exception e) {
			FormatsUtils.failureMsg(e.getMessage() + "\n");
			return null;
		}
	}
	
	/**
	 * Ex: 4.9
	 * @see   InputExmaple: AAB12
	 * @param sc
	 * @param systemFacade
	 */
	public static void printProductOrders(Scanner sc, SystemFacade systemFacade) {
		Product product = null;
		FormatsUtils.printTitle("\t The Product Orders", FormatsUtils.ANSI_CYAN);
		product = getProductBySerial(sc, systemFacade);
		if(product !=null)
			try {
				System.out.println(systemFacade.getProductOrders(product) + "\n");
			} catch (Exception e) {
				FormatsUtils.failureMsg(e.getMessage()+"\n");
			}
	}
	
	/**
	 * Ex: 4.2
	 * @see   InputExmaple: 1 PCK12  Tami4 60 80 5 20
	 * @param sc
	 * @param systemFacade
	 */
	public static void addProductMenu(Scanner sc, SystemFacade systemFacade) {
		Product product = null;
		int option;
		String serial,productName;
		double costPrice, sellingPrice,weight;
		int stock;
		FormatsUtils.printTitle("\t  Add a Product", FormatsUtils.ANSI_CYAN);
		System.out.println();
		option=pickProductByType(sc);
		if(option > 3) {
			FormatsUtils.failureMsg("Invalid Input\n");
			return;
		}
		System.out.println("Enter product serial");
		serial=sc.nextLine();
		if (systemFacade.isSerialProductExist(serial)) {
			FormatsUtils.failureMsg("Serial product is already exist!\n");
			return;
		}
		System.out.println("Enter product name");
		productName=sc.nextLine();
		costPrice = (double)getValidNumber(sc,"Enter cost price\n",POSITIVE,Double.class);
		sellingPrice = (double)getValidNumber(sc,"Enter selling price\n",POSITIVE,Double.class);
		weight = (double)getValidNumber(sc,"Enter weight\n",POSITIVE,Double.class);
		stock = (int)getValidNumber(sc,"Enter stock\n",POSITIVE,Integer.class);
		switch(option) {
		case 1:
			product = ProductFactory.createProduct(eProduct.eProductWholesalers, serial, productName, costPrice, sellingPrice, stock, weight);
			break;
		case 2:
			product = ProductFactory.createProduct(eProduct.eProductStore, serial, productName, costPrice, sellingPrice, stock, weight);
			break;
		case 3:
			product = ProductFactory.createProduct(eProduct.eProductWebsite, serial, productName, costPrice, sellingPrice, stock, weight);
			break;
		default:
			FormatsUtils.failureMsg("Invalid input\n");
			break;
		}
		if(product!=null) {
			try {
				systemFacade.addProductToDB(product);
				FormatsUtils.successMsg("Product was added successfully!\n");
			}catch(Exception e) {
				FormatsUtils.failureMsg(e.getMessage());
			}
		}
	}	
	
	public static int pickProductByType(Scanner sc) {
		int option;
		System.out.println("1- To create an wholesalers product");
		System.out.println("2- To create a product in store");
		System.out.println("3- To create a product through website");
		option = (int) getValidNumber(sc,"Enter your choice\n",POSITIVE,Integer.class);
		return option;
	}
	
	public static int pickOrderByProductType(Scanner sc) {
		int option;
		System.out.println("1- To create an order of wholesalers product");
		System.out.println("2- To create an order of product in store");
		System.out.println("3- To create an order of product through website");
		option = (int) getValidNumber(sc,"Enter your choice\n",POSITIVE,Integer.class);
		return option;
	}
	
	public static eShipType getShipTypeMenu(Scanner sc) {
		eShipType type=eShipType.eNone;
		int tmp;
		boolean flag=true;
		do {
			System.out.println();
			System.out.println("Please pick the shipping type");
			System.out.println("For Express enter 1");
			System.out.println("For Standard enter 2");
			tmp = sc.nextInt();
			switch(tmp) {
			case 1:
				type=eShipType.eExpress;
				flag = false;
				break;
			case 2:
				type=eShipType.eStandard;
				flag = false;
				break;
			default:
				flag = true;
				break;
			}
		}while(flag);
		return type;
	}


}
