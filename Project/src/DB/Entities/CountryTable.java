package DB.Entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Components.Country;
import Components.Customer;
import DB.BasicTable;

public class CountryTable extends BasicTable<String,Object> {
	
	private String countryID = "country_id";
	private String country = "country";
	private String vat_rate = "vat_rate";
	private String currency_type = "currency_type";
	private String currency_change = "currency_change";

    public CountryTable(Connection conn) {
        super(conn, "countries");
    }

    protected Country mapResultSetToEntity(ResultSet rs) throws SQLException {
    	Country country = new Country(rs.getInt(countryID),rs.getString(this.country),rs.getDouble(vat_rate)
    			,rs.getString(currency_type),rs.getDouble(currency_change));
    	return country;
    }
    
    public Country findCountryByName(String countryName) throws Exception{
    	Country c;
		ResultSet rs =this.findBy(this.country, countryName);
		c = mapResultSetToEntity(rs);
		return c;
    }

    public void createCountry(Country country) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(this.country, country.getCountry());
        entityMap.put(vat_rate, country.getVatRate());
        entityMap.put(currency_type, country.getCurrency_type());
        entityMap.put(currency_change, country.getCurrency_change());

        // Call the generic create method
        this.create(entityMap);
    }

    public void updateCountryByName(Country country) throws Exception {
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(this.country, country.getCountry());
        entityMap.put(vat_rate, country.getVatRate());
        entityMap.put(currency_type, country.getCurrency_type());
        entityMap.put(currency_change, country.getCurrency_change());
        this.update(entityMap, this.country,country.getCountry());
    }

    public int deleteCountry(int id) throws Exception {
        return this.delete(this.countryID, id);
    }
}
