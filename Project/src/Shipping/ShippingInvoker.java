package Shipping;

import java.util.HashSet;
import java.util.Set;

import Interfaces.ICommand;
import Products.Product;
import eNums.eShipType;
import Interfaces.IShippingReceiver;

public class ShippingInvoker {
	private Set<ICommand> expressCommands;
	private Set<ICommand> standardCommands;

	public ShippingInvoker() {
		this.expressCommands = new HashSet<>();
		this.standardCommands = new HashSet<>();
	}

	public void addCommand(eShipType type, ICommand command) {
		switch (type) {
		case eExpress:
			addExpressCommand(command);
		case eStandard:
			addStandardCommand(command);
		default:
			break;
		}
	}

	private void addExpressCommand(ICommand command) {
		this.expressCommands.add(command);
	}

	private void addStandardCommand(ICommand command) {
		this.standardCommands.add(command);
	}

	public IShippingReceiver calculateShippingFee(eShipType type, Product product) {
		switch (type) {
		case eExpress:
			setCommandsProduct(expressCommands, product);
			return calculateCheapestShipping(expressCommands);
		case eStandard:
			setCommandsProduct(standardCommands, product);
			return calculateCheapestShipping(standardCommands);
		default:
			return null;
		}
	}

	private void setCommandsProduct(Set<ICommand> commands, Product product) {
		for (ICommand command : commands) {
			command.setNewProduct(product);
		}
	}

	private IShippingReceiver calculateCheapestShipping(Set<ICommand> commands) {
		double cheapestPrice = Double.MAX_VALUE;
		IShippingReceiver cheappestReciever = null;
		double importTax = 1; //TODO: need to take from the DB
		for (ICommand command : commands) {
			IShippingReceiver receiver = command.execute(importTax); // Assume execute() returns the IShippingReceiver
			double price = receiver.getPrice();
			if (price < cheapestPrice) {
				cheapestPrice = price;
				cheappestReciever = receiver;
			}
		}

		return cheappestReciever;
	}
}
