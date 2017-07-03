package client;

import java.util.Map;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
				
//				System.out.println("UPdating user list");
				ObservableList<String> existingList = lobby.getOnlineUsersList();
//				System.out.println("Before " + existingList);
				Map<String, String> onlineUsersFromServer = lobby.getClientController().getOnlineUsers();
//				System.out.println("List from server " + onlineUsersFromServer);
				
				
				//Add to lobby list new user that is not current user
				//Remove from lobby list non existing in server users
				for (String key : onlineUsersFromServer.keySet()) {
					if (!key.equals(lobby.getClientController().getSessionID()) && !(existingList.contains(onlineUsersFromServer.get(key))))
						existingList.add(onlineUsersFromServer.get(key));
				}
				
				
				if (existingList.size() > 0){
					for (String onlineUser : existingList) {
						if (!onlineUsersFromServer.containsValue(onlineUser))
							existingList.remove(onlineUser);
					}
					
				}
//				System.out.println("After " + existingList);
				
				
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						
						lobby.setOnlineUsers(existingList);
						
						
					}
				});
				Thread.sleep(2_000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
