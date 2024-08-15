package DB.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public Country findCountryByID(Integer countryID) throws Exception{
    	Country c;
		ResultSet rs =this.findBy(this.countryID, countryID);
		if (rs.next()) {
	        // If a row is found, map the result set to a Country entity
	        c = mapResultSetToEntity(rs);
	    } else {
	        // Handle the case where no country is found
	        throw new Exception("Country with ID " + countryID + " does not exist.");
	    }
		return c;
    }
    
    /*
     * Randomize a route a countries exclude the country
     */
    public List<Integer> getRoute(int lastCountry) throws SQLException{
    	String sql = "WITH shuffled_countries AS (\r\n"
    			+ "    SELECT country_id\r\n"
    			+ "    FROM countries\r\n"
    			+ "    WHERE country_id <> ? "
    			+ "    ORDER BY RANDOM() \r\n"
    			+ "    LIMIT (SELECT FLOOR(RANDOM() * 5) + 1) \r\n"
    			+ ")\r\n"
    			+ "SELECT country_id\r\n"
    			+ "FROM shuffled_countries";
    	PreparedStatement stmt = this.conn.prepareStatement(sql);
    	stmt.setObject(1, lastCountry);
    	ResultSet rs = stmt.executeQuery();
    	List<Integer> l = new ArrayList<>();
    	while(rs.next()) {
    		l.add(rs.getInt(this.countryID));
    	}
    	l.add(lastCountry);
    	return l;
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
    
    public void printAllCountries() throws Exception {
    	ResultSet rs = this.findAll();
    	while(rs.next()) {
    		Country c =mapResultSetToEntity(rs);
    		System.out.println(c.prettyStr());
    	}
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
