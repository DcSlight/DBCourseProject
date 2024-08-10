package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.InputMismatchException;
import java.util.Scanner;

import Components.Customer;
import DB.DatabaseConnection;
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
	public static final String UNDO_ORDER = "5";
	public static final String PRINT_PRODUCT_DETAILS = "6";
	public static final String PRINT_ALL_PRODUCTS = "7";
	public static final String PRINT_PRODUCT_ORDERS = "8";
	public static final String BACKUP_SYSTEM = "9";
	public static final String RESTORE_SYSTEM = "10";
	public static final String EXIT_1 = "E";
	public static final String EXIT_2 = "e";
	public static final Boolean POSITIVE = true;

	public static void main(String[] args) throws Exception  {		
		
		Scanner sc = new Scanner(System.in);
		SystemFacade systemFacade = SystemFacade.getInstance();
		boolean flag = true;
		String option;
		System.out.println(FormatsUtils.ANSI_CYAN_BOLD + "--------------------------------------");
		System.out.println("\tWelcome to the System!");
		System.out.println("--------------------------------------" + FormatsUtils.ANSI_RESET);
		do {
			System.out.println("0 - To init the system");
			System.out.println("1 - To add a product");
			System.out.println("2 - To remove a product");
			System.out.println("3 - To edit a product stock");
			System.out.println("4 - To add a order to product");
			System.out.println("5 - To undo an order");
			System.out.println("6 - Print a product details");
			System.out.println("7 - Print system profit and all products");
			System.out.println("8 - Print product orders");
			System.out.println("9 - To backup System");
			System.out.println("10 - To restore System");
			System.out.println("E/e - To Exit");
			option = sc.nextLine();
			switch(option) {
			case INIT_SYSTEM:
				systemFacade.initSystems(); 
				break;
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
			case UNDO_ORDER:
				System.out.println(systemFacade.undoOrder());
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
			case BACKUP_SYSTEM:
				systemFacade.backUpSystem();
				FormatsUtils.successMsg("System has been backup!\n");
				break;
			case RESTORE_SYSTEM:
				systemFacade.restoreSystem();
				FormatsUtils.successMsg("System has been restore!\n");
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
	
	
	/**
	 * Ex: 4.5
	 * @see   InputExmaple: 3 AAB12 A4 Avi 0506007070 1 Israel 3
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
		System.out.println(systemFacade.getAllProductsByType(productType));
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
		if(product instanceof ProductSoldThroughWebsite) {
			type = getShipTypeMenu(sc);
			sc.nextLine();//CLEAN BUFFER
			System.out.println("Enter Dest Country");
			destCountry=sc.nextLine();
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
			System.out.println(e.getMessage());
		}
	}
	
	public static String getSerialOrder(Scanner sc, SystemFacade systemFacade) {
		String serial;
		System.out.println("Please enter order serial");
		serial = sc.nextLine();
		if(systemFacade.isSerialOrderExist(serial))
			return null;
		return serial;
			
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
	
	/**
	 * Ex: 4.3
	 * @see   InputExmaple: AAB12
	 * @param sc
	 * @param systemFacade
	 */
	public static void removeProduct(Scanner sc, SystemFacade systemFacade)
	{
		Product product;
		product = getProductBySerial(sc, systemFacade);
		if(product == null)
			return;
		if(systemFacade.removeProduct(product)) {
			FormatsUtils.successMsg("Product was removed successfully!\n");
			return;
		}
		FormatsUtils.failureMsg("Product has not been removed\n");
	}
	
	
	/**
	 * Ex: 4.4
	 * @see   InputExmaple: AAB12 20
	 * @param sc
	 * @param systemFacade
	 */
	public static void editProductStock(Scanner sc, SystemFacade systemFacade)
	{
		Product product;
		int stock;
		product = getProductBySerial(sc, systemFacade);
		if(product == null)
			return;
		stock = (int) getValidNumber(sc, "Enter a stock number for the product\n", POSITIVE, Integer.class);
		product.setStock(stock);
		FormatsUtils.successMsg("Product stock was updated!\n");
	}
	
	public static Product getProductBySerial(Scanner sc,SystemFacade systemFacade) {
		Product product;
		String serial;
		if(systemFacade.getProducts().isEmpty()) {
			FormatsUtils.failureMsg("There are no products in the system\n");
			return null;
		}
		System.out.println("Please Enter a product serial");
		serial = sc.nextLine();
		product=systemFacade.getProductBySerial(serial);
		if(product == null) {
			FormatsUtils.failureMsg("Product was not found!\n");
			return null;
		}
		return product;
	}
	
	public static Product getProductBySerialAndType(Scanner sc,SystemFacade systemFacade,eProduct type) {
		Product product;
		String serial;
		if(systemFacade.getProducts().isEmpty()) {
			FormatsUtils.failureMsg("There are no products in the system\n");
			return null;
		}
		System.out.println("Please Enter a product serial");
		serial = sc.nextLine();
		product=systemFacade.getProductBySerialAndType(serial,type);
		if(product == null) {
			FormatsUtils.failureMsg("Product was not found!\n");
			return null;
		}
		return product;
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

	public static Customer getCustomerDetailsMenu(Scanner sc) {
		Customer c;
		String name,mobile;
		System.out.println("Enter Customer name");
		name= sc.nextLine();
		System.out.println("Enter phone number");
		mobile = sc.nextLine();
		c = new Customer(name,mobile);
		return c;
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
			System.out.println(systemFacade.getProductOrders(product) + "\n");
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
			systemFacade.addProduct(product);
			FormatsUtils.successMsg("Product was added successfully!\n");
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
