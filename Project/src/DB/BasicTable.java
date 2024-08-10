package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public abstract class BasicTable<K,V> implements ICRUD<K, V>{
	protected Connection conn;
	protected String tableName;
	
	public BasicTable(Connection conn, String tableName) {
		this.conn=conn;
		this.tableName=tableName;
	}

	@Override
	public void create(Map<K, V> entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Map<K, V> entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResultSet findBy(String column, String value) throws Exception {
	    String sql = "SELECT * FROM " + this.tableName + " WHERE " + column + " = ?";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    stmt.setObject(1, value);
	    ResultSet rs = stmt.executeQuery();
	    return rs;
	}


	@Override
	public ResultSet findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
