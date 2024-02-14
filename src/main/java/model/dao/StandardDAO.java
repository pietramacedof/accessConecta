package model.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Questions;
import model.Standard;

public class StandardDAO {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String JDBC_URL = "jdbc:mysql://localhost:3306/accessconecta";
    private String user = "root";
    private String password = "@PG3005d";
    
    public StandardDAO() {
		super();
	}
    
    private Connection toConnect() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(JDBC_URL, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

    public List<Standard> getStandards() {
        List<Standard> standards = new ArrayList<>();
        try (Connection con = toConnect();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT s.standard_name, s.standard_id " +
                 "FROM standard s")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String standardName = rs.getString("standard_name");
                String standardId = rs.getString("standard_id");
                Standard standard = new Standard(standardName, standardId);
                standards.add(standard);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return standards;
    }
    
    public List<Questions> getQuestionsByStandard(String standardId) {
        List<Questions> questions = new ArrayList<>();
        try (Connection con = toConnect();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT q.* " +
                 "FROM questions q " +
                 "WHERE q.standard_id = ?")) {

            ps.setString(1, standardId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Questions question = new Questions();
                question.setId(rs.getString("question_id"));
                question.setQuestion(rs.getString("question_text"));
                // Adicione outros campos conforme necessário
                questions.add(question);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return questions;
    }
    
}
