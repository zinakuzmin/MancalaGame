package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.server.ServerCloneException;
import java.util.ArrayList;

import model.User;
import protocol.Message;
import protocol.ServerClientConnectResponse;
import protocol.ServerConnectedClientsResponse;
import protocol.ServerLoginResponseMsg;
import protocol.ServerSignupResponseMsg;
import javafx.application.Platform;


public class ServerListener extends Thread{
	private ObjectInputStream in;
	private ClientController gameController;
	private boolean shouldListenToServer = true;

	/**
	 * Initialize {@link ServerListener}
	 * @param in
	 * @param gameController
	 */
	public ServerListener(ObjectInputStream in,ClientController gameController) {
		this.in = in;
		this.gameController = gameController;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 * Run listener for outcomming messages
	 */
	@Override
	public void run() {
		
		
		gameController.setServerListenerActivated(true);
		
		while (shouldListenToServer){
			
			try{
				Message message = (Message) in.readObject();
				System.out.println("Client got message");
				System.out.println(message);
				
				if (message instanceof ServerClientConnectResponse){
					gameController.setSessionID(((ServerClientConnectResponse) message).getSesssionID());
				}
				
				
				else if (message instanceof ServerSignupResponseMsg){
					gameController.setSignupResult(((ServerSignupResponseMsg) message).getResult());
					
				}
				else if (message instanceof ServerLoginResponseMsg){
					
					gameController.setLoginResult(((ServerLoginResponseMsg) message).getResult());
					if (((ServerLoginResponseMsg) message).getResult().isActionSucceeded()){
						if (((ServerLoginResponseMsg) message).getResult().getObject() != null){
							gameController.setUser((User)((ServerLoginResponseMsg) message).getResult().getObject());
							
						}
						
					}
					
				}
				else if (message instanceof ServerConnectedClientsResponse){
					gameController.setOnlineUsers(((ServerConnectedClientsResponse) message).getConnectedClients());
					
				}
				
			}
			
			catch (Exception e){
				e.printStackTrace();
			}
		}
		
//		gameController.setServerListenerActivated(true);
//		while (true) {
//			
//			try {
//				Message message = (Message) in.readObject();
//				
//				if (message instanceof UpdateRacesMsg) {
//					ArrayList<Race> activaRaces = (((UpdateRacesMsg) message).getRaces());
//					gameController.setActiveRaces(activaRaces);
//					gameController.setGotRacesFromServer(true);
//					Platform.runLater(() -> gameController.getClientView().setRacesNamesInView());
//					
//				} else if (message instanceof UserDetailsMsg) {
//					
//					User user = ((UserDetailsMsg) message).getUser();
//					gameController.setUserDetails(user);
//					gameController.setGotUserFromServer(true);
//					Platform.runLater(() -> gameController.getClientView()
//							.setUserDetailsInView());
//					
//
//				} else if (message instanceof UpdateRaceRunsMsg) {
//					
//					ArrayList<RaceRun> raceRuns = (((UpdateRaceRunsMsg) message)
//							.getRaceRuns());
//					gameController.setRaceRuns(raceRuns);
//					gameController.setGotRacesRunsFromServer(true);
//					Platform.runLater(() -> gameController.getClientView()
//							.setRacesStatusInView());
//					
//				}
//
//				else if (message instanceof WinnerCarMsg) {
//					
//					gameController.setLastWinnerCarId(((WinnerCarMsg) message)
//							.getCarId());
//					Platform.runLater(() -> gameController.getClientView().setUserDetailsInView());
//				}
//				
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				break;
//			}
//		}
	}

	public boolean isShouldListenToServer() {
		return shouldListenToServer;
	}

	public void setShouldListenToServer(boolean shouldListenToServer) {
		this.shouldListenToServer = shouldListenToServer;
	}
	
	public void stopServerListener(){
		setShouldListenToServer(false);
	}

}


