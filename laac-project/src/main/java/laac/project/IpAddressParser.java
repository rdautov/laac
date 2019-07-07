package laac.project;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

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

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");		
		
		File database = new File(
				getClass().getClassLoader().getResource("GeoLite2-City.mmdb").getFile());
		DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
//		InetAddress ipAddress = InetAddress.getByName(request.getRemoteHost());
//		InetAddress ipAddress = InetAddress.getByName("78.91.101.80"); //norway
//		InetAddress ipAddress = InetAddress.getByName("95.31.18.119"); //russia	
//		InetAddress ipAddress = InetAddress.getByName("163.177.112.32"); //china
//		InetAddress ipAddress = InetAddress.getByName("146.227.0.115"); //de monfort
		InetAddress ipAddress = InetAddress.getByName("193.61.201.205"); //kings college
				
		StringBuilder result = new StringBuilder("Your IP address: " + ipAddress.getHostAddress());
	    result.append(System.getProperty("line.separator"));
		CityResponse cResponse;
		
		try {
			cResponse = dbReader.city(ipAddress);
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

			response.getWriter().print(result.toString());
		} catch (GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().print(e.getLocalizedMessage());
		}    
	}
}