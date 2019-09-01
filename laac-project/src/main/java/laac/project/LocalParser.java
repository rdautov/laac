package laac.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.InetAddress;
import java.nio.file.Files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class LocalParser {

	static DatabaseReader dbReader;
	static CSVParser csvParser;

	public static void main(String[] args) throws IOException, GeoIp2Exception {

		loadIpAddressDB();
		
		for (CSVRecord record : csvParser) {
			System.out.println(record.get("network"));
		}
		// InetAddress ipAddress = InetAddress.getByName(request.getRemoteHost());
//		InetAddress ipAddress = InetAddress.getByName("78.91.101.80"); //norway
//		InetAddress ipAddress = InetAddress.getByName("95.31.18.119"); //russia	
//		InetAddress ipAddress = InetAddress.getByName("163.177.112.32"); //china
//		InetAddress ipAddress = InetAddress.getByName("146.227.159.62"); //de monfort
		InetAddress ipAddress = InetAddress.getByName("193.61.201.205"); // kings college
				
		loadGeoIpDB();
		System.out.println(parse(ipAddress));

	}

	/**
	 * @throws IOException
	 */
	private static void loadGeoIpDB() throws IOException {
		File database = new File(LocalParser.class.getClassLoader().getResource("GeoLite2-City.mmdb").getFile());
		dbReader = new DatabaseReader.Builder(database).build();
	}
	
	/**
	 * @throws IOException
	 */
	private static void loadIpAddressDB() throws IOException {
		File database = new File(LocalParser.class.getClassLoader().getResource("geoip2-ipv4.csv").getFile());
		BufferedReader ipReader = new BufferedReader(new FileReader(database));
        csvParser = new CSVParser(ipReader, CSVFormat.DEFAULT.withHeader("network", "geoname_id", "continent_code", "continent_name",
        		"country_iso_code","country_name", "is_anonymous_proxy", "is_satellite_provider")
                .withIgnoreHeaderCase()
                .withTrim());
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
