package Shipping;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import eNums.eShipMethod;

public class Track {
	private eShipMethod shippmentType;
	private Integer fromCountryID;
	private Date dateDeparture;
	private Integer toCountryID;
	private Date dateArrive;
	
	public Track(eShipMethod shippmentType, Integer fromCountryID, String dateDeparture, Integer toCountryID,
			String dateArrive) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.shippmentType = shippmentType;
		this.fromCountryID = fromCountryID;
		this.dateDeparture = dateFormat.parse(dateDeparture);
		this.toCountryID = toCountryID;
		this.dateArrive = dateFormat.parse(dateArrive);;
	}
	public eShipMethod getShippmentType() {
		return shippmentType;
	}
	
	public String formatDate(Date date) {
		   // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Format the Date object into the desired string
        String formattedDate = dateFormat.format(date);
        return formattedDate;
	}

	public Integer getFromCountryID() {
		return fromCountryID;
	}

	public Date getDateDeparture() {
		return dateDeparture;
	}

	public Integer getToCountryID() {
		return toCountryID;
	}

	public Date getDateArrive() {
		return dateArrive;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(dateArrive, dateDeparture, fromCountryID, shippmentType, toCountryID);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Track other = (Track) obj;
		return Objects.equals(dateArrive, other.dateArrive) && Objects.equals(dateDeparture, other.dateDeparture)
				&& Objects.equals(fromCountryID, other.fromCountryID) 
				&& shippmentType == other.shippmentType && Objects.equals(toCountryID, other.toCountryID);
	}
	@Override
	public String toString() {
		return "Track [shippmentType=" + shippmentType + ", fromCountryID=" + fromCountryID + ", dateDeparture="
				+ dateDeparture + ", toCountryID=" + toCountryID + ", dateArrive=" + dateArrive 
				+ "]";
	}

	
	
	
}
