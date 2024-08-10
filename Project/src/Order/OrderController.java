package Order;

import java.util.Stack;

import Exception.StockException;
import Interfaces.IUndoCommand;
import Products.Product;

public class OrderController{
	private Stack<IUndoCommand> stack;
	private OrderControllerMemento memento;
	
	public OrderController() {
		stack = new Stack<>();
	}
	
	public void createMemento() {
		memento= new OrderControllerMemento(stack);
	}

	public void setMemento() {
		this.stack = memento.stack;
	}
	
	public static class OrderControllerMemento{
		private Stack<IUndoCommand> stack;
		
		private OrderControllerMemento(Stack<IUndoCommand> stack){
			this.stack =new Stack<>();
			Stack<IUndoCommand> tmp = new Stack<>();
			while(!stack.isEmpty()) {
				tmp.push(stack.pop());
			}
			
			while(!tmp.isEmpty()) {
				stack.push(tmp.lastElement());
				this.stack.push(tmp.pop());
			}
		}
	}
	
	public void removeOrdersOfProducts(Product p) {
		Stack<IUndoCommand> tmp = new Stack<>();
		while(!this.stack.isEmpty()) {
			IUndoCommand cmd = this.stack.pop();
			Product pTemp =cmd.getProduct();
			if(!pTemp.equals(p))
				tmp.push(cmd);
		}
		
		while(!tmp.isEmpty()) {
			this.stack.push(tmp.pop());
		}
	}
	
	public boolean haveOrders() {
		if (stack.isEmpty())
			return false;
		return true;
	}
	
	public void updateOrders(Order order, Product product) throws StockException {
		IUndoCommand cmd = new OrderUpdateCommand(product,order);
		cmd.execute();
		stack.add(cmd);
	}

	public void undoOrder() {
		if (!stack.isEmpty()) {
			IUndoCommand cmd = stack.pop();
			cmd.undo();
		}
	}
}
