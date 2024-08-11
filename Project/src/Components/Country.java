package Components;

public class Country {
	private Integer id;
	private String country;
	private double vatRate;
	private String currency_type;
	private double currency_change;
	
	
	public Country(Integer id, String country, double vatRate, String currency_type, double currency_change) {
		this.id = id;
		this.country = country;
		this.vatRate = vatRate;
		this.currency_type = currency_type;
		this.currency_change = currency_change;
	}

	public int getId() {
		return id;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public double getVatRate() {
		return vatRate;
	}
	
	public void setVatRate(double vatRate) {
		this.vatRate = vatRate;
	}
	
	public String getCurrency_type() {
		return currency_type;
	}
	
	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}
	
	public double getCurrency_change() {
		return currency_change;
	}
	
	public void setCurrency_change(double currency_change) {
		this.currency_change = currency_change;
	}
	
	public String prettyStr() {
		return "id: " + this.id + "	 Country: " + this.country;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", country=" + country + ", varRate=" + vatRate + ", currency_type="
				+ currency_type + ", currency_change=" + currency_change + "]";
	}
	
}
