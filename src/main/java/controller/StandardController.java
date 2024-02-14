package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Questions;
import model.Standard;

/**
 * Servlet implementation class StandardController
 */
@WebServlet("/standard")
public class StandardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StandardController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json;charset=UTF-8");

		
		PrintWriter out = response.getWriter();
		Standard standar = new Standard();
		List<Standard> s = new ArrayList<>();
		s.addAll(standar.consultStandard());
		for (Standard currentStandard : s) {
		    List<Questions> questions = standar.getQuestionsByStandard(currentStandard);
		    currentStandard.addQuestions(questions);
		}
		for (Object item : s) {
		    System.out.println(item);
		}
		
		String jsonResponse = serializeStandardsToJson(s);
	   
	    
	 
	    out.print(jsonResponse);
	    
		
		
	}
	
	public String serializeStandardsToJson(List<Standard> standards) {
	    StringBuilder jsonBuilder = new StringBuilder();
	    jsonBuilder.append("[");
	    for (int i =  0; i < standards.size(); i++) {
	        Standard standard = standards.get(i);
	        jsonBuilder.append("{")
	                   .append("\"standard_name\":\"").append(standard.getName()).append("\",")
	                   .append("\"standard_id\":").append(standard.getId()).append(",")
	                   .append("\"questions\":[");
	        for (int j =  0; j < standard.getQuestions().size(); j++) {
	            Questions question = standard.getQuestions().get(j);
	            jsonBuilder.append("{")
	                       .append("\"question_id\":").append(question.getId()).append(",")
	                       .append("\"question_text\":\"").append(question.getQuestion()).append("\"")
	                       .append("}");
	            if (j < standard.getQuestions().size() -  1) {
	                jsonBuilder.append(",");
	            }
	        }
	        jsonBuilder.append("]}");
	        if (i < standards.size() -  1) {
	            jsonBuilder.append(",");
	        }
	    }
	    jsonBuilder.append("]");
	    return jsonBuilder.toString();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
