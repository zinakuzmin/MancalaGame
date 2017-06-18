package server;


import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.TextArea;
import javafx.stage.Stage;
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
	
	
	
	public MancalaServerController(Stage stage){
		onlineClients = new ArrayList<Client>();
		db = new DBHandler();
		validator = new Validator();
		clientListenerTask = new ClientListener(this);
		clientsListenerThread = new Thread(clientListenerTask);
		clientsListenerThread.start();
//		try {
//			new ServerUI(new TextArea()).start(new Stage());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
	
	
	
	
//
//	public MancalaServerController(Stage stage) {
//		// TODO Auto-generated constructor stub
//	}





	public ArrayList<Client> getOnlineClients() {
		return onlineClients;
	}

	
	public synchronized ActionResult addOnlineClient(Client client){
		if (onlineClients.size() < GlobalParams.MAX_NUMBER_OF_CONNECTED_CLIENTS){
			onlineClients.add(client);
			System.out.println("Added new client " + onlineClients.size());
			return new ActionResult(true, null);
		}
		else{
			return new ActionResult(false, "Exceeding number of allowed clients");
		}
	}
	
	
	public synchronized ActionResult removeOnlineClient(Client client){
		if (onlineClients.size() > 0 && onlineClients.contains(client)){
			onlineClients.remove(client);
			disconnectClient(client);
			System.out.println("Removed online client " + onlineClients.size());
			return new ActionResult(true, null);
		}
		
		else {
			return new ActionResult(false, "Online client does not exists");
		}
	}
	
	public synchronized ActionResult registerUser(User user){
		ActionResult validationResult = validateUserDetails(user);
		if (validationResult.isActionSucceeded()){
			User userFromDB = db.insertUser(user);
			if (userFromDB.getUserID() != 0){
				return new ActionResult(true, null, userFromDB);
				
			}
			else {
				return new ActionResult(false, "Could not register user");
			}
			
		}
		else
			return validationResult;
	}
	
	
	public synchronized ActionResult validateUserDetails(User user){
		if (!validator.validateEmail(user.getUserEmail()))
			return new ActionResult(false, "Invalid email address");
		else if (!validator.validateUsername(user.getUserName()))
			return new ActionResult(false, "Invalid user name");
		else if (!validator.validatePasswordComplexity(user.getPassword()))
			return new ActionResult(false, "Invalid password complexity");
				
		return new ActionResult(true, null);
	}
	
	/**ToDo - add password decryption */
	public synchronized ActionResult userLogin(User user){
//		if (user.getUserID() > 0){
			if (db.isUserExist(user)){
				System.out.println("User from client "+ user);
				User userFromDB = db.getUserByEmailAsObject(user.getUserEmail());
				System.out.println("User from db "+ userFromDB);
				if (userFromDB.getUserEmail().equals(user.getUserEmail()) && userFromDB.getPassword().equals(user.getPassword()))
					return new ActionResult(true, null, userFromDB);
				else
					return new ActionResult(false, "Wrong password");
			}
			else
				return new ActionResult(false, "User does not exist");
			
//		}
//		return new ActionResult(false, "User does not exist");
		
	}
	
	
	public synchronized void disconnectClient(Client client){
		//			client.getSocket().close();
		System.out.println("Disconnectiong client " + client);
	}
	
	public synchronized void closeApp(){
		for (Client client : onlineClients) {
			removeOnlineClient(client);
		}
		clientListenerTask.setThreadShouldRun(false);
		
		
	}

}
