package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import protocol.Message;
import protocol.ServerConnectedClientsResponse;
import protocol.ServerMakeMoveMsg;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Game;
import model.User;
import runner.GlobalParams;
import server.db.DBHandler;
import server.utils.Validator;
import server.view.ServerUI;

public class MancalaServerController {
	private DBHandler db;
	private Validator validator;
	private ArrayList<Client> onlineClients;
	private Thread clientsListenerThread;
	private ClientListener clientListenerTask;
	private ArrayList<GameController> runningGames;

	public MancalaServerController(Stage stage) {
		onlineClients = new ArrayList<Client>();
		runningGames = new ArrayList<>();
		db = new DBHandler();
		validator = new Validator();
		clientListenerTask = new ClientListener(this);
		clientsListenerThread = new Thread(clientListenerTask);
		clientsListenerThread.start();
		// try {
		// new ServerUI(new TextArea()).start(new Stage());
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	//
	// public MancalaServerController(Stage stage) {
	// // TODO Auto-generated constructor stub
	// }

	public ArrayList<Client> getOnlineClients() {
		return onlineClients;
	}

	public synchronized ActionResult addOnlineClient(Client client) {
		if (onlineClients.size() < GlobalParams.MAX_NUMBER_OF_CONNECTED_CLIENTS) {
			onlineClients.add(client);
			System.out.println("Added new client " + onlineClients.size());
			updateClientsWithOpponentList();
			return new ActionResult(true, null);
		} else {
			return new ActionResult(false,
					"Exceeding number of allowed clients");
		}
	}

//	public synchronized Map<String, String> generateOpponentsListExcludingAskingClient(
//			String clientID, int userID) {
//		Map<String, String> opponents = new HashMap<String, String>();
//		for (Client client : onlineClients) {
//			if (!client.getClientId().equals(clientID)
//					&& client.getUserID() != userID) {
//				String username = db.getUserByIDAsObject(userID).getUserName();
//				opponents.put(client.getClientId(), username);
//
//			}
//		}
//		return opponents;
//	}

	public synchronized Map<String, String> generateOpponentsList() {
		Map<String, String> opponents = new HashMap<String, String>();
		for (Client client : onlineClients) {
			if (client.getUserID() != 0){
				String username = db.getUserByIDAsObject(client.getUserID()).getUserName();
				opponents.put(client.getClientId(), username);
				
			}

		}
		return opponents;
	}
	
	
	public void updateClientsWithOpponentList(){
		sendBroadcast(new ServerConnectedClientsResponse(generateOpponentsList()));
	}

	public synchronized void sendBroadcast(Message message) {
		for (Client client : onlineClients) {
			System.out.println("Broadcast: Send message to client " + client.getClientId());
			client.sendMessage(message);
		}
	}
	
	
	
	
	
	public synchronized void sendMove(String clientID1, String clientID2, Message message){
		for (Client client : onlineClients) {
			if (client.getClientId().equals(clientID1) || client.getClientId().equals(clientID2)){
				client.sendMessage(message);
			}
		}
	}

	public synchronized ActionResult removeOnlineClient(Client client) {
		if (onlineClients.size() > 0 && onlineClients.contains(client)) {
			onlineClients.remove(client);
			disconnectClient(client);
			System.out.println("Removed online client " + onlineClients.size());
			updateClientsWithOpponentList();
			return new ActionResult(true, null);
		}

		else {
			return new ActionResult(false, "Online client does not exists");
		}
	}

	public synchronized ActionResult registerUser(User user) {
		ActionResult validationResult = validateUserDetails(user);
		if (validationResult.isActionSucceeded()) {
			User userFromDB = db.insertUser(user);
			if (userFromDB.getUserID() != 0) {
				return new ActionResult(true, null, userFromDB);

			} else {
				return new ActionResult(false, "Could not register user");
			}

		} else
			return validationResult;
	}
	
	public synchronized int findUserIDBySessionID(String SessionID){
		for (Client client : onlineClients) {
			if (client.getClientId().equals(SessionID))
				return client.getUserID();
		}
		return 0;
	}
	
	public synchronized String findSessionIDByUserEmail(String userEmail){
		User user = db.getUserByEmailAsObject(userEmail);
		for (Client client : onlineClients) {
			if (user != null && client.getUserID() == user.getUserID())
				return client.getClientId();
		}
		return null;
	}
	
	
	
	public synchronized void startGame(String client1ID, String client2ID){
		//check that users have no active games
		boolean canAddGame = true;
		ServerMakeMoveMsg message;
		GameController newGame = new GameController(this, client1ID, client2ID);
		for (GameController game : runningGames) {
			if (game.isPlayerInGame(client2ID) || game.isPlayerInGame(client1ID)){
				canAddGame = false;
				break;
			}	
		}
		
		if (canAddGame){
			runningGames.add(newGame);
			registerGame(client1ID, client2ID, "", 0);
			message = new ServerMakeMoveMsg(false, newGame.getPits(), newGame.getPlayerTurn(),client1ID, client2ID, newGame.getGameStatus());
		}
		else{
			newGame.setGameStatus(GameStatusEnum.aborted);
			message = new ServerMakeMoveMsg(false, newGame.getPits(), newGame.getPlayerTurn(),client1ID, client2ID, newGame.getGameStatus());
		}
			
		sendMove(client1ID, client2ID, message);
	}
	
	public synchronized void makeGameActive(String client1ID, String client2ID){
		for (GameController game : runningGames) {
			if (game.isPlayerInGame(client2ID) && game.isPlayerInGame(client1ID)){
				game.setGameStatus(GameStatusEnum.inProgress);
				ServerMakeMoveMsg message = new ServerMakeMoveMsg(true, game.getPits(), game.getPlayerTurn(),client1ID, client2ID, game.getGameStatus());
				sendMove(client1ID, client2ID, message);
//				sendBroadcast(message);
				break;
			}	
		}
		
	}
	
	
	public synchronized void makeMove(String client1ID, String client2ID,String moveSessionID,int movePitIndex){
		for (GameController game : runningGames) {
			if (game.isPlayerInGame(client2ID) && game.isPlayerInGame(client1ID)){
				game.makeMove(moveSessionID, movePitIndex);
//				game.setGameStatus(GameStatusEnum.inProgress);
//				ServerMakeMoveMsg message = new ServerMakeMoveMsg(false, game.getPits(), game.getPlayerTurn(), game.getGameStatus());
//				sendBroadcast(message);
				break;
			}	
		}
	}
	
	public synchronized void registerGame(String client1ID, String client2ID, String winnerSession, int winnerScore){
		int user1ID = findUserIDBySessionID(client1ID);
		int user2ID = findUserIDBySessionID(client2ID);
		int winnerID = 0;
		if (!winnerSession.isEmpty()){
			if (client1ID.equals(winnerSession)){
				winnerID = user1ID;
			}
			else
				winnerID = user2ID;
			User user = db.getUserByIDAsObject(winnerID);
			if (user.getBestScore() < winnerScore){
				user.setBestScore(winnerScore);
				updateUserInDB(user);
			}
			
		}

		Game game = new Game(user1ID, user2ID, true, winnerID, winnerScore);
		db.insertGame(game);
		
		
		
	}
	
	
	
	public synchronized void updateUserInDB(User user){
		db.insertUser(user);
	}

	public synchronized ActionResult validateUserDetails(User user) {
		if (!validator.validateEmail(user.getUserEmail()))
			return new ActionResult(false, "Invalid email address");
		else if (!validator.validateUsername(user.getUserName()))
			return new ActionResult(false, "Invalid user name");
		else if (!validator.validatePasswordComplexity(user.getPassword()))
			return new ActionResult(false, "Invalid password complexity");

		return new ActionResult(true, null);
	}

	/** ToDo - add password decryption */
	public synchronized ActionResult userLogin(User user) {
		// if (user.getUserID() > 0){
		if (findSessionIDByUserEmail(user.getUserEmail()) != null){
			return new ActionResult(false, "User already logged in");
		}
		else {
			
		
		if (db.isUserExist(user)) {
			System.out.println("User from client " + user);
			User userFromDB = db.getUserByEmailAsObject(user.getUserEmail());
			System.out.println("User from db " + userFromDB);
			if (userFromDB.getUserEmail().equals(user.getUserEmail())
				&& userFromDB.getPassword().equals(user.getPassword())){
				updateClientsWithOpponentList();
				return new ActionResult(true, null, userFromDB);
				
			}
			else
				return new ActionResult(false, "Wrong password");
		} else
			return new ActionResult(false, "User does not exist");
		}

		// }
		// return new ActionResult(false, "User does not exist");

	}

	public synchronized void disconnectClient(Client client) {
		// client.getSocket().close();
		System.out.println("Disconnectiong client " + client);
	}

	public synchronized void closeApp() {
		for (Client client : onlineClients) {
			removeOnlineClient(client);
		}
		clientListenerTask.setThreadShouldRun(false);

	}

}
