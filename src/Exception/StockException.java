package Exception;

public class StockException extends Exception{

	private static final long serialVersionUID = 1L;

	public StockException(String msg) {
		super(msg);
	}

	public StockException() {
		super("Error: Not enough in stock!");
	}
}



