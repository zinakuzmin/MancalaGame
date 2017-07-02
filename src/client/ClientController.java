package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import model.User;
import protocol.ClientConnectMsg;
import protocol.ClientLoginMsg;
import protocol.ClientSignupMsg;
import protocol.Message;
import server.ActionResult;
import server.GameStatusEnum;
import client.UI.ClientLoginUI;
import client.UI.GameApprovalUI;
import client.UI.GameUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ClientController {
	/**
	 * Socket to server
	 */
	private Socket socket;
	
	
	/**
	 * Input stream to server
	 */
	private ObjectInputStream in;
	
	/**
	 * Output steam to server
	 */
	private ObjectOutputStream out;
	
	private Stage stage;
	
	
	private boolean serverListenerActivated = false;
	
	private ClientLoginUI loginView;
	
	
	private ServerListener serverListener;
	
	private ActionResult loginResult;
	
	
	private ActionResult signupResult;
	
	private User user;
	
	private String sessionID;
	
	private boolean hasActiveGame = false;
	
	private String opponentUserName = "";

	
	private String opponentSessionID = "";
	
	private Map<String, String> onlineUsers;
	
	private GameUI theGame = null;
	
	private String gameInitiator = "";
	
	
	private GameStatusEnum gameStatus = GameStatusEnum.waiting;
	
//	private ArrayList<E>
	
	


	

	/**
	 * Initialize {@link ZRaceGameController}
	 * @param primaryStage
	 */
	public ClientController(Stage primaryStage) {
		stage = primaryStage;
		
		new Thread(() -> {
			try {

				setSocket(new Socket("localhost", 8000));
				setOut(new ObjectOutputStream(getSocket().getOutputStream()));
				getOut().flush();
				in = new ObjectInputStream(getSocket().getInputStream());
				sendConnectMessage();
				serverListener = new ServerListener(in, this);
				serverListener.start();
//				new Thread(serverListener).start();

			} catch (IOException ex) {
				System.err.println(ex);
			}
		}).start();

		try {
			while (!serverListenerActivated) {
				Thread.sleep(200);

			}
			System.out.println("start client GUI");
			loginView = new ClientLoginUI(this);
			loginView.start(stage);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Once input from login screen approved send them to server
	 * @param userFullName
	 */
	public synchronized void sendLoginMessage(String userFullName, String password) {
		
		ClientLoginMsg msg = new ClientLoginMsg(userFullName.toLowerCase(), password);
		try {
			getOut().writeObject(msg);
			getOut().reset();
			getOut().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public synchronized void sendMessageToServer(Message message){
		try {
			getOut().writeObject(message);
			getOut().reset();
			getOut().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void sendConnectMessage(){
		sendMessageToServer(new ClientConnectMsg());
	}

	
	
	public synchronized Socket getSocket() {
		return socket;
	}

	public synchronized void setSocket(Socket socket) {
		this.socket = socket;
	}

	public synchronized ObjectOutputStream getOut() {
		return out;
	}

	public synchronized void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public synchronized void setServerListenerActivated(boolean b) {
		this.serverListenerActivated = b;
		
	}

	public synchronized ServerListener getServerListener() {
		return serverListener;
	}

	public synchronized void setServerListener(ServerListener serverListener) {
		this.serverListener = serverListener;
	}
	

	public synchronized ActionResult getLoginResult() {
		return loginResult;
	}

	public synchronized void setLoginResult(ActionResult loginResult) {
		System.out.println("LoginResult " + loginResult);
		this.loginResult = loginResult;
	}
	
	public synchronized User getUser() {
		return user;
	}

	public synchronized void setUser(User user) {
		this.user = user;
	}

	public synchronized void sendSignupMessage(String username, String userEmail,
			String password) {
		ClientSignupMsg message = new ClientSignupMsg(username, userEmail, password);
		sendMessageToServer(message);
		
	}

	public String findOpponentByUsername(String username){
		for (String key : onlineUsers.keySet()) {
			if (onlineUsers.get(key).equals(username))
				return key;
		}
		return null;
	}
	
	
	public String findOpponentSession(String session){
		for (String key : onlineUsers.keySet()) {
			if (key.equals(session))
				return key;
		}
		return null;
	}
	
	
	public void gameApproval(){
		GameApprovalUI gameApproval = new GameApprovalUI(this);
		try {
			Platform.runLater(gameApproval);
//			gameApproval.start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public ActionResult getSignupResult() {
		return signupResult;
	}

	public void setSignupResult(ActionResult signupResult) {
		this.signupResult = signupResult;
	}

	public Map<String, String> getOnlineUsers() {
		return onlineUsers;
	}

	public void setOnlineUsers(Map<String, String> onlineUsers) {
		this.onlineUsers = onlineUsers;
		System.out.println("Client connected users " + onlineUsers);
		
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public boolean isHasActiveGame() {
		return hasActiveGame;
	}

	public void setHasActiveGame(boolean hasActiveGame) {
		this.hasActiveGame = hasActiveGame;
	}

	public String getOpponentUserName() {
		return opponentUserName;
	}

	public void setOpponentUserName(String opponentUserName) {
		this.opponentUserName = opponentUserName;
	}

	public String getOpponentSessionID() {
		return opponentSessionID;
	}

	public void setOpponentSessionID(String opponentSessionID) {
		this.opponentSessionID = opponentSessionID;
	}

	public GameUI getTheGame() {
		return theGame;
	}

	public void setTheGame(GameUI theGame) {
		this.theGame = theGame;
	}

	public GameStatusEnum getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatusEnum gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public String getGameInitiator() {
		return gameInitiator;
	}

	public void setGameInitiator(String gameInitiator) {
		this.gameInitiator = gameInitiator;
	}

}
