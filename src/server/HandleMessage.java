package server;

import model.User;
import protocol.ClientAbortedGameMsg;
import protocol.ClientConnectMsg;
import protocol.ClientLoginMsg;
import protocol.ClientMakeMoveMsg;
import protocol.ClientSignupMsg;
import protocol.ClientStartGameMsg;
import protocol.Message;
import protocol.ServerClientConnectResponse;
import protocol.ServerLoginResponseMsg;
import protocol.ServerMakeMoveMsg;
import protocol.ServerSignupResponseMsg;

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
	 * 
	 * @param messageFromClient
	 * @param clientHandler
	 */
	public HandleMessage(Message messageFromClient, Client clientHandler) {
		this.messageFromClient = messageFromClient;
		this.client = clientHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run() Run thread that recognizes the message type
	 * and performs action
	 */
	@Override
	public void run() {
		System.out.println(messageFromClient);

		if (messageFromClient instanceof ClientConnectMsg) {
			client.sendMessage(new ServerClientConnectResponse(client
					.getClientId()));
		}

		if (messageFromClient instanceof ClientSignupMsg) {
			User user = new User(
					((ClientSignupMsg) messageFromClient).getUserEmail(),
					((ClientSignupMsg) messageFromClient).getUsername(),
					((ClientSignupMsg) messageFromClient).getPassword(), 0);
			ActionResult result = client.getClientListener()
					.getServerController().registerUser(user);
			new Thread(() -> {
				try {

					// sendMessage(new ServerSignupResponseMsg(result));
					client.getStreamToClient().writeObject(
							new ServerSignupResponseMsg(result));
					client.getStreamToClient().flush();

				} catch (Exception e) {

					e.printStackTrace();
				}
			}).start();

		}

		else if (messageFromClient instanceof ClientLoginMsg) {

			// User user = new User("", ((ClientLoginMsg)
			// messageFromClient).getUsername(), ((ClientLoginMsg)
			// messageFromClient).getPassword(), 0);
			User user = new User(
					((ClientLoginMsg) messageFromClient).getUsername(), "",
					((ClientLoginMsg) messageFromClient).getPassword(), 0);
			ActionResult result = client.getClientListener()
					.getServerController().userLogin(user);
			client.sendMessage(new ServerLoginResponseMsg(result,null));
			if (result.getObject() != null
					&& ((User) result.getObject()).getUserID() != 0) {
				client.setUserID(((User) result.getObject()).getUserID());

				new Thread(() -> {
					try {
						
						if (result.isActionSucceeded()){
							client.getClientListener().getServerController().updateClientsWithOpponentList();
						}
//						Map<String, String> onlineUsers = client
//								.getClientListener().getServerController()
//								.generateOpponentsList();
//						client.sendMessage(new ServerConnectedClientsResponse(
//								onlineUsers));
						

					} catch (Exception e) {

						e.printStackTrace();
					}

				}).start();

			}
		}

			else if (messageFromClient instanceof ClientStartGameMsg) {
				
				if (((ClientStartGameMsg) messageFromClient).isStartGameDeclined()){
					ServerMakeMoveMsg message = new ServerMakeMoveMsg(false, null, null, ((ClientStartGameMsg) messageFromClient).getPlayer1Session(), ((ClientStartGameMsg) messageFromClient).getPlayer2Session(), GameStatusEnum.declined);
					client.getClientListener().getServerController().sendMove(((ClientStartGameMsg) messageFromClient).getPlayer1Session(),((ClientStartGameMsg) messageFromClient).getPlayer2Session(), message);
				}
				
				
				else if (((ClientStartGameMsg) messageFromClient)
						.isStartGameApproved()) {
					client.getClientListener()
							.getServerController()
							.makeGameActive(
									((ClientStartGameMsg) messageFromClient)
											.getPlayer1Session(),
									((ClientStartGameMsg) messageFromClient)
											.getPlayer2Session());
				} else
					client.getClientListener()
							.getServerController()
							.startGame(
									((ClientStartGameMsg) messageFromClient)
											.getPlayer1Session(),
									((ClientStartGameMsg) messageFromClient)
											.getPlayer2Session());
			}
		
			else if (messageFromClient instanceof ClientAbortedGameMsg){
				client.getClientListener().getServerController().makeMove(((ClientAbortedGameMsg) messageFromClient).getClientAskedToAbortTheGame(), ((ClientAbortedGameMsg) messageFromClient).getOtherClient(), ((ClientAbortedGameMsg) messageFromClient).getClientAskedToAbortTheGame(), -1);
//				ServerMakeMoveMsg message = new ServerMakeMoveMsg(false, null, null, ((ClientAbortedGameMsg) messageFromClient).getClientAskedToAbortTheGame(), ((ClientAbortedGameMsg) messageFromClient).getOtherClient(), GameStatusEnum.aborted);
//				client.getClientListener().getServerController().sendMove(((ClientAbortedGameMsg) messageFromClient).getClientAskedToAbortTheGame(), ((ClientAbortedGameMsg) messageFromClient).getOtherClient(), message);
			
			}
		

			else if (messageFromClient instanceof ClientMakeMoveMsg) {
				client.getClientListener()
						.getServerController()
						.makeMove(
								((ClientMakeMoveMsg) messageFromClient)
										.getPlayer1SessionID(),
								((ClientMakeMoveMsg) messageFromClient)
										.getPlayer2SessionID(),
								((ClientMakeMoveMsg) messageFromClient)
										.getMoveSessionID(),
								((ClientMakeMoveMsg) messageFromClient)
										.getMovePitIndex());
			}
		
		}

	}

