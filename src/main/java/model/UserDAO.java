package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class UserDAO {

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String JDBC_URL = "jdbc:mysql://localhost:3306/accessconecta";
	private String user = "root";
	private String password = "@PG3005d";

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

	public void testeconexao() {
		try {
			Connection con = toConnect();
			System.out.println(con);
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Integer validadeType(String email) {
		Integer userType = null;
		String sql = "SELECT user_type FROM user_info WHERE email = ?";
		Connection conn = toConnect();
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, email);

			
			try (ResultSet resultSet = statement.executeQuery()) {
				
				if (resultSet.next()) {
					userType = resultSet.getInt("user_type");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente real
		}

		return userType;
	}
	
    public Owner consultOwner(String email) {
        Owner owner = new Owner();
        String token = generateToken(email);
        token = hashPassword(token);
        System.out.println("Token :" + token);
        setarToken(email,token);
        try (Connection conn = toConnect()) {
            String sql = "SELECT f_name, l_name, email, date_of_birth, user_type FROM user_info WHERE email = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, email);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        owner.setFirstName(resultSet.getString("f_name"));
                        owner.setLastName(resultSet.getString("l_name"));
                        owner.setEmail(resultSet.getString("email"));
                        owner.setUserType(resultSet.getInt("user_type"));
                        owner.setToken(token);
                        java.sql.Date sqlDate = resultSet.getDate("date_of_birth");
                        if (sqlDate != null) {
                            owner.setDateOfBirth(new Date(sqlDate.getTime()));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente real
        }

        return owner;
    }
    
    public static String generateToken(String email) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // Concatenate email and timestamp with a delimiter
        String token = email + "-" + timestamp;

        return token;
    }
    
    public Evaluator consultEvaluator(String email) {
        Evaluator evaluator = new Evaluator(); // Assumindo que você tenha uma classe Evaluator
        String token = generateToken(email);
        token = hashPassword(token);
        System.out.println(token);
        setarToken(email,token);
        try (Connection conn = toConnect()) {
            String sql = "SELECT f_name, l_name, email, type_of_disability, user_type FROM user_info WHERE email = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, email);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        evaluator.setFirstName(resultSet.getString("f_name"));
                        evaluator.setLastName(resultSet.getString("l_name"));
                        evaluator.setEmail(resultSet.getString("email"));
                        evaluator.setUserType(resultSet.getInt("user_type"));
                        evaluator.setTypeOfDisability(resultSet.getString("type_of_disability"));
                        evaluator.setToken(token);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente real
        }
        return evaluator;
    }


	public boolean validateCredentials(String email, String password) {
		password = hashPassword(password);
		System.out.println(password);
		try {
			Connection conn = toConnect();
			String sql = "SELECT * FROM user_info WHERE email = ? AND watchword = ?";
			try (PreparedStatement statement = conn.prepareStatement(sql)) {
				statement.setString(1, email);
				statement.setString(2, password);

				try (ResultSet resultSet = statement.executeQuery()) {
					return resultSet.next(); // Retorna true se houver uma correspondência
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente real
			return false;
		}
	}

	public boolean hasEmail(String email) {
		String sql = "SELECT email FROM user_info WHERE email = ?";
		try (Connection conn = toConnect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
    public boolean setarToken(String email, String token) {
        try (Connection conn = toConnect()) {
            String sql = "UPDATE user_info SET token = ? WHERE email = ?";
            
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, token);
                statement.setString(2, email);

                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente real
        }
        return false;
    }

	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	

	public void createOwner(Owner owner) {
		String sql = "INSERT INTO user_info (f_Name, l_Name, email, watchword, date_of_birth, user_type) VALUES (?, ?, ?, ?, ?, ?)";
		owner.setPassword(hashPassword(owner.getPassword()));
		System.out.println(owner.getPassword());
		System.out.println("oi");
		try {
			Connection conn = toConnect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, owner.getFirstName());
			pstmt.setString(2, owner.getLastName());
			pstmt.setString(3, owner.getEmail());
			pstmt.setString(4, owner.getPassword());
			pstmt.setDate(5, (Date) owner.getDateOfBirth());
			pstmt.setString(6, "1");

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Erro ao inserir o proprietário.");
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void createEvaluator(Evaluator evaluator) {
		String sql = "INSERT INTO user_info (f_Name, l_Name, email, watchword, type_of_disability, user_type) VALUES (?, ?, ?, ?, ?, ?)";
		evaluator.setPassword(hashPassword(evaluator.getPassword()));
		System.out.println(evaluator.getPassword());
		System.out.println("oi");
		try {
			Connection conn = toConnect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, evaluator.getFirstName());
			pstmt.setString(2, evaluator.getLastName());
			pstmt.setString(3, evaluator.getEmail());
			pstmt.setString(4, evaluator.getPassword());
			pstmt.setString(5, evaluator.getTypeOfDisability());
			pstmt.setString(6, "0");

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Erro ao inserir o avaliador.");
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
