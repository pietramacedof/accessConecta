package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import model.User;
import model.UserDAO;
import model.dao.LocationDAO;

/**
 * Servlet implementation class RegisterLocation
 */
@WebServlet("/registerLocation")
public class RegisterLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	LocationDAO dao = new LocationDAO();
	UserDAO udao = new UserDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			System.out.println(token);
			User u = new User();
			u = u.findUserByToken(token);
			
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			List<Location> locations = new ArrayList<>();
			Location l = new Location();
			locations = l.consultAllLocations();
			String jsonResponse = buildJsonResponse("success", "Usuário possui locais cadastrados",
					convertLocationsToJson(locations));
			System.out.println(jsonResponse);
			out.println(jsonResponse);
		} else {
			// Se o cabeçalho de autorização não começa com "Bearer ", envia uma resposta
			// com status 401
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			PrintWriter writer = response.getWriter();
			writer.write("Autorização inválida.");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String token = request.getHeader("Authorization");

		token = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
		Owner owner = udao.findOwnerByToken(token);
		String locationName = request.getParameter("locationName");
		String postalCode = request.getParameter("postalCode");
		String street = request.getParameter("street");
		String neighborhood = request.getParameter("neighborhood");
		String number = request.getParameter("number");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		System.out.println(state + city + number);
		String establishmentType = request.getParameter("establishmentType");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if (dao.existLocation(postalCode, street, number, city, state)) {
			String jsonResponse = "{\"status\": \"error\", \"message\": \"O endereço informado já está cadastrado.\"}";
			out.println(jsonResponse);
			System.out.println("Local já existe.");
			return;
		}
		System.out.println("Informações do local:" + locationName + ", " + postalCode + ", " + street + ", "
				+ neighborhood + ", " + number + ", " + city + ", " + state + ", " + establishmentType);
		if (establishmentType.equalsIgnoreCase("restaurant")) {
			String cuisineType = request.getParameter("cuisineType");
			String operatingDays = request.getParameter("operatingDays");

			Restaurant restaurant = new Restaurant(street, neighborhood, city, state, locationName, postalCode, number,
					cuisineType, operatingDays);
			System.out.println(restaurant.getCep() + restaurant.getTypeOfCuisine());
			int id = dao.insertRestaurant(restaurant, owner);
			System.out.println(id);
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\", \"locationId\": "
					+ id + "}";
			out.println(jsonResponse);
		} else if (establishmentType.equalsIgnoreCase("event")) {
			String startDateStr = request.getParameter("startDate");
			String endDateStr = request.getParameter("endDate");
			String eventPrice = request.getParameter("eventPrice");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null;
			try {
				startDate = formatter.parse(startDateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Date endDate = null;
			try {
				endDate = formatter.parse(endDateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Event event = new Event(street, neighborhood, city, state, locationName, postalCode, number, startDate,
					endDate, eventPrice);
			System.out.println(event.getCep() + event.getEventPrice());
			int id = dao.insertEvent(event, owner);
			System.out.println(id);
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\", \"locationId\": "
					+ id + "}";
			out.println(jsonResponse);

		} else {
			String productType = request.getParameter("productType");

			Store store = new Store(street, neighborhood, city, state, locationName, postalCode, number, productType);

			System.out.println(store.getCep() + store.getTypeProduct());
			int id = dao.insertStore(store, owner);
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\", \"locationId\": "
					+ id + "}";
			out.println(jsonResponse);
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String locationId = request.getParameter("id");
		System.out.println(locationId);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		Location location = new Location();
		if (location.deleteLocation(locationId)) {
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\"}";
			out.println(jsonResponse);
		} else {
			String jsonResponse = "{\"status\": \"failed\", \"message\": \"Operação mal-sucedida\"}";
			out.println(jsonResponse);
		}
	}

	private String buildJsonResponse(String status, String message, String locationsJson) {
		String jsonResponse = "{" + "\"status\":\"" + status + "\"," + "\"message\":\"" + message + "\","
				+ "\"locations\":" + locationsJson + "}";
		return jsonResponse;
	}

	public static String convertLocationsToJson(List<Location> locations) {
		StringBuilder jsonBuilder = new StringBuilder("[");
		boolean first = true;

		for (Location location : locations) {
			if (!first) {
				jsonBuilder.append(",");
			} else {
				first = false;
			}

			double note = location.getAcessibilityNote();
			int quantity = location.getQuantityOfEvaluation();
			String formattedNote = String.format("%.2f", note); // Format to 2 decimal places
			String quantityAsString = String.valueOf(quantity);
			jsonBuilder.append("{").append("\"id\":" + location.getId() + ",")
					.append("\"publicPlace\":\"" + escapeJsonString(location.getPublicPlace()) + "\",")
					.append("\"neighborhood\":\"" + escapeJsonString(location.getNeighborhood()) + "\",")
					.append("\"acessibilityNote\":\"" + escapeJsonString(formattedNote) + "\",")
					.append("\"quantityOfEvaluation\":\"" + escapeJsonString(quantityAsString) + "\",")
					.append("\"city\":\"" + escapeJsonString(location.getCity()) + "\",")
					.append("\"uf\":\"" + escapeJsonString(location.getUf()) + "\",")
					.append("\"placeName\":\"" + escapeJsonString(location.getPlaceName()) + "\",")
					.append("\"cep\":\"" + escapeJsonString(location.getCep()) + "\",")
					.append("\"number\":\"" + escapeJsonString(location.getNumber()) + "\"");

			if (location instanceof Restaurant) {
				Restaurant restaurant = (Restaurant) location;
				jsonBuilder.append(",\"type\":\"restaurant\",")
						.append("\"typeOfCuisine\":\"" + escapeJsonString(restaurant.getTypeOfCuisine()) + "\",")
						.append("\"operatingDays\":\"" + escapeJsonString(restaurant.getOperatingDays()) + "\"");
			} else if (location instanceof Store) {
				Store store = (Store) location;
				jsonBuilder.append(",\"type\":\"store\",")
						.append("\"typeProduct\":\"" + escapeJsonString(store.getTypeProduct()) + "\"");
			} else {
				Event event = (Event) location;
				jsonBuilder.append(",\"type\":\"event\",").append("\"startDate\":\"" + event.getStartDate() + "\",")
						.append("\"endDate\":\"" + event.getEndDate() + "\",")
						.append("\"eventPrice\":\"" + escapeJsonString(event.getEventPrice()) + "\"");
			}

			jsonBuilder.append("}");
		}

		jsonBuilder.append("]");

		return jsonBuilder.toString();
	}

	private static String escapeJsonString(String input) {
		if (input == null) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		for (char c : input.toCharArray()) {
			switch (c) {
			case '"':
				result.append("\\\"");
				break;
			case '\\':
				result.append("\\\\");
				break;
			case '\b':
				result.append("\\b");
				break;
			case '\f':
				result.append("\\f");
				break;
			case '\n':
				result.append("\\n");
				break;
			case '\r':
				result.append("\\r");
				break;
			case '\t':
				result.append("\\t");
				break;
			default:
				result.append(c);
			}
		}
		return result.toString();
	}

}
