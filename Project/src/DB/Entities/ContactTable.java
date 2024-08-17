package DB.Entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Components.Contact;
import DB.BasicTable;

public class ContactTable extends BasicTable<String,Object> {
	
	private String ContactID = "contact_id";
	private String fullName = "full_name";
	private String whatsApp = "whats_app";

    public ContactTable(Connection conn) {
        super(conn, "Contact");
    }

    protected Contact mapResultSetToEntity(ResultSet rs) throws SQLException {
    	Contact contact = new Contact(rs.getString(fullName),rs.getString(this.whatsApp));
        return contact;
    }
    
    public Contact findContactByFullName(String fullName) throws Exception{
    	Contact c = null;
		ResultSet rs =this.findBy(this.fullName, fullName);
		c = mapResultSetToEntity(rs);
		return c;
    }

    public void createContact(Contact contact) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(fullName, contact.getName());
        entityMap.put(this.whatsApp, contact.getWhatsApp());

        // Call the generic create method
        this.create(entityMap);
    }

    public void updateContactPhoneNumber(String whatsApp, Contact contact) throws Exception {
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(fullName, contact.getName());
        entityMap.put(this.whatsApp, whatsApp);
        this.update(entityMap, fullName,contact.getName());
    }

    public void printAllContacts() throws Exception {
        ResultSet rs = this.findAll();
        while (rs.next()) {
            System.out.println(rs.getInt(this.ContactID) + "- " + rs.getString(this.fullName) + "- " + rs.getString(this.whatsApp));
        }
    }

    public int deleteContact(int id) throws Exception {
        return this.delete(this.ContactID, id);
    }
}
