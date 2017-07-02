package client;

import javafx.application.Platform;
import client.UI.ClientLobbyUI;

public class OnlineUsersListener implements Runnable{
	private ClientLobbyUI lobby;
	
	
	public OnlineUsersListener(ClientLobbyUI lobby) {
		this.lobby = lobby;
	}

	@Override
	public void run() {
		while (lobby != null && lobby.isRunOnlineUsersListener()){
			try {
				Thread.sleep(2_000);
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						lobby.setOnlineUsers();
						
						
					}
				});
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
