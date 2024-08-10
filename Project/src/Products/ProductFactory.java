package Products;

import eNums.eProduct;

public class ProductFactory {
	public static Product createProduct(eProduct type,String serial,String productName,double costPrice,double sellingPrice,int stock,double weight) {
		switch(type) {
		case eProductWebsite:
			return new ProductSoldThroughWebsite(serial, productName, costPrice, sellingPrice, stock,weight);
		case eProductStore:
			return new ProductSoldInStore(serial, productName, costPrice, sellingPrice, stock,weight);
		case eProductWholesalers: 
			return new ProductSoldToWholesalers(serial, productName, costPrice, sellingPrice, stock,weight);
		default:
			throw new IllegalArgumentException();
		}
	}
}
