package DB.Entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import DB.BasicTable;
import Shipping.CompanyFactory;
import Shipping.ShippingCompany;

public class ShippingCompanyTable extends BasicTable<String,Object> {
	
	private String companyID = "company_id";
	private String name = "name";

    public ShippingCompanyTable(Connection conn) {
        super(conn, "shipping_company");
    }

    protected ShippingCompany mapResultSetToEntity(ResultSet rs) throws Exception {
    	ShippingCompany company = CompanyFactory.createCompany(rs.getInt(companyID), rs.getString(name));
    	ShippingContactTable t = new ShippingContactTable(this.conn);
    	t.findAllContactOfCompany(company);
    	return company;
    }
    
    public ShippingCompany findCompany(String name) throws Exception{
    	ShippingCompany company = null;
		ResultSet rs =this.findBy(this.name, name);
		while(rs.next())
			company = mapResultSetToEntity(rs);
		return company;
    }
    
    public Set<ShippingCompany> findAllCompanies() throws Exception{
    	ResultSet rs =this.findAll();
    	Set<ShippingCompany> companies= new HashSet<>();
    	while(rs.next()) {
    		ShippingCompany s = mapResultSetToEntity(rs);
    		companies.add(s);
    	}
    	return companies;
    }
    
    public ShippingCompany findCompanyByID(int id) throws Exception{
    	ShippingCompany company = null;
		ResultSet rs =this.findBy(this.companyID, id);
		company = mapResultSetToEntity(rs);
		return company;
    }
    
}
