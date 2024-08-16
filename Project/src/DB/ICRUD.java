package DB;

import java.sql.ResultSet;
import java.util.Map;

public interface ICRUD<K,V> {
	void create(Map<K,V> entity) throws Exception;
	int update(Map<K, V> entity, String primaryKeyColumn, V primaryKeyValue)  throws Exception;
	ResultSet findBy(String column, V value) throws Exception;
	ResultSet findAll() throws Exception;
	int delete(K primaryKeyColumn, V primaryKeyValue) throws Exception;
}
