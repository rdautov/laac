package laac.project;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
//import com.maxmind.geoip2.model.CountryResponse;

@WebServlet(name = "IpAddressParser", urlPatterns = { "/parse" })
public class IpAddressParser extends HttpServlet {
	
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 486100875373833076L;
	
	/**
	 * Different MAC addresses
	 */
	ArrayList<String> macAddresses = new ArrayList<String>(
		      Arrays.asList("00-15-5D-00-56-05",
		    		  "00-15-5D-00-56-06", 
		    		  "00-15-5D-00-56-07",
		    		  "00-15-5D-00-56-08",
		    		  "00-15-5D-00-56-09"));

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
		System.out.println("Timestamp 1: " + timestamp1);
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");		
		
		Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
		System.out.println("Timestamp 2: " + timestamp2);
		
		String macAddress = getMacAddress();
		
		File database = new File(
				getClass().getClassLoader().getResource("GeoLite2-City.mmdb").getFile());
		DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
//		InetAddress ipAddress = InetAddress.getByName(request.getRemoteHost());
//		InetAddress ipAddress = InetAddress.getByName("78.91.101.80"); //norway
//		InetAddress ipAddress = InetAddress.getByName("95.31.18.119"); //russia	
//		InetAddress ipAddress = InetAddress.getByName("163.177.112.32"); //china
		InetAddress ipAddress = InetAddress.getByName("146.227.0.115"); //de monfort
//		InetAddress ipAddress = InetAddress.getByName("193.61.201.205"); //kings college
				
		StringBuilder result = new StringBuilder("Your IP address: " + ipAddress.getHostAddress());
		result.append("<br>");
		
		result.append("Your MAC address: " + macAddress);
		result.append("<br>");
		
		CityResponse cResponse;
		
		try {
			cResponse = dbReader.city(ipAddress);
			String countryName = cResponse.getCountry().getName();
		    result.append("Your country: " + countryName);
		    result.append("<br>");
		    
		    String cityName = cResponse.getCity().getName();
		    result.append("Your city: " + cityName);
		    result.append("<br>");
		    
		    String postal = cResponse.getPostal().getCode();
		    result.append("Your post code: " + postal);
		    result.append("<br>");
		    
		    String state = cResponse.getLeastSpecificSubdivision().getName();
		    result.append("Your area: " + state);
		    result.append("<br>");
		    
		    if (countryName.equalsIgnoreCase("russia") || countryName.equalsIgnoreCase("china")) {
		    	//result.append("<font color=\"red\">Based on your current location, you are not allowed to access this resource!</font>");
		    	result.append("<font color=\"red\">A transition to an untrusted network detected! You are not allowed to access this resource!</font>");
		    } else {
		    	//result.append("<font color=\"green\">Access granted!</font>");
		    	result.append("<font color=\"green\">A transition to a trusted network detected! You are still able to access this resource!</font>");
		    }
		    result.append("<br>");
		    
		    result.append("<br>");
		    result.append("<button type=\"button\">Switch to a new location</button>");
		    result.append("&nbsp;");
		    result.append("<button type=\"button\">Switch to the original location</button>");
		    result.append("<br>");
		    
		    result.append("<br>");
		    result.append("<button type=\"button\">Switch to a new MAC address</button>");
		    result.append("&nbsp;");
		    result.append("<button type=\"button\">Switch to the original MAC address</button>");
		    result.append("<br>");
		
			response.getWriter().print(result.toString());
		} catch (GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print(e.getLocalizedMessage());
		}    
		
		Timestamp timestamp3 = new Timestamp(System.currentTimeMillis());
		System.out.println("Timestamp 3: " + timestamp3);
		
		
		//System.out.println("Time difference: " + timestamp2. - timestamp1);
	}
	
	/**
	 * Get MAC address of the local machine
	 * 
	 * @return
	 */
	public String getMacAddress() {
		
		String result = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			System.out.println("IP address : " + ip.getHostAddress());
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			System.out.print("MAC address : ");

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			result = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}