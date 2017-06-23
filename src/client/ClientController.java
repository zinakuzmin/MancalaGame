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
import client.UI.ClientLoginUI;
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
	
	private boolean serverListenerActivated = false;
	
	private ClientLoginUI loginView;
	
	
	private ServerListener serverListener;
	
	private ActionResult loginResult;
	
	
	private ActionResult signupResult;
	
	private User user;
	
	private String sessionID;
	
	private Map<String, String> onlineUsers;
	
//	private ArrayList<E>
	
	


	

	/**
	 * Initialize {@link ZRaceGameController}
	 * @param primaryStage
	 */
	public ClientController(Stage primaryStage) {
		
		
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
			loginView.start(new Stage());


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
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

}
