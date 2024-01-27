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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet implementation class toAlterPassword
 */
@WebServlet("/modifyPassword")
public class toAlterPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public toAlterPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		token = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		
		User user = new User();
		
		user = user.findUserByToken(token);
		
		System.out.println(user.getEmail() + user.getFirstName() + confirmPassword);
		
		try {
			if(user.toAlterPassword(user, confirmPassword)) {
				String jsonResponse = "{\"status\": \"success\", \"message\": \"Operação bem-sucedida.\"}";
				out.println(jsonResponse);
			}
			else {
				String jsonResponse = "{\"status\": \"error\", \"message\": \"Operação mal-sucedida.\"}";
				out.println(jsonResponse);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
