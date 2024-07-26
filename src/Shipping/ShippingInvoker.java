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

    public void addExpressCommand(ICommand command) {
        this.expressCommands.add(command);
    }

    public void addStandardCommand(ICommand command) {
        this.standardCommands.add(command);
    }
    
    public String calculateShippingFee(eShipType type, Product product)
    {
    	switch(type) {
    		case eExpress:
    			setCommandsProduct(expressCommands, product);
    			return calculateCheapestShipping(expressCommands);
    		case eStandard:
    			setCommandsProduct(standardCommands, product);
    			return calculateCheapestShipping(standardCommands);
			default:
				return "Wrong";//TODO: exception
    	}
    }
    
    private void setCommandsProduct(Set<ICommand> commands, Product product)
    {
    	for (ICommand command : commands) {
    		command.setNewProduct(product);
    	}
    }

    private String calculateCheapestShipping(Set<ICommand> commands) {
        double cheapestPrice = Double.MAX_VALUE;
        String cheapestCompany = "";

        for (ICommand command : commands) {
            IShippingReceiver receiver = command.execute(); // Assume execute() returns the IShippingReceiver
            double price = receiver.getPrice();
            if (price < cheapestPrice) {
                cheapestPrice = price;
                cheapestCompany = receiver.getName();
            }
            System.out.println(price); //TODO: delete
        }

        return cheapestCompany + " offers the cheapest shipping at $" + (float)cheapestPrice;
    }
}
