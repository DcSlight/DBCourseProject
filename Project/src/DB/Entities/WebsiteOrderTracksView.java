package DB.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Components.Contact;
import Components.Customer;
import DB.BasicTable;
import Shipping.Track;
import eNums.eShipMethod;

public class WebsiteOrderTracksView extends BasicTable<String,Object> {
	private String shippingType = "shippingtype";
	private String fromCountryID = "from_country_id";
	private String dateDeparture = "date_departure";
	private String toCountryID = "to_country_id";
	private String dateArrive = "date_arrive";
	private String orderID = "order_id";

    public WebsiteOrderTracksView(Connection conn) {
        super(conn, "shipping_order_tracks_view");
    }
    
    /** 
     * Important: this method will not insert to the view.
     * this method will insert values to 3 different table.
     * this method doesnt support prepare statement (DO in postgress doesnt support ?)
     * @throws SQLException 
     */
    public void insertOrderTransaction(int companyID,String orderID,Set<Track> tracks) throws SQLException {
    	String trackString =this.trackSetToStrings(tracks);
    	String sql = "DO $$\r\n"
    			+ "DECLARE\r\n"
    			+ "    generated_status_code INT;\r\n"
    			+ "BEGIN\r\n"
    			+ "    INSERT INTO shipping_status (company_id, order_id)\r\n"
    			+ "    VALUES ("+companyID+", '"+orderID+"')\r\n"
    			+ "    RETURNING status_code INTO generated_status_code;\r\n"
    			+ "\r\n"
    			+ "    INSERT INTO order_website (order_id, status_code)\r\n"
    			+ "    VALUES ('"+orderID+"', generated_status_code);\r\n"
    			+ "\r\n"
    			+ "    INSERT INTO tracks (shippingType, from_country_id, date_departure, to_country_id, date_arrive, shipping_status_id)\r\n"
    			+ "    VALUES \r\n"
    			+ "    "+ trackString+";\r\n"
    			+ "\r\n"
    			+ "    -- Commit the transaction\r\n"
    			+ "    COMMIT;\r\n"
    			+ "END $$;\r\n"
    			+ "";
    	Statement  stmt = conn.createStatement();
    	stmt.execute(sql);
    }
    
    private String trackSetToStrings(Set<Track> tracks) {
    	StringBuffer st = new StringBuffer("");
    	for (Track t : tracks) {
    		st.append(this.trackToInsertString(t)+",");
    	}
    	st.deleteCharAt(st.length()-1);
    	return st.toString();
    }
    
    private String trackToInsertString(Track t) {
    	String str;
    	str="('"+t.getShippmentType()+"', "+t.getFromCountryID()+", '"+t.formatDate(t.getDateDeparture())
    	+"', "+t.getToCountryID() +", '"+t.formatDate(t.getDateArrive())+"', generated_status_code)";
    	return str;
    }
    

    protected Track mapResultSetToEntityTrack(ResultSet rs) throws Exception {
    	Track t = new Track(eShipMethod.valueOf(rs.getString(shippingType)),
    			rs.getInt(fromCountryID),rs.getString(dateDeparture),
    			rs.getInt(toCountryID),rs.getString(dateArrive));
        return t;
    }
    
    public Set<Track> findAllTracksByOrderID(String orderID) throws Exception{
    	ResultSet rs =this.findBy(this.orderID, orderID);
    	Set<Track> tracks = new HashSet<>();
    	while(rs.next())
    		tracks.add(mapResultSetToEntityTrack(rs));
    	return tracks;
    }
    
    public void updateTrackToArrive(int trackID) throws SQLException {
    	String sql = "UPDATE tracks\r\n"
    			+ "SET has_arrive = True\r\n"
    			+ "WHERE track_id=?;";
    	PreparedStatement  stmt = conn.prepareStatement(sql);
    	stmt.setObject(1, trackID);
    	stmt.executeUpdate();
    }
    

    public void printAllContacts() throws Exception {
        ResultSet rs = this.findAll();
        while (rs.next()) {
            System.out.println(rs.getInt(this.ContactID) + "- " + rs.getString(this.fullName) + "- " + rs.getString(this.whatsApp));
        }
    }
    
    public double getAverageVATRate(String orderID) throws Exception {
    	String sql = "SELECT ROUND(AVG(countries.vat_rate), 2) AS average_vat_rate\r\n"
    			+ "FROM shipping_order_tracks_view\r\n"
    			+ "INNER JOIN countries ON shipping_order_tracks_view.to_country_id = countries.country_id\r\n"
    			+ "WHERE shipping_order_tracks_view.order_id = ?;";
    	PreparedStatement  stmt = conn.prepareStatement(sql);
    	stmt.setObject(1, orderID);
    	ResultSet rs = stmt.executeQuery();
    	if (rs.next()) {
	    	return rs.getDouble("average_vat_rate");
    	}else {
	        // Handle the case where no country is found
	        throw new Exception("Order with ID " + orderID + " does not exist.");
	    }
	    }

    public int deleteContact(int id) throws Exception {
        return this.delete(this.ContactID, id);
    }
}
