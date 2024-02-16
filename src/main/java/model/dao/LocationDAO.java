package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

import model.Event;
import model.Location;
import model.Owner;
import model.Restaurant;
import model.Store;

public class LocationDAO {

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

	public int insertRestaurant(Restaurant restaurant, Owner owner) {
		String sql = "INSERT INTO location (location_user_id, location_public_place, location_neighborhood, location_city, location_uf,"
				+ "location_place_name, location_cep, location_place_number, location_type, location_type_of_cuisine, location_operating_days) VALUES"
				+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			Connection conn = toConnect();
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, owner.getId());
			pstmt.setString(2, restaurant.getPublicPlace());
			pstmt.setString(3, restaurant.getNeighborhood());
			pstmt.setString(4, restaurant.getCity());
			pstmt.setString(5, restaurant.getUf());
			pstmt.setString(6, restaurant.getPlaceName());
			pstmt.setString(7, restaurant.getCep());
			pstmt.setString(8, restaurant.getNumber());
			pstmt.setString(9, "1");
			pstmt.setString(10, restaurant.getTypeOfCuisine());
			pstmt.setString(11, restaurant.getOperatingDays());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Erro ao inserir o restaurante.");
			}

			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1); // Retorna o ID gerado pelo banco de dados
			} else {
				throw new SQLException("Erro ao obter o ID gerado pelo banco de dados.");
			}
		} catch (SQLException e) {
			System.out.println(e);
			return -1; // Ou outro valor que indique falha
		}
	}

	public boolean deleteLocation(String id) {
		try (Connection conn = toConnect()) {
			String sql = "DELETE FROM location WHERE location_id = ?";

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, id);

				int rowsAffected = pstmt.executeUpdate();

				return rowsAffected > 0; // Retorna true se pelo menos uma linha foi afetada (excluída)
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Lida com exceções de SQL, substitua por um tratamento apropriado
			return false;
		}
	}

	public int insertEvent(Event event, Owner owner) {
		String sql = "INSERT INTO location (location_user_id, location_public_place, location_neighborhood, location_city, location_uf,"
				+ "location_place_name, location_cep, location_place_number, location_type, location_start_date, location_end_date, location_event_price) VALUES"
				+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			Connection conn = toConnect();
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, owner.getId());
			pstmt.setString(2, event.getPublicPlace());
			pstmt.setString(3, event.getNeighborhood());
			pstmt.setString(4, event.getCity());
			pstmt.setString(5, event.getUf());
			pstmt.setString(6, event.getPlaceName());
			pstmt.setString(7, event.getCep());
			pstmt.setString(8, event.getNumber());
			pstmt.setString(9, "2");
			pstmt.setDate(10, new java.sql.Date(event.getStartDate().getTime()));
			pstmt.setDate(11, new java.sql.Date(event.getEndDate().getTime()));
			if (event.getEventPrice().equalsIgnoreCase("free")) {
				pstmt.setString(12, "1");
			} else {
				pstmt.setString(12, "2");
			}
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Erro ao inserir o evento.");
			}
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1); // Retorna o ID gerado pelo banco de dados
			} else {
				throw new SQLException("Erro ao obter o ID gerado pelo banco de dados.");
			}
		} catch (SQLException e) {
			System.out.println(e);
			return -1;
		}
	}

	public int insertStore(Store store, Owner owner) {
		String sql = "INSERT INTO location (location_user_id, location_public_place, location_neighborhood, location_city, location_uf,"
				+ "location_place_name, location_cep, location_place_number, location_type, location_type_product) VALUES"
				+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			Connection conn = toConnect();
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, owner.getId());
			pstmt.setString(2, store.getPublicPlace());
			pstmt.setString(3, store.getNeighborhood());
			pstmt.setString(4, store.getCity());
			pstmt.setString(5, store.getUf());
			pstmt.setString(6, store.getPlaceName());
			pstmt.setString(7, store.getCep());
			pstmt.setString(8, store.getNumber());
			pstmt.setString(9, "3");
			pstmt.setString(10, store.getTypeProduct());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Erro ao inserir a loja.");
			}
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1); // Retorna o ID gerado pelo banco de dados
			} else {
				throw new SQLException("Erro ao obter o ID gerado pelo banco de dados.");
			}
		} catch (SQLException e) {
			System.out.println(e);
			return -1;
		}
	}

	public boolean hasLocation(Integer userId) {
		String sql = "SELECT COUNT(*) FROM location WHERE location_user_id = ?";
		try {
			Connection conn = toConnect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Trate a exceção apropriadamente em um ambiente real
		}
		return false;
	}

	public boolean existLocationAlter(String id, String postalCode, String street, String number, String city,
			String state) {
		String sql = "SELECT COUNT(*) FROM location WHERE location_city = ? AND location_uf = ? AND location_cep = ?"
				+ " AND location_place_number = ? AND location_public_place = ? AND location_id != ?";
		boolean locationExists = false;
		try (Connection conn = toConnect()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, city);
				stmt.setString(2, state);
				stmt.setString(3, postalCode);
				stmt.setString(4, number);
				stmt.setString(5, street);
				stmt.setString(6, id);

				try (ResultSet rs = stmt.executeQuery()) {
					rs.next(); // Move to the first row
					int count = rs.getInt(1); // Get the count from the first column
					locationExists = count >= 1;
				}
			}
		} catch (SQLException e) {
			// Handle SQL exceptions appropriately
			System.err.println("Error checking location existence: " + e.getMessage());
			// Consider logging the error or throwing a custom exception
		}

		return locationExists;
	}

	public boolean updateEvent(Event e) {
		String sql = "UPDATE location SET " + "location_city = ?, " + "location_uf = ?, " + "location_cep = ?, "
				+ "location_place_number = ?, " + "location_public_place = ?, " + "location_start_date = ?, "
				+ "location_end_date = ?, " + "location_place_name = ? " + "WHERE location_id = ?";

		try (Connection connection = toConnect();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			// Set the values for the placeholders in the SQL statement
			preparedStatement.setString(1, e.getCity());
			preparedStatement.setString(2, e.getUf());
			preparedStatement.setString(3, e.getCep());
			preparedStatement.setString(4, e.getNumber());
			preparedStatement.setString(5, e.getPublicPlace());
			preparedStatement.setDate(6, (Date) e.getStartDate());
			preparedStatement.setDate(7, (Date) e.getEndDate());
			preparedStatement.setString(8, e.getPlaceName());
			preparedStatement.setString(9, e.getId());

			int rowsUpdated = preparedStatement.executeUpdate();

			return rowsUpdated > 0;

		} catch (SQLException ex) {
			ex.printStackTrace(); // Handle the exception appropriately based on your application's requirements
			return false;
		}
	}

	public boolean updateStore(Store s) {
		String sql = "UPDATE location SET " + "location_city = ?, " + "location_uf = ?, " + "location_cep = ?, "
				+ "location_place_number = ?, " + "location_public_place = ?, " + "location_type_product = ?, "
				+ "location_place_name = ? " + "WHERE location_id = ?";

		try (Connection connection = toConnect();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			// Set the values for the placeholders in the SQL statement
			preparedStatement.setString(1, s.getCity());
			preparedStatement.setString(2, s.getUf());
			preparedStatement.setString(3, s.getCep());
			preparedStatement.setString(4, s.getNumber());
			preparedStatement.setString(5, s.getPublicPlace());
			preparedStatement.setString(6, s.getTypeProduct());
			preparedStatement.setString(7, s.getPlaceName());
			preparedStatement.setString(8, s.getId());

			int rowsUpdated = preparedStatement.executeUpdate();

			return rowsUpdated > 0;

		} catch (SQLException ex) {
			ex.printStackTrace(); // Handle the exception appropriately based on your application's requirements
			return false;
		}
	}

	public boolean updateRestaurant(Restaurant r) {
		String sql = "UPDATE location SET " + "location_city = ?, " + "location_uf = ?, " + "location_cep = ?, "
				+ "location_place_number = ?, " + "location_public_place = ?, " + "location_type_of_cuisine = ?, "
				+ "location_operating_days = ?, " + // Add a comma here
				"location_place_name = ? " + "WHERE location_id = ?";

		try (Connection connection = toConnect();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			// Set the values for the placeholders in the SQL statement
			preparedStatement.setString(1, r.getCity());
			preparedStatement.setString(2, r.getUf());
			preparedStatement.setString(3, r.getCep());
			preparedStatement.setString(4, r.getNumber());
			preparedStatement.setString(5, r.getPublicPlace());
			preparedStatement.setString(6, r.getTypeOfCuisine());
			preparedStatement.setString(7, r.getOperatingDays());
			preparedStatement.setString(8, r.getPlaceName());
			preparedStatement.setString(9, r.getId());

			int rowsUpdated = preparedStatement.executeUpdate();

			return rowsUpdated > 0;

		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception appropriately based on your application's requirements
			return false;
		}
	}

	public boolean existLocation(String postalCode, String street, String number, String city, String state) {
		String sql = "SELECT COUNT(*) FROM location WHERE location_city = ? AND location_uf = ? AND location_cep = ? AND location_place_number = ? "
				+ "AND location_public_place = ?";
		boolean locationExists = false;
		try (Connection conn = toConnect()) {
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, city);
				stmt.setString(2, state);
				stmt.setString(3, postalCode);
				stmt.setString(4, number);
				stmt.setString(5, street);

				try (ResultSet rs = stmt.executeQuery()) {
					rs.next(); // Move to the first row
					int count = rs.getInt(1); // Get the count from the first column
					locationExists = count >= 1;
				}
			}
		} catch (SQLException e) {
			// Handle SQL exceptions appropriately
			System.err.println("Error checking location existence: " + e.getMessage());
			// Consider logging the error or throwing a custom exception
		}

		return locationExists;
	}

	public List<Location> consultAllLocations() {
		List<Location> locations = new ArrayList<>();
		Connection conn = toConnect();
		try {
			String query = "SELECT * FROM location";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						Location location = createLocationFromResultSet(resultSet);
						locations.add(location);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return locations;
	}

	public List<Location> consultLocations(int ownerId) {
		List<Location> locations = new ArrayList<>();
		Connection conn = toConnect();
		try {
			String query = "SELECT * FROM location WHERE location_user_id = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, ownerId);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						Location location = createLocationFromResultSet(resultSet);
						locations.add(location);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Trate a exceção de acordo com suas necessidades
		}

		return locations;
	}

	private Location createLocationFromResultSet(ResultSet resultSet) throws SQLException {
		String type = resultSet.getString("location_type"); // Supondo que há uma coluna 'type' que indica o tipo de
															// local

		Location location;

		switch (type) {
		case "1":
			location = createRestaurantFromResultSet(resultSet);
			break;
		case "2":
			location = createEventFromResultSet(resultSet);
			break;
		case "3":
			location = createStoreFromResultSet(resultSet);
			break;
		default:
			throw new SQLException("Tipo de local desconhecido: " + type);
		}

		return location;
	}

	private Restaurant createRestaurantFromResultSet(ResultSet resultSet) throws SQLException {
		// Extrair atributos específicos de Restaurant do ResultSet
		String typeOfCuisine = resultSet.getString("location_type_of_cuisine");
		String operatingDays = resultSet.getString("location_operating_days");

		// Criar e retornar um objeto Restaurant
		return new Restaurant(resultSet.getString("location_id"), resultSet.getString("location_public_place"),
				resultSet.getString("location_neighborhood"), resultSet.getString("location_city"),
				resultSet.getDouble("location_acessibility_note"),
				resultSet.getInt("location_quantity_of_evaluation"),
				resultSet.getString("location_uf"), resultSet.getString("location_place_name"),
				resultSet.getString("location_cep"), resultSet.getString("location_place_number"), typeOfCuisine,
				operatingDays);
	}

	private Event createEventFromResultSet(ResultSet resultSet) throws SQLException {
		// Extrair atributos específicos de Event do ResultSet
		// Você pode precisar converter os valores de data do banco de dados para
		// objetos Date aqui
		Date startDate = resultSet.getDate("location_start_date");
		Date endDate = resultSet.getDate("location_end_date");
		String eventPrice = resultSet.getString("location_event_price");

		// Criar e retornar um objeto Event
		return new Event(resultSet.getString("location_id"), resultSet.getString("location_public_place"),
				resultSet.getString("location_neighborhood"), resultSet.getString("location_city"),
				resultSet.getDouble("location_acessibility_note"), resultSet.getInt("location_quantity_of_evaluation"),
				resultSet.getString("location_uf"), resultSet.getString("location_place_name"),
				resultSet.getString("location_cep"), resultSet.getString("location_place_number"), startDate, endDate,
				eventPrice);
	}

	private Store createStoreFromResultSet(ResultSet resultSet) throws SQLException {
		// Extrair atributos específicos de Store do ResultSet
		String typeProduct = resultSet.getString("location_type_product");

		// Criar e retornar um objeto Store
		return new Store(resultSet.getString("location_id"), resultSet.getString("location_public_place"),
				resultSet.getString("location_neighborhood"), resultSet.getString("location_city"),
				resultSet.getDouble("location_acessibility_note"), resultSet.getInt("location_quantity_of_evaluation"),
				resultSet.getString("location_uf"), resultSet.getString("location_place_name"),
				resultSet.getString("location_cep"), resultSet.getString("location_place_number"), typeProduct);
	}

	public Location consultLocationById(String id) throws SQLException {
		Location location = null;
		Connection connection = toConnect();
		try (PreparedStatement statement = connection
				.prepareStatement("SELECT * FROM location WHERE location_id = ?")) {
			statement.setString(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// Aqui você usaria o método createLocationFromResultSet para construir o objeto
					// Location
					// com base nos dados obtidos do ResultSet.
					location = createLocationFromResultSet(resultSet);
				}
			}
		}
		return location;
	}

	public boolean updateNoteById(Location location, double total, double convertedNote) {
		String sql = "UPDATE location " + "SET location_acessibility_note = ?, location_quantity_of_evaluation = ?,"
				+ "location_total_rating = ?"
				+ "WHERE location_id = ?";
		Connection connection = toConnect();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setDouble(1, location.getAcessibilityNote());
			preparedStatement.setInt(2, location.getQuantityOfEvaluation());
			preparedStatement.setDouble(3, (total+convertedNote));
			preparedStatement.setString(4, location.getId());

			int rowsUpdated = preparedStatement.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void insertEvaluation(Integer userId, String locationId, double rating) throws SQLException {
		  String sql = "INSERT INTO evaluation (evaluation_user_id, evaluation_location_id, evaluation_rating) VALUES (?, ?, ?)";
		  try (Connection connection = toConnect();
			   PreparedStatement statement = connection.prepareStatement(sql)) {
		    statement.setInt(1, userId);
		    statement.setString(2, locationId);
		    statement.setDouble(3, rating);
		    statement.executeUpdate();
		  }
	}
	
	public double getLocationTotalRatingById(String id) {
		
		  Connection connection = toConnect();

		  PreparedStatement statement = null;
		  ResultSet resultSet = null;

		  try {
		    
		    String sql = "SELECT location_total_rating FROM location WHERE location_id = ?";

		   
		    statement = connection.prepareStatement(sql);
		    statement.setString(1, id);
		    resultSet = statement.executeQuery();

		    // Verifique se algum registro foi encontrado
		    if (resultSet.next()) {
		      
		      return resultSet.getDouble("location_total_rating");
		    } else {
		     
		      return 0.0;
		    }
		  } catch (SQLException e) {
		    
		    e.printStackTrace();
		    return 0.0; // Valor padrão em caso de erro
		  } finally {
		    
		    try {
		      if (resultSet != null) resultSet.close();
		      if (statement != null) statement.close();
		      if (connection != null) connection.close();
		    } catch (SQLException e) {
		      
		      e.printStackTrace();
		    }
		  }
		}

}
