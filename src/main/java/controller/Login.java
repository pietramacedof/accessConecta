package controller;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Evaluator;
import model.Owner;
import model.dao.UserDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	UserDAO dao = new UserDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuilder jsonReceived = new StringBuilder();
	    String line;
	    try (BufferedReader reader = request.getReader()) {
	        while ((line = reader.readLine()) != null) {
	            jsonReceived.append(line);
	        }
	    }
	    
	    Gson gson = new Gson();
	    JsonObject jsonObject = gson.fromJson(jsonReceived.toString(), JsonObject.class);
	    String email = jsonObject.get("email").getAsString();
	    String password = jsonObject.get("password").getAsString();

		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if (dao.validateCredentials(email, password)) {
			Integer type = dao.validadeType(email);
			System.out.println(type);
			if (type == 1) {
				Owner owner = new Owner();
				owner = owner.consultOwner(email);
				System.out.println(owner.getFirstName());
				String jsonResponse = "{\"token\": \"" + owner.getToken() + "\"}";
				out.println(jsonResponse);
			} else {
				Evaluator evaluator = new Evaluator();
				evaluator = evaluator.consultEvaluator(email);
				System.out.println(evaluator.getFirstName());
				String jsonResponseEvaluator = "{\"token\": \"" + evaluator.getToken() + "\"}";
				out.println(jsonResponseEvaluator);
			}

		} else {
			String jsonResponse = "{\"status\": \"error\", \"message\": \"Dados inválidos, tente novamente.\"}";
			out.println(jsonResponse);
		}
	}
}
