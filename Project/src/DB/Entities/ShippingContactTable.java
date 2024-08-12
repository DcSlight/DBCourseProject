package DB.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Components.Contact;
import DB.BasicTable;
import Shipping.ShippingCompany;

public class ShippingContactTable extends BasicTable<String,Object> {
	
	private String companyId = "company_id";
	private String contactId = "contact_id";

    public ShippingContactTable(Connection conn) {
        super(conn, "shipping_company_contacts");
    }

    protected void mapResultSetToEntity(ResultSet rs,ShippingCompany company) throws SQLException {
    	Contact c = new Contact(rs.getString("full_name"), rs.getString("whats_app"));
    	company.addContact(c);
    }
    
    public void findAllContactOfCompany(ShippingCompany company) throws Exception{
    	 String sql = "SELECT \r\n"
    	 		+ "    Contact.full_name,\r\n"
    	 		+ "    Contact.whats_app,\r\n"
    	 		+ "    Shipping_Company.name AS company_name\r\n"
    	 		+ "FROM \r\n"
    	 		+ "    shipping_company_contacts\r\n"
    	 		+ "INNER JOIN \r\n"
    	 		+ "    Contact ON shipping_company_contacts.contact_id = Contact.contact_id\r\n"
    	 		+ "INNER JOIN \r\n"
    	 		+ "    Shipping_Company ON shipping_company_contacts.company_id = Shipping_Company.company_id\r\n"
    	 		+ "WHERE  Shipping_Company.company_id=?;";
     	 PreparedStatement stmt = conn.prepareStatement(sql);
         stmt.setObject(1, company.getCompanyID());
         ResultSet rs = stmt.executeQuery(); // Return the ResultSet containing all rows
         while(rs.next())
         	 mapResultSetToEntity(rs,company);
    }

    public void insertShippingContact(int companyID, int contactID) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(this.companyId, companyID);
        entityMap.put(this.contactId, contactID);
        // Call the generic create method
        this.create(entityMap);
    }

    public int deleteShippingContact(int companyID, int contactID) throws Exception {
    	String sql = "DELETE FROM " + tableName + " WHERE "+companyId+"=? AND "+contactId +"=?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setObject(1, companyID);
        stmt.setObject(1, contactID);
        return stmt.executeUpdate(); // Returns the number of rows affected
    }
}
