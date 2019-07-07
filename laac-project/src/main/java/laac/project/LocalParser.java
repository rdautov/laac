package laac.project;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class LocalParser {

	static DatabaseReader dbReader;

	public static void main(String[] args) throws IOException, GeoIp2Exception {

		// InetAddress ipAddress = InetAddress.getByName(request.getRemoteHost());
//		InetAddress ipAddress = InetAddress.getByName("78.91.101.80"); //norway
//		InetAddress ipAddress = InetAddress.getByName("95.31.18.119"); //russia	
//		InetAddress ipAddress = InetAddress.getByName("163.177.112.32"); //china
//		InetAddress ipAddress = InetAddress.getByName("146.227.159.62"); //de monfort
		InetAddress ipAddress = InetAddress.getByName("193.61.201.205"); // kings college
				
		loadDB();
		System.out.println(parse(ipAddress));	

	}

	/**
	 * @throws IOException
	 */
	private static void loadDB() throws IOException {
		File database = new File(LocalParser.class.getClassLoader().getResource("GeoLite2-City.mmdb").getFile());
		dbReader = new DatabaseReader.Builder(database).build();
	}

	/**
	 * @param ipAddress
	 * @return
	 * @throws IOException
	 * @throws GeoIp2Exception
	 */
	private static StringBuilder parse(InetAddress ipAddress) throws IOException, GeoIp2Exception {

		StringBuilder result = new StringBuilder();

		CityResponse cResponse = dbReader.city(ipAddress);
		String countryName = cResponse.getCountry().getName();
		result.append("Your country: " + countryName);
		result.append(System.getProperty("line.separator"));

		String cityName = cResponse.getCity().getName();
		result.append("Your city: " + cityName);
		result.append(System.getProperty("line.separator"));

		String postal = cResponse.getPostal().getCode();
		result.append("Your post code: " + postal);
		result.append(System.getProperty("line.separator"));

		String state = cResponse.getLeastSpecificSubdivision().getName();
		result.append("Your area: " + state);
		result.append(System.getProperty("line.separator"));
		
		if (countryName.equalsIgnoreCase("russia") || countryName.equalsIgnoreCase("china")) {
	    	result.append("Based on your current location, you are not allowed to access this resource!");
	    } else {
	    	result.append("Access granted!");
	    }

		return result;
	}

}
