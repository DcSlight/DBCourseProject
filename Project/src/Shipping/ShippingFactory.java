package Shipping;

import Interfaces.ICommand;
import eNums.eShipType;

public class ShippingFactory {
	public static ICommand createShippingCommand(eShipType type,ShippingCompany company) {
		switch(type) {
		case eExpress:
			if(company instanceof FedEx) 
				return new FedExExpressCommand((FedEx)company);
			if(company instanceof DHL)
				return new DHLExpressCommand((DHL)company);
		case eStandard:
			if(company instanceof FedEx) 
				return new FedExStandardCommand((FedEx)company);
			if(company instanceof DHL)
				return new DHLStandardCommand((DHL)company);
		default:
			throw new IllegalArgumentException();
		}
	}
}
