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
import model.User;
import model.UserDAO;
import model.dao.LocationDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/hasLocation")
public class HasLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserDAO dao = new UserDAO();
	LocationDAO ldao = new LocationDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HasLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String noteStr = request.getParameter("note");
		double note = Double.parseDouble(noteStr);
		String id = request.getParameter("id");
		String token = request.getParameter("token");
		User u = new User();
		u = u.findUserByToken(token);
		System.out.println(u.getId() + u.getFirstName());
		Location location = new Location();
		try {
			if (location.checkerNote(note)) {
				location = location.evaluateLocation(id, note, u);
				double accessibilityNote = location.getAcessibilityNote();
				String jsonResponse = "{ \"status\": \"success\", \"message\": \"Operação bem-sucedida.\", \"accessibilityNote\": \""
						+ accessibilityNote + "\" }";
				out.println(jsonResponse);
			} else {
				String jsonResponse = "{\"status\": \"error\", \"message\": \"Nota inválida.\"}";
				out.println(jsonResponse);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Recupera o token do cabeçalho de autorização
		String authHeader = request.getHeader("Authorization");
		System.out.println("Validação Local");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7); // Remova "Bearer " do início do token
			System.out.println("Validação Local");
			// Agora você pode usar o token para verificar se o usuário tem algum local
			// cadastrado
			Owner owner = dao.findOwnerByToken(token);
			if (owner != null && ldao.hasLocation(owner.getId())) {
				Location l = new Location();
				owner.setLocations(l.consultLocationsByOwner(owner.getId()));
				String jsonResponse = buildJsonResponse("success", "Usuário possui locais cadastrados",
						convertLocationsToJson(owner.getLocations()));
				System.out.println(jsonResponse);
				out.println(jsonResponse);
			} else {
				// Se o usuário não tem nenhum local cadastrado, envia uma resposta com status
				// 404

				String jsonResponse = "{\"hasLocation\": \"error\", \"message\": \"Operação bem-sucedida\"}";
				System.out.println(jsonResponse);
				out.println(jsonResponse);
			}
		} else {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			PrintWriter writer = response.getWriter();
			writer.write("Autorização inválida.");
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
			String formattedNote = String.format("%.2f", note); // Format to 2 decimal places
			int quantity = location.getQuantityOfEvaluation();
			String quantityAsString = String.valueOf(quantity);
			jsonBuilder.append("{").append("\"id\":" + location.getId() + ",")
					.append("\"publicPlace\":\"" + escapeJsonString(location.getPublicPlace()) + "\",")
					.append("\"acessibilityNote\":\"" + escapeJsonString(formattedNote) + "\",")
					.append("\"neighborhood\":\"" + escapeJsonString(location.getNeighborhood()) + "\",")
					.append("\"city\":\"" + escapeJsonString(location.getCity()) + "\",")
					.append("\"uf\":\"" + escapeJsonString(location.getUf()) + "\",")
					.append("\"quantityOfEvaluation\":\"" + escapeJsonString(quantityAsString) + "\",")
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

}
