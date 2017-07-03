package client;

import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Map;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.User;
import protocol.Message;
import protocol.ServerClientConnectResponse;
import protocol.ServerConnectedClientsResponse;
import protocol.ServerLoginResponseMsg;
import protocol.ServerMakeMoveMsg;
import protocol.ServerSignupResponseMsg;
import server.GameStatusEnum;
import client.UI.ClientLobbyUI;
import client.UI.GameUI;

public class ServerListener extends Thread {
	private ObjectInputStream in;
	private ClientController gameController;
	private boolean shouldListenToServer = true;

	/**
	 * Initialize {@link ServerListener}
	 * 
	 * @param in
	 * @param gameController
	 */
	public ServerListener(ObjectInputStream in, ClientController gameController) {
		this.in = in;
		this.gameController = gameController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run() Run listener for outcomming messages
	 */
	@Override
	public void run() {

		gameController.setServerListenerActivated(true);

		while (shouldListenToServer) {

			try {
				Message message = (Message) in.readObject();
				System.out.println("Client " + gameController.getSessionID() + " got message");
				System.out.println(message);

				if (message instanceof ServerClientConnectResponse) {
					gameController
							.setSessionID(((ServerClientConnectResponse) message)
									.getSesssionID());
				}

				
				
				else if (message instanceof ServerSignupResponseMsg) {
					gameController
							.setSignupResult(((ServerSignupResponseMsg) message)
									.getResult());

					
					
				} else if (message instanceof ServerLoginResponseMsg) {

					gameController
							.setLoginResult(((ServerLoginResponseMsg) message)
									.getResult());
					if (((ServerLoginResponseMsg) message).getResult()
							.isActionSucceeded()) {
						if (((ServerLoginResponseMsg) message).getResult()
								.getObject() != null) {
							gameController
									.setUser((User) ((ServerLoginResponseMsg) message)
											.getResult().getObject());

						}

					}

					
					
					
				} else if (message instanceof ServerConnectedClientsResponse) {
					Map<String, String> onlineClients = ((ServerConnectedClientsResponse) message)
							.getConnectedClients();
					gameController
							.setOnlineUsers(onlineClients);

				}

				
				
				
				
				else if (message instanceof ServerMakeMoveMsg) {
					GameStatusEnum gameStatus = ((ServerMakeMoveMsg) message)
							.getGameStatus();
					gameController.setGameStatus(gameStatus);
					
					
					
					if (gameStatus.equals(GameStatusEnum.waiting)) {
						if (!((ServerMakeMoveMsg) message).isMoveAllowed()) {
							//Prompt game approval
							if (!((ServerMakeMoveMsg) message).getNextTurn()
									.equals(gameController.getSessionID())) {
								
								
								
								gameController
										.setOpponentSessionID(((ServerMakeMoveMsg) message)
												.getPlayer1SessionID());
								gameController
										.setOpponentUserName(gameController
												.findOpponentSession(((ServerMakeMoveMsg) message)
														.getPlayer1SessionID()));
								gameController.gameApproval();
							}
							
							

						}
						//Move  allowed
						else {
							
						}
						
						
						
					}  else if (gameStatus.equals(GameStatusEnum.declined)) {
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								try {
									gameController.getTheGame().stop();
									ClientLobbyUI lobby = new ClientLobbyUI(gameController);
									gameController.setGameInitiator("");
									gameController.setHasActiveGame(false);
									gameController.setOpponentSessionID("");
									gameController.setOpponentUserName("");
									lobby.start(new Stage());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						});

					} else if (gameStatus.equals(GameStatusEnum.inProgress)) {

						if (((ServerMakeMoveMsg) message).isMoveAllowed()) {
							
							if (gameController.getTheGame() != null) {
								int[] boardFromServer = ((ServerMakeMoveMsg) message)
										.getPits();
								System.out.println("Board from server "
										+ Arrays.toString(boardFromServer));
								Platform.runLater(new Runnable() {

									@Override
									public void run() {
										gameController.setGameStatus(((ServerMakeMoveMsg) message).getGameStatus());
										gameController.getTheGame().setNextTurn(((ServerMakeMoveMsg) message).getNextTurn());
										gameController.getTheGame().makeMoveUI(((ServerMakeMoveMsg) message).isMoveAllowed(),
												boardFromServer, false);

									}
								});
							} else {
								GameUI game = new GameUI(gameController, false, gameController.getStage());
								gameController.setTheGame(game);
								if (gameController.getLobby()!= null){
									gameController.getLobby().stop();
									gameController.getStage().close();
								}
							
								try {
									Platform.runLater(game);
									// game.start(new Stage());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								int[] boardFromServer = ((ServerMakeMoveMsg) message)
										.getPits();
								System.out.println("Board from server "
										+ Arrays.toString(boardFromServer));
								Platform.runLater(new Runnable() {

									@Override
									public void run() {
										gameController.getTheGame().makeMoveUI(((ServerMakeMoveMsg) message).isMoveAllowed(),
												boardFromServer, false);

									}
								});
							}
						}
						else{
							//move not allowed
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									gameController.getTheGame().makeMoveUI(((ServerMakeMoveMsg) message).isMoveAllowed(),
											((ServerMakeMoveMsg) message).getPits(), false);

								}
							});
						}
					
					} else if (gameStatus.equals(GameStatusEnum.competed)){
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
//								gameController.getTheGame().setGameStatusField(gameStatusField);
								gameController.getTheGame().makeMoveUI(((ServerMakeMoveMsg) message).isMoveAllowed(),
										((ServerMakeMoveMsg) message).getPits(), true);

							}
						});
					}
					else if (gameStatus.equals(GameStatusEnum.aborted)){
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								if (gameController.getTheGame() != null)
									gameController.getTheGame().abortGame();

							}
						});
					}
				}

			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

		
	}

	public boolean isShouldListenToServer() {
		return shouldListenToServer;
	}

	public void setShouldListenToServer(boolean shouldListenToServer) {
		this.shouldListenToServer = shouldListenToServer;
	}

	public void stopServerListener() {
		setShouldListenToServer(false);
	}

}
