package server;

import java.util.ArrayList;

import protocol.ClientConnectMsg;
import protocol.ClientLoginMsg;
import protocol.ClientSignupMsg;
import protocol.Message;


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
		
		if (messageFromClient instanceof ClientConnectMsg){
			
		}
		
		if (messageFromClient instanceof ClientSignupMsg){
			
		}
		
		else if (messageFromClient instanceof ClientLoginMsg){
			
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
//		else if (messageFromClient instanceof ClientBetMsg){
//			
//			ArrayList<Bet> bets = ((ClientBetMsg)messageFromClient).getBets();
//			
//			for (Bet bet : bets) {
//				client.getController().registerBet(bet);
//				
//			}
//			client.getController().getLogger().logMessage(messageFromClient);
//			
//			
//		}
//		
//		else if (messageFromClient instanceof ClientDisconnectMsg){
//			client.getController().getLogger().logMessage(messageFromClient);
//			client.clientDisconnect(((ClientDisconnectMsg)messageFromClient).getUser());
//		}

	
		
	}

}
