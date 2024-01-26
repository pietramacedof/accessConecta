package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Location;
import model.Owner;

import java.io.IOException;
import java.io.PrintWriter;

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
	}

}
