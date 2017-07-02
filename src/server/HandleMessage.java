package server;

import java.io.IOException;
import java.util.ArrayList;




import java.util.Map;





import model.User;
import protocol.ClientConnectMsg;
import protocol.ClientLoginMsg;
import protocol.ClientMakeMoveMsg;
import protocol.ClientSignupMsg;
import protocol.ClientStartGameMsg;
import protocol.Message;import protocol.ServerClientConnectResponse;
import protocol.ServerConnectedClientsResponse;
import protocol.ServerLoginResponseMsg;import protocol.ServerSignupResponseMsg;





public class HandleMessage implements Runnable {
	/**
	 * Message that server received from client
	 */
	private Message messageFromClient;
	
	/**
	 * Client object for controlling and forwarding message to server controller
	 */
	private Client client;
	
	/**
	 * Initialize HandleMessage object
	 * @param messageFromClient
	 * @param clientHandler
	 */
	public HandleMessage(Message messageFromClient, Client clientHandler) {
		this.messageFromClient = messageFromClient;
		this.client = clientHandler;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * Run thread that recognizes the message type and performs action
	 */
	@Override
	public void run() {
		System.out.println(messageFromClient);
		
		if (messageFromClient instanceof ClientConnectMsg){
			client.sendMessage(new ServerClientConnectResponse(client.getClientId()));
		}
		
		if (messageFromClient instanceof ClientSignupMsg){
			User user = new User(((ClientSignupMsg) messageFromClient).getUserEmail(), ((ClientSignupMsg) messageFromClient).getUsername(), ((ClientSignupMsg) messageFromClient).getPassword(), 0);
			ActionResult result = client.getClientListener().getServerController().registerUser(user);
			new Thread(() -> {
				try {
				
//					sendMessage(new ServerSignupResponseMsg(result));
					client.getStreamToClient().writeObject(new ServerSignupResponseMsg(result));
					client.getStreamToClient().flush();
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}).start();
			
		}
		
		else if (messageFromClient instanceof ClientLoginMsg){
			
//			User user = new User("", ((ClientLoginMsg) messageFromClient).getUsername(), ((ClientLoginMsg) messageFromClient).getPassword(), 0);
			User user = new User(((ClientLoginMsg) messageFromClient).getUsername(), "", ((ClientLoginMsg) messageFromClient).getPassword(), 0);
			ActionResult result = client.getClientListener().getServerController().userLogin(user);
			if (result.getObject() != null &&  ((User)result.getObject()).getUserID() != 0){
				client.setUserID(((User)result.getObject()).getUserID());
			}
			
			new Thread(() -> {
				try {
				
//					sendMessage(new ServerLoginResponseMsg(result));
					client.getStreamToClient().writeObject(new ServerLoginResponseMsg(result, null));
					client.getStreamToClient().flush();
					Map<String, String> onlineUsers = client.getClientListener().getServerController().generateOpponentsList();
					client.sendMessage(new ServerConnectedClientsResponse(onlineUsers));
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}).start();
			
		}
		
		else if (messageFromClient instanceof ClientStartGameMsg){
			if (((ClientStartGameMsg) messageFromClient).isStartGameApproved()){
				client.getClientListener().getServerController().makeGameActive(((ClientStartGameMsg) messageFromClient).getPlayer1Session(), ((ClientStartGameMsg) messageFromClient).getPlayer2Session());
			}
			else
				client.getClientListener().getServerController().startGame(((ClientStartGameMsg) messageFromClient).getPlayer1Session(), ((ClientStartGameMsg) messageFromClient).getPlayer2Session());
		}
		
		else if (messageFromClient instanceof ClientMakeMoveMsg){
			client.getClientListener().getServerController().makeMove(((ClientMakeMoveMsg) messageFromClient).getPlayer1SessionID(), ((ClientMakeMoveMsg) messageFromClient).getPlayer2SessionID(), ((ClientMakeMoveMsg) messageFromClient).getMoveSessionID(), ((ClientMakeMoveMsg) messageFromClient).getMovePitIndex());
		}
//		if (messageFromClient instanceof ClientConnectMsg){
//			
//			client.setUserId(((ClientConnectMsg) messageFromClient).getUserId());
//			client.setUserFullName(((ClientConnectMsg) messageFromClient).getUserFullName());
//			User user = client.getController().userLoginOrRegister(((ClientConnectMsg) messageFromClient).getUserId(), ((ClientConnectMsg) messageFromClient).getUserFullName());
//			client.getController().addClientToActiveClients(client);
//			client.getController().getLogger().logMessage(messageFromClient);
//			
//			new Thread(() -> {
//				try {
//				
//					client.getStreamToClient().writeObject(new UserDetailsMsg(0, user));
//					ArrayList<Race> activeRaces = client.getController().getActiveRaces();
//					client.getStreamToClient().flush();
//					client.getStreamToClient().writeObject(new UpdateRacesMsg(100, activeRaces));
//					
//					client.getStreamToClient().writeObject(new UpdateRaceRunsMsg(101, client.getController().getRaceRuns()));
//					client.getStreamToClient().flush();
//				} catch (Exception e) {
//					
//					e.printStackTrace();
//				}
//			}).start();
//
//		}

//		
//		else if (messageFromClient instanceof ClientDisconnectMsg){
//			client.getController().getLogger().logMessage(messageFromClient);
//			client.clientDisconnect(((ClientDisconnectMsg)messageFromClient).getUser());
//		}

	
		
	}
	
	

}
