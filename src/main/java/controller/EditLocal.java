package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Event;
import model.Location;
import model.Owner;
import model.Restaurant;
import model.Store;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet implementation class EditLocal
 */
@WebServlet("/alterLocation")
public class EditLocal extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditLocal() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		
		token = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
		String locationName = request.getParameter("locationName");
		String postalCode = request.getParameter("postalCode");
		String street = request.getParameter("street");
		String neighborhood = request.getParameter("neighborhood");
		String number = request.getParameter("number");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String id = request.getParameter("locationId");
		String type = request.getParameter("locationType");
		System.out.println("Tipo: " + type);
		Location location = new Location(id, street, neighborhood, city, state, locationName, postalCode, number);
		System.out.println(state + city + id);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		boolean exist = location.isExist(location);
		System.out.println(exist);
		if (exist) {
			String jsonResponse = "{\"status\": \"error\", \"message\": \"O endereço informado já está cadastrado.\"}";
			out.println(jsonResponse);
			System.out.println("Local já existe.");
			return;
		}
		else {
			if(type.equalsIgnoreCase("restaurant")) {
				String typeOfCuisine = request.getParameter("cuisineType");
				String operatingDays = request.getParameter("operatingDays");
				Restaurant r = new Restaurant(id, street, neighborhood, city, state, locationName, postalCode, number, typeOfCuisine, operatingDays);	
				System.out.println("é restaurante" + typeOfCuisine);
				if(r.updateLocation(r)) {
					String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida.\"}";
					out.println(jsonResponse);
				}
				else {
					System.out.println("n atualizou");
				}
			} else if(type == "event") {
				String startDateStr = request.getParameter("startDate");
				String endDateStr = request.getParameter("endDate");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = null;
				Date endDate = null;
				try {
					startDate = formatter.parse(startDateStr);
					endDate = formatter.parse(endDateStr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				Event e = new Event(id, street, neighborhood, city, state, locationName, postalCode, number, startDate, endDate, null);
				if(e.updateLocation(e)) {
					String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida.\"}";
					out.println(jsonResponse);
				}
				else {
					System.out.println("n atualizou");
				}
			} else {
				String productType = request.getParameter("productType");
				Store s = new Store (id, street, neighborhood, city, state, locationName, postalCode, number, productType);
				if(s.updateLocation(s)) {
					String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida.\"}";
					out.println(jsonResponse);
				}
				else {
					System.out.println("n atualizou");
				}
			}
		}
	}

}
