package controller;

import java.io.BufferedReader;
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
import model.Evaluation;
import model.Evaluator;
import model.Event;
import model.Location;
import model.Owner;
import model.Restaurant;
import model.Store;
import model.User;
import model.dao.UserDAO;
import model.dao.LocationDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


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
		PrintWriter out = response.getWriter();
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			System.out.println(token);
			Evaluator u = new Evaluator();
			u = u.findEvaluatorByToken(token);
			System.out.println(u.getEmail());
			Evaluation evaluation = new Evaluation();
			List<Evaluation> e = new ArrayList<>();
			e = evaluation.consultEvaluationByUser(u);
			for (Evaluation evalu : e) {
			    System.out.println("ID: " + evalu.getId());
			    System.out.println("Note: " + evalu.getNote());
			    System.out.println("User: " + evalu.getUser().getId());
			    System.out.println("Location: " + evalu.getLocation().getId());
			    System.out.println("--------------------");
			}
			response.setContentType("application/json;charset=UTF-8");
			List<Location> locations = new ArrayList<>();
			Location l = new Location();
			locations = l.consultAllLocations();
			String jsonResponse = buildJsonResponse("success", "Operação bem-sucedida",
					convertLocationsToJson(locations, e));
			System.out.println(jsonResponse);
			out.println(jsonResponse);
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			String jsonResponse = "{\"status\": \"error\", \"message\": \"Sem autorização\"}";
		    out.println(jsonResponse);
		    return; 
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		String token = request.getHeader("Authorization");
		PrintWriter out = response.getWriter();

		token = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
		
		if (token == null || token.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
		    String jsonResponse = "{\"status\": \"error\", \"message\": \"Sem autorização\"}";
		    out.println(jsonResponse);
		    return; 
		}
		
		StringBuilder jsonReceived = new StringBuilder();
	    String line;
	    try (BufferedReader reader = request.getReader()) {
	        while ((line = reader.readLine()) != null) {
	            jsonReceived.append(line);
	        }
	    }
	    
	    Gson gson = new Gson();
	    JsonObject jsonObject = gson.fromJson(jsonReceived.toString(), JsonObject.class);
	    String locationName = jsonObject.get("locationName").getAsString();
	    String postalCode = jsonObject.get("postalCode").getAsString();
	    String street = jsonObject.get("street").getAsString();
	    String neighborhood = jsonObject.get("neighborhood").getAsString();
	    String number = jsonObject.get("number").getAsString();
	    String city = jsonObject.get("city").getAsString();
	    String state = jsonObject.get("state").getAsString();
		String establishmentType = jsonObject.get("establishmentType").getAsString();
		
		Owner owner = udao.findOwnerByToken(token);
		
		if (dao.existLocation(postalCode, street, number, city, state)) {
			String jsonResponse = "{\"status\": \"error\", \"message\": \"O endereço informado já está cadastrado.\"}";
			out.println(jsonResponse);
			System.out.println("Local já existe.");
			return;
		}
		System.out.println("Informações do local:" + locationName + ", " + postalCode + ", " + street + ", "
				+ neighborhood + ", " + number + ", " + city + ", " + state + ", " + establishmentType);
		if (establishmentType.equalsIgnoreCase("restaurant")) {
			String cuisineType = jsonObject.get("cuisineType").getAsString();
			String operatingDays = jsonObject.get("operatingDays").getAsString();

			Restaurant restaurant = new Restaurant(street, neighborhood, city, state, locationName, postalCode, number,
					cuisineType, operatingDays);
			System.out.println(restaurant.getCep() + restaurant.getTypeOfCuisine());
			int id = dao.insertRestaurant(restaurant, owner);
			System.out.println(id);
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\", \"locationId\": "
					+ id + "}";
			out.println(jsonResponse);
		} else if (establishmentType.equalsIgnoreCase("event")) {
			String startDateStr = jsonObject.get("startDate").getAsString();
			String endDateStr = jsonObject.get("endDate").getAsString();
			String eventPrice = jsonObject.get("eventPrice").getAsString();

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
			String productType = jsonObject.get("productType").getAsString();

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
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		Location location = new Location();
		String token = request.getHeader("Authorization");
		
		token = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
			
		if (token == null || token.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
			String jsonResponse = "{\"status\": \"error\", \"message\": \"Sem autorização\"}";
			out.println(jsonResponse);
			return; 			
		}
		
		StringBuilder jsonReceived = new StringBuilder();
	    String line;
	    try (BufferedReader reader = request.getReader()) {
	        while ((line = reader.readLine()) != null) {
	            jsonReceived.append(line);
	        }
	    }
	    Gson gson = new Gson();
	    JsonObject jsonObject = gson.fromJson(jsonReceived.toString(), JsonObject.class);
	    String locationId = jsonObject.get("id").getAsString();
		if (location.deleteLocation(locationId)) {
			String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\"}";
			out.println(jsonResponse);
		} else {
			String jsonResponse = "{\"status\": \"failed\", \"message\": \"Operação mal-sucedida\"}";
			out.println(jsonResponse);
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		
		response.setContentType("application/json;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    
	    String token = request.getHeader("Authorization");
	    token = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
			
		if (token == null || token.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
			String jsonResponse = "{\"status\": \"error\", \"message\": \"Sem autorização\"}";
			out.println(jsonResponse);
			return; 			
		}
	    Owner owner = udao.findOwnerByToken(token);

	    if (owner == null) {
	        out.println("{\"status\": \"error\", \"message\": \"Usuário não autenticado.\"}");
	        return;
	    }

	    // 📌 Lendo o JSON recebido
	    BufferedReader reader = request.getReader();
	    StringBuilder jsonReceived = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        jsonReceived.append(line);
	    }

	    // 📌 Convertendo JSON para Objeto
	    Gson gson = new Gson();
	    JsonObject jsonObject = gson.fromJson(jsonReceived.toString(), JsonObject.class);

	    // 📌 Pegando os dados do JSON
	    String locationId = jsonObject.get("locationId").getAsString();
	    String locationName = jsonObject.get("placeName").getAsString();
	    String postalCode = jsonObject.get("cep").getAsString();
	    String street = jsonObject.get("publicPlace").getAsString();
	    String neighborhood = jsonObject.get("neighborhood").getAsString();
	    String number = jsonObject.get("number").getAsString();
	    String city = jsonObject.get("city").getAsString();
	    String state = jsonObject.get("uf").getAsString();
	    String establishmentType = jsonObject.get("establishmentType").getAsString();

	    System.out.println("Atualizando local: " + locationId);

	    boolean updated = false;

	    if ("restaurant".equalsIgnoreCase(establishmentType)) {
	        String cuisineType = jsonObject.get("typeOfCuisine").getAsString();
	        String operatingDays = jsonObject.get("operatingDays").getAsString();

	        Restaurant restaurant = new Restaurant(locationId, street, neighborhood, city, 0, 0, state, locationName, postalCode, number, cuisineType, operatingDays);
	        updated = dao.updateRestaurant(restaurant);

	    } else if ("event".equalsIgnoreCase(establishmentType)) {
	        String startDateStr = jsonObject.get("startDate").getAsString();
	        String endDateStr = jsonObject.get("endDate").getAsString();
	        String eventPrice = jsonObject.get("eventPrice").getAsString();

	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        Date startDate = null, endDate = null;

	        try {
	            startDate = formatter.parse(startDateStr);
	            endDate = formatter.parse(endDateStr);
	        } catch (ParseException e) {
	            e.printStackTrace();
	            out.println("{\"status\": \"error\", \"message\": \"Formato de data inválido.\"}");
	            return;
	        }

	        Event event = new Event(locationId, street, neighborhood, city, 0, 0, state, locationName, postalCode, number, startDate, endDate, eventPrice);
	        updated = dao.updateEvent(event);

	    } else if ("store".equalsIgnoreCase(establishmentType)) {
	        String productType = jsonObject.get("typeProduct").getAsString();

	        Store store = new Store(locationId, street, neighborhood, city, 0, 0, state, locationName, postalCode, number, productType);
	        updated = dao.updateStore(store);
	    } else {
	        out.println("{\"status\": \"error\", \"message\": \"Tipo de estabelecimento inválido.\"}");
	        return;
	    }

	    if (updated) {
	        out.println("{\"status\": \"success\", \"message\": \"Local atualizado com sucesso.\"}");
	    } else {
	        out.println("{\"status\": \"error\", \"message\": \"Erro ao atualizar o local.\"}");
	    }
	}


	private String buildJsonResponse(String status, String message, String locationsJson) {
		String jsonResponse = "{" + "\"status\":\"" + status + "\"," + "\"message\":\"" + message + "\","
				+ "\"locations\":" + locationsJson + "}";
		return jsonResponse;
	}

	public static String convertLocationsToJson(List<Location> locations, List<Evaluation> evaluations) {
		StringBuilder jsonBuilder = new StringBuilder("[");
		boolean first = true;
		
		
		
		for (Location location : locations) {
			if (!first) {
				jsonBuilder.append(",");
			} else {
				first = false;
			}
			
			boolean hasEvaluation = evaluations.stream()
	                .anyMatch(evaluation -> evaluation.getLocation().getId().equals(location.getId()));
			String locationEvaluation = hasEvaluation ? "true" : "false";
			
			double evaluatorNote = hasEvaluation ?
			        evaluations.stream()
	                .filter(evaluation -> evaluation.getLocation().getId().equals(location.getId()))
	                .findFirst()
	                .map(Evaluation::getNote)
	                .orElse(0.0) :
	        0.0;
			String evaluatorNoteFormatted = String.format("%.2f", evaluatorNote); 
			System.out.println("EvaluationNote:" + evaluatorNote);
			System.out.println("True:" + hasEvaluation);
			double note = location.getAcessibilityNote();
			int quantity = location.getQuantityOfEvaluation();
			String formattedNote = String.format("%.2f", note); 
			String quantityAsString = String.valueOf(quantity);
			jsonBuilder.append("{").append("\"id\":" + location.getId() + ",")
					.append("\"publicPlace\":\"" + escapeJsonString(location.getPublicPlace()) + "\",")
					.append("\"neighborhood\":\"" + escapeJsonString(location.getNeighborhood()) + "\",")
					.append("\"acessibilityNote\":\"" + escapeJsonString(formattedNote) + "\",")
					.append("\"locationEvaluation\":\"" + escapeJsonString(locationEvaluation) + "\",")
					.append("\"noteEvaluation\":\"" + escapeJsonString(evaluatorNoteFormatted) + "\",")
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
