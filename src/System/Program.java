package System;

import Components.Contact;
import Products.Product;
import Products.ProductSoldThroughWebsite;
import Shipping.*;
import eNums.eShipType;

public class Program {

	public static void main(String[] args) {
		ShippingInvoker shippingInvoker = new ShippingInvoker();
		Contact c = new Contact("idan","0506507070");
        DHL dhl = new DHL(c, 20);
        FedEx fedEx = new FedEx(c, 20);
        Product product1 = new ProductSoldThroughWebsite("AA12", "Tami 4", 50, 70, 25, 4.2);
        Product product2 = new ProductSoldThroughWebsite("BA13", "Tami 5", 100, 200, 25, 25);
        Product product3 = new ProductSoldThroughWebsite("CA14", "Tami 6", 50, 70, 25, 17.6);
        
        shippingInvoker.addExpressCommand(new FedExExpressCommand(fedEx));
        shippingInvoker.addExpressCommand(new DHLExpressCommand(dhl));
        
        shippingInvoker.addStandardCommand(new FedExStandardCommand(fedEx));
        shippingInvoker.addStandardCommand(new DHLStandardCommand(dhl));
        
        System.out.println(shippingInvoker.calculateShippingFee(eShipType.eExpress, product1));
        System.out.println(shippingInvoker.calculateShippingFee(eShipType.eStandard, product1));
        
        System.out.println(shippingInvoker.calculateShippingFee(eShipType.eExpress, product2));
        System.out.println(shippingInvoker.calculateShippingFee(eShipType.eStandard, product2));
        
        System.out.println(shippingInvoker.calculateShippingFee(eShipType.eExpress, product3));
        System.out.println(shippingInvoker.calculateShippingFee(eShipType.eStandard, product3));
	}

}
