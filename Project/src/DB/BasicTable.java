package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public abstract class BasicTable<K, V> implements ICRUD<K, V>{
	protected Connection conn;
	protected String tableName;
	
	public BasicTable(Connection conn, String tableName) {
		this.conn=conn;
		this.tableName=tableName;
	}

	@Override
	public void create(Map<K, V> entity) throws Exception {
	    StringBuffer columns = new StringBuffer();
	    StringBuffer values = new StringBuffer();
	    // Loop through the map and prepare the columns and placeholder
	    for (K key : entity.keySet()) {
	    	if(key.toString().contains("::")) {//support ENUMS
	    		String[] parts = key.toString().split("::");
	    		columns.append(parts[0].toString()+",");
	    		values.append("?::"+parts[1]+",");
	    	}else {
		        columns.append(key.toString()+",");
		        values.append("?"+",");
	    	}
	    }
	    columns.deleteCharAt(columns.length()-1); //remove the ,
	    values.deleteCharAt(values.length()-1);
	    
	
	    String sql = "INSERT INTO " + tableName + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    // Set the values in the prepared statement
	    int index = 1;
	    for (V value : entity.values()) {
	        stmt.setObject(index++, value);
	    }
	    stmt.executeUpdate();
    }
	
	@Override
	public int update(Map<K, V> entity, String primaryKeyColumn, V primaryKeyValue) throws Exception {
	    // Construct the SET params of the SQL UPDATE statement
	    StringBuffer params = new StringBuffer();
	
	    // Loop through the map to construct the SET clause
	    for (Map.Entry<K, V> entry : entity.entrySet()) {	
	    	
	    	if(entry.getKey().toString().contains("::")) {//support ENUMS
	    		String[] parts = entry.getKey().toString().split("::");
	    		params.append(parts[0] + " = ?::"+parts[1]+",");
	    	}else {
	    		params.append(entry.getKey() + " = ?,");
	    	}
	    }
	    
	    params.deleteCharAt(params.length()-1);
	    String sql = "UPDATE " + tableName + " SET " + params.toString() + " WHERE " + primaryKeyColumn + " = ?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    int index = 1;
	    for (V value : entity.values()) {
	        stmt.setObject(index++, value);
	    }
	    // Set the primary key value for the WHERE clause
	    stmt.setObject(index, primaryKeyValue);
	    return stmt.executeUpdate();
	}

	@Override
	public ResultSet findBy(String column, V value) throws Exception {
	    String sql = "SELECT * FROM " + this.tableName + " WHERE " + column + " = ?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    stmt.setObject(1, value);
	    ResultSet rs = stmt.executeQuery();
	    return rs;
	}

	@Override
    public ResultSet findAll() throws Exception {
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery(); // Return the ResultSet containing all rows
    }

	@Override
	public int delete(K primaryKeyColumn, V primaryKeyValue) throws Exception {
	    String sql = "DELETE FROM " + tableName + " WHERE " + primaryKeyColumn + " = ?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setObject(1, primaryKeyValue);
        return stmt.executeUpdate(); // Returns the number of rows affected
	}

}
