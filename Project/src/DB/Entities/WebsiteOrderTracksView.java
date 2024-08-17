package DB.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import DB.BasicTable;
import Shipping.Track;
import eNums.eShipMethod;
import eNums.eShipType;

public class WebsiteOrderTracksView extends BasicTable<String,Object> {
	private String shippingType = "shippingtype";
	private String fromCountryID = "from_country_id";
	private String dateDeparture = "date_departure";
	private String toCountryID = "to_country_id";
	private String dateArrive = "date_arrive";

    public WebsiteOrderTracksView(Connection conn) {
        super(conn, "shipping_order_tracks_view");
    }
    
    /*
     * Insert website order using transaction
     * that does:
		1.insert to order_website
		2.insert to shipping_status
		3.insert 1 tracks
		4.insert to shippment_route
     */
    public void insertOrderTransaction(int companyID,String orderID,eShipType shipType,List<Track> tracks,double shippingFee) throws SQLException {
    	String trackString =this.trackSetToStrings(tracks);
    	String sql = "BEGIN;\r\n"
    			+ "\r\n"
    			+ "INSERT INTO order_website (order_id, ship_type)\r\n"
    			+ "VALUES ('"+orderID+"', '"+shipType.toString()+"');\r\n"
    			+ "\r\n"
    			+ "WITH inserted_status AS (\r\n"
    			+ "    INSERT INTO shipping_status (company_id, shipping_fee, status)\r\n"
    			+ "    VALUES ("+companyID+", "+shippingFee+", 'eOnTheWay')\r\n"
    			+ "    RETURNING status_code\r\n"
    			+ "),\r\n"
    			+ "\r\n"
    			+ "inserted_tracks AS (\r\n"
    			+ "    INSERT INTO tracks (shippingType, from_country_id, date_departure, to_country_id, date_arrive)\r\n"
    			+ "    VALUES \r\n"
    			+ trackString+"\r\n"
    			+ "    RETURNING track_id, shippingType\r\n"
    			+ ")\r\n"
    			+ "\r\n"
    			+ "INSERT INTO shippment_route (order_id, status_id, tracks_id)\r\n"
    			+ "SELECT '"+orderID+"', inserted_status.status_code, track_id\r\n"
    			+ "FROM inserted_tracks, inserted_status;\r\n"
    			+ "\r\n"
    			+ "COMMIT;";
    	PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.executeUpdate();
    }
    
    private String trackSetToStrings(List<Track> tracks) {
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
    	+"', "+t.getToCountryID() +", '"+t.formatDate(t.getDateArrive())+"' )";
    	return str;
    }
    

    protected Track mapResultSetToEntityTrack(ResultSet rs) throws Exception {
    	Track t = new Track(eShipMethod.valueOf(rs.getString(shippingType)),
    			rs.getInt(fromCountryID),rs.getString(dateDeparture),
    			rs.getInt(toCountryID),rs.getString(dateArrive),rs.getBoolean("has_arrive"));
        return t;
    }
    
    public ResultSet findAllTracksByOrderID(String orderID) throws Exception{
    	 String sql = "select order_id,\r\n"
    	 		+ "shipping_company.name AS company_name,\r\n"
    	 		+ "shippment_route_view.shippingType, \r\n"
    	 		+ "from_countries.country AS from_country_name,\r\n"
    	 		+ "to_countries.country AS to_country_name,\r\n"
    	 		+ "shippment_route_view.date_departure,\r\n"
    	 		+ "shippment_route_view.date_arrive,\r\n"
    	 		+ "shippment_route_view.has_arrive\r\n"
    	 		+ "from shippment_route_view\r\n"
    	 		+ "INNER JOIN shipping_company ON shippment_route_view.company_id = shipping_company.company_id\r\n"
    	 		+ "INNER JOIN countries AS from_countries ON shippment_route_view.from_country_id = from_countries.country_id\r\n"
    	 		+ "INNER JOIN countries AS to_countries ON shippment_route_view.to_country_id = to_countries.country_id\r\n"
    	 		+ "WHERE order_id=?";
    	    PreparedStatement stmt = conn.prepareStatement(sql);
    	    stmt.setObject(1, orderID);
    	    return stmt.executeQuery();
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

    public int deleteOrderWebsite(String orderID) throws Exception {
    	 String sql = "DELETE FROM shippment_route WHERE order_id = ?";
    	 PreparedStatement stmt = conn.prepareStatement(sql);
         stmt.setObject(1, orderID);
         return stmt.executeUpdate();
    }
}
