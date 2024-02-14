package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Owner;
import model.User;
import model.UserDAO;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class IsLogged
 */
@WebServlet("/isLogged")
public class IsLogged extends HttpServlet {
	private static final long serialVersionUID = 1L;
    UserDAO dao = new UserDAO();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsLogged() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = authHeader.substring(7);
		User user = new User();
		user = user.findUserByToken(token);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		System.out.println("Tipo:" + user.getUserType());
		if(user != null) {
			if(user.getUserType() == 1) {
				System.out.println("Entrou no owner");
				String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\", \"userType\": " + user.getUserType() + "}";
				System.out.println(jsonResponse);
				out.println(jsonResponse);
			}
			else {
				System.out.println("Entrou no evaluator");
				String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida\", \"userType\": " + user.getUserType() + "}";
				System.out.println(jsonResponse);
				out.println(jsonResponse);
			}
			
		} else {
			String jsonResponse = "{\"status\": \"error\", \"message\": \"Operação mal-sucedida\"}";
			System.out.println(jsonResponse);
			out.println(jsonResponse);
		}
	}
}
