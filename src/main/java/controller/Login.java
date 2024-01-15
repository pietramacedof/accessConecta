package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Evaluator;
import model.Owner;
import model.UserDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

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

		String username = request.getParameter("email");
		String password = request.getParameter("password");

		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();

		System.out.println("oiiiiiiiii");
		System.out.println(username);

		// Lógica de autenticação (substitua isso pela lógica real)
		if (dao.validateCredentials(username, password)) {
			Integer type = dao.validadeType(username);
			System.out.println(type);
			if (type == 1) {
				Owner owner = new Owner();
				owner = dao.consultOwner(username);
				System.out.println(owner.getFirstName());
				String jsonResponse = "{"
				        + "\"status\": \"success\","
				        + "\"message\": \"Operação bem-sucedida\","
				        + "\"firstName\": \"" + owner.getFirstName() + "\","
				        + "\"lastName\": \"" + owner.getLastName() + "\","
				        + "\"email\": \"" + owner.getEmail() + "\","
				        + "\"dateOfBirth\": \"" + owner.getDateOfBirth() + "\","
				        + "\"userType\": \"" + owner.getUserType() + "\","
				        + "\"token\": \"" + owner.getToken() + "\""
				        + "}";
				out.println(jsonResponse);
			} else {
				Evaluator evaluator = new Evaluator();
				evaluator = dao.consultEvaluator(username);
				System.out.println(evaluator.getFirstName());
				String jsonResponseEvaluator = "{"
						+ "\"status\": \"success\","
						+ "\"message\": \"Operação bem-sucedida\","
						+ "\"firstName\": \"" + evaluator.getFirstName() + "\","
						+ "\"lastName\": \"" + evaluator.getLastName() + "\","
						+ "\"email\": \"" + evaluator.getEmail() + "\","
						+ "\"typeOfDisability\": \"" + evaluator.getTypeOfDisability() + "\","
						+ "\"userType\": \"" + evaluator.getUserType() + "\","
						+ "\"token\": \"" + evaluator.getToken() + "\"" // Token adicionado aqui
						+ "}";
				out.println(jsonResponseEvaluator);
			}

		} else {
			String jsonResponse = "{\"status\": \"error\", \"message\": \"Dados inválidos, tente novamente.\"}";
			out.println(jsonResponse);
			System.out.println("não existe");
		}

	}

}
