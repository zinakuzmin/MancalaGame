package server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Game;
import model.User;

public class DBHandler {

	/**
	 * {@code connection} - the connection with the sql database server.
	 */
	private Connection theConnection;

	/**
	 * Create an Handler type DBHandler.
	 */
	public DBHandler() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			theConnection = DriverManager.getConnection(
					"jdbc:mysql://localhost/mancaladb?useSSL=false", "scott",
					"tiger");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized User insertUser(User theUser) {
		String theQuery = " insert into Users (userID, userEmail, userName, password, bestScore)"
				+ " values (?, ?, ?, ?, ?)";
		
		try {
			
			if (!isUserExist(theUser)){
			
				PreparedStatement preparedStmt = theConnection.prepareStatement(
						theQuery, Statement.RETURN_GENERATED_KEYS);
				preparedStmt.setInt(1, 0);
				preparedStmt.setString(2, theUser.getUserEmail().toLowerCase());
				preparedStmt.setString(3, theUser.getUserName().toLowerCase());
				preparedStmt.setString(4, theUser.getPassword());
				preparedStmt.setDouble(5, theUser.getBestScore());
	
				preparedStmt.execute();
				ResultSet rs = preparedStmt.getGeneratedKeys();
				rs.next();
				int auto_id = rs.getInt(1);
				theUser.setUserID(auto_id);
				System.out.println(theUser);
				preparedStmt.close();
				return theUser;
			}
			
			else{
				theUser.setUserID(0);
				return theUser;
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theUser;

	}
	
	public synchronized Game insertGame(Game theGame) {
		String theQuery = " insert into Games (gameID, user1ID, user2ID, isCompleted, winnerID, winnerScore)"
				+ " values (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement preparedStmt = theConnection.prepareStatement(
					theQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStmt.setInt(1, 0);
			preparedStmt.setInt(2,  theGame.getUser1ID());
			preparedStmt.setInt(3,  theGame.getUser2ID());
			preparedStmt.setBoolean(4,  theGame.isCompleted());
			preparedStmt.setInt(5,  theGame.getWinnerID());
			preparedStmt.setInt(6,  theGame.getWinnerScore());

			preparedStmt.execute();
			ResultSet rs = preparedStmt.getGeneratedKeys();
			rs.next();
			int auto_id = rs.getInt(1);
			theGame.setGameID(auto_id);

			preparedStmt.close();
			return theGame;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theGame;

	}
	
	public synchronized boolean isUserExist(User theUser) {
//		String theQuery = " select  * from Users where userId = '"
//				+ theUser.getUserID();
		String theQuery = " select  * from Users where userEmail = '"
				+ theUser.getUserEmail()+ "'";
		ResultSet res = executeQuery(theQuery);
		try {
			if (res.first())
				return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return false;
	}
	
	
	/**
	 * @param userID
	 *            - unique id of the user.
	 * @return return a ResultSet that Contains a user with same id 
	 */
	public synchronized ResultSet getUserByID(int userID) {

		String theQuery = "select * from Users where userID = "
				+ userID;
		return executeQuery(theQuery);

	}
	
	/**
	 * Gets the user by ID as object.
	 *
	 * @param userID the user ID
	 * @return the user by ID as object
	 */
	public synchronized User getUserByIDAsObject(int userID) {

		return (User) convertResultSetToObject(getUserByID(userID), "User");

	}
	
	public synchronized ResultSet getUserByEmail(String email){
		String theQuery = "select * from Users where userEmail = '"
				+ email + "'";
		return executeQuery(theQuery);
	}
	
	
	public synchronized User getUserByEmailAsObject(String email) {

		return (User) convertResultSetToObject(getUserByEmail(email), "User");

	}
	
	
	
	
	/**
	 * Gets the game by ID.
	 *
	 * @param userID the user ID
	 * @return the game by ID
	 */
	public synchronized ResultSet getGameByID(int gameID) {

		String theQuery = "select * from Games where gameID = "
				+ gameID;
		return executeQuery(theQuery);

	}
	
	
	/**
	 * Gets the game by ID as object.
	 *
	 * @param gameID the game ID
	 * @return the game by ID as object
	 */
	public synchronized Game getGameByIDAsObject(int gameID) {

		return (Game) convertResultSetToObject(getGameByID(gameID), "Game");

	}
	
	
	/**
	 * Gets the all users.
	 *
	 * @return the all users
	 */
	public synchronized ResultSet getAllUsers() {
		String theQuery = "select * from Users";
		return executeQuery(theQuery);
	}
	
	public synchronized ResultSet getAllUsersOrderByScore() {
		String theQuery = "select userName, bestScore from Users order by bestScore";
		return executeQuery(theQuery);
	}
	
	/**
	 * Gets the all users as array.
	 *
	 * @return the all users as array
	 */
	@SuppressWarnings("unchecked")
	public synchronized ArrayList<User> getAllUsersAsArray() {
		
		return (ArrayList<User>) convertResultSetToArraylist(getAllUsers(), "User");
	}
	
	/**
	 * Gets the all games.
	 *
	 * @return the all games
	 */
	public synchronized ResultSet getAllGames() {
		String theQuery = "select * from Games";
		return executeQuery(theQuery);
	}
	
	/**
	 * Gets the all games as array.
	 *
	 * @return the all games as array
	 */
	@SuppressWarnings("unchecked")
	public synchronized ArrayList<User> getAllGamesAsArray() {
		
		return  (ArrayList<User>) convertResultSetToArraylist(getAllGames(), "Game");
	}
	
	
	
	
	/**
	 * @param theQuery
	 *            - a query that expects to return a result set.
	 * @return return a ResultSet according to the query. else return null.
	 */
	public synchronized ResultSet executeQuery(String theQuery) {
		try {
			Statement stat = theConnection.createStatement();
			System.out.println(theQuery);
			return stat.executeQuery(theQuery);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param theQuery
	 *            - a query that doesn't expects to return a result set.
	 */
	public synchronized void executeSqlQuery(String theQuery) {
		try {
			Statement stat = theConnection.createStatement();
			stat.execute(theQuery);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	
	
	/**
	 * Convert result set of user , game to array of objects
	 * 
	 * @param result
	 * @param classType
	 * @return ArrayList<?>
	 */
	public synchronized ArrayList<?> convertResultSetToArraylist(
			ResultSet result, String classType) {



		if (classType.equals("Game")) {
			if (result != null) {
				ArrayList<Game> games = new ArrayList<>();
				if (result != null) {
					try {
						while (result.next()) {
							Game game;

							game = new Game(result.getInt("gameID"),
									result.getInt("user1ID"),
									result.getInt("user2ID"),
									result.getBoolean("isCompleted"),
									result.getInt("winnerID"),
									result.getInt("winnerScore"));
							games.add(game);

						}
						return games;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}


		else if (classType.equals("User")) {
			ArrayList<User> users = new ArrayList<>();
			try {
				if (!result.next()) {
					System.out.println("no data found");
				} else {
					do {
						User user;

						user = new User(result.getInt("userID"),
								result.getString("userEmail"),
								result.getString("userName"),
								result.getString("password"),
								result.getInt("bestScore"));

						users.add(user);

					} while (result.next());
					return users;
				}

			}

			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
		return null;
	}

	/**
	 * Convert result set to Object
	 * 
	 * @param result
	 * @param objectType
	 * @return Object (User, Game)
	 */
	public synchronized Object convertResultSetToObject(ResultSet result,
			String objectType) {
		@SuppressWarnings("unchecked")
		ArrayList<Object> object = (ArrayList<Object>) convertResultSetToArraylist(
				result, objectType);

		if (object != null)
			return object.get(0);

		return object;
	}



}
