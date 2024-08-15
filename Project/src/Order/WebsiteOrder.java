package Order;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import Components.Customer;
import DB.DatabaseConnection;
import DB.Entities.CountryTable;
import DB.Entities.ShippingCompanyTable;
import DB.Entities.WebsiteOrderTracksView;
import Products.Product;
import Shipping.ShippingCompany;
import Shipping.Track;
import System.SystemFacade;
import eNums.eShipMethod;
import eNums.eStatus;

public class WebsiteOrder extends Order{
	private int companyID;
	private Set<Track> tracks;
	private eStatus status;
	
	//private double shippingPrice; will be calc in the controller

	public WebsiteOrder(Product product, Customer customer, 
			int amount,
			String serial,double profit,eStatus status) {
		super(product,customer,amount,serial,profit);
		this.tracks = new HashSet<>();
		this.status=status;
	}
	
	/*
	 * adding websiteOrder and create a random way until it get to customer
	 */
	public void createWebsiteOrder() throws SQLException, Exception {
		List<Integer> countriesRoute = new ArrayList<Integer>();
		CountryTable ct = new CountryTable(DatabaseConnection.getConnection());
		countriesRoute=ct.getRoute(this.getCustomer().getCountryID());
		// Define a subset of the ShippingType enum
		eShipMethod[] subset = {eShipMethod.eShip, eShipMethod.ePlane};

        // Randomly select a ShippingType from the subset
        eShipMethod randomType;
        Integer fromCountry;
        List<String> dates = SystemFacade.generateRandomDates(countriesRoute.size()+1);
		System.out.println(dates);
		System.out.println(countriesRoute);
        int index=0;
        Track t;
        List<Track> tracks = new ArrayList<>();
		for (Integer country : countriesRoute) {
			fromCountry=country;
			randomType=SystemFacade.getRandomEnumValueFromSubset(subset);
			if(fromCountry != this.getCustomer().getCountryID()) {
				//for any country thats not the last country
				t= new Track(randomType,fromCountry,dates.get(index),
						countriesRoute.get(index+1),dates.get(index+1));
			}
			else {
				//if arrive to last country
				t= new Track(eShipMethod.eTruck,fromCountry,dates.get(index),
						countriesRoute.get(index),dates.get(index+1));
			}
			tracks.add(t);
			if(index < countriesRoute.size())
				index++;
			System.out.println(t);
		}
	}
	

	public eStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(eStatus status) {
		this.status=status;
	}
	
	public int getCountryID() {
		return this.getCustomer().getCountryID();
	}
	

	public ShippingCompany getCompany() throws SQLException, Exception {
		ShippingCompanyTable st = new ShippingCompanyTable(DatabaseConnection.getConnection());
		return st.findCompanyByID(this.companyID);
	}

	public void setCompany(int companyID) {
		//TODO: NEED query here
		this.companyID = companyID;
	}

	/*
	 * the shipping price is the average of VAT in all the country at need to visit
	 * */
	public double getShippingPrice() throws SQLException, Exception {
		WebsiteOrderTracksView w = new WebsiteOrderTracksView(DatabaseConnection.getConnection());
		double shippingPrice = w.getAverageVATRate(this.getSerial());
		return shippingPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(companyID, status, tracks);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebsiteOrder other = (WebsiteOrder) obj;
		return companyID == other.companyID && status == other.status && Objects.equals(tracks, other.tracks);
	}

	@Override
	public String toString() {
		return "WebsiteOrder [companyID=" + companyID + ", tracks=" + tracks + ", status=" + status + "]";
	}
	
	
	


	
	
	
	
}
