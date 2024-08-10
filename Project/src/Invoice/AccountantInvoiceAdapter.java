package Invoice;

import Interfaces.IInvoice;

public class AccountantInvoiceAdapter implements IInvoice{
	private AccountantInvoice adaptee;
	
	public AccountantInvoiceAdapter(AccountantInvoice accountantInvoice) {
		adaptee = accountantInvoice;
	}

	@Override
	public String showInvoice() {
		return adaptee.showInvoice();
	}
}
