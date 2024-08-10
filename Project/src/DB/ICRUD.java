package DB;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface ICRUD<K,V> {
	void create(Map<K,V> entity) throws Exception;
	void update(Map<K,V> entity) throws Exception;
	ResultSet findBy(String column, String value) throws Exception;
	ResultSet findAll() throws Exception;
	void delete() throws Exception;
}
