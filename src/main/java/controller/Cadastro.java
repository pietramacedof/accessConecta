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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet implementation class Cadastro
 */
@WebServlet("/cadastro")
public class Cadastro extends HttpServlet {
	private static final long serialVersionUID = 1L;

	UserDAO dao = new UserDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Cadastro() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 *   @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String userType = request.getParameter("userType");
		System.out.println("Página acessada." + firstName);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if (dao.hasEmail(email)) {
			String jsonResponse = "{\"status\": \"error\", \"message\": \"O e-mail informado já está cadastrado.\"}";
			out.println(jsonResponse);
			System.out.println("Email já existe.");
			return;
		}
			if ("owner".equals(userType)) {

				String dateOfBirthString = request.getParameter("dateOfBirth");
				System.out.println("Usuário do tipo owner: " + password);
				Date dateOfBirth = null;

				try {

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date utilDate = dateFormat.parse(dateOfBirthString); // Obtém java.util.Date
					dateOfBirth = new java.sql.Date(utilDate.getTime());
					System.out.println("Usuário do tipo owner: " + dateOfBirthString);
					Owner owner = new Owner(firstName, lastName, email, password, dateOfBirth);
					dao.createOwner(owner);
					String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\"}";
					out.println(jsonResponse);

				} catch (ParseException e) {
					e.printStackTrace(); // ou trate a exceção de outra maneira
				}

			} else {
				String typeOfDisability = request.getParameter("typeOfDisability");
				System.out.println("Usuário do tipo evaluator com tipo de deficiência: " + typeOfDisability);
				Evaluator evaluator = new Evaluator(firstName, lastName, email, password, typeOfDisability);
				dao.createEvaluator(evaluator);
				String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\"}";
				out.println(jsonResponse);
			}

	}

}
