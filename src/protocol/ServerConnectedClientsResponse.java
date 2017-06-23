package protocol;

import java.util.Map;

public class ServerConnectedClientsResponse extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> connectedClients;
	
	public ServerConnectedClientsResponse(Map<String, String> connectedClients) {
		setConnectedClients(connectedClients);
	}
	
	
	public Map<String, String> getConnectedClients() {
		return connectedClients;
	}
	public void setConnectedClients(Map<String, String> connectedClients) {
		this.connectedClients = connectedClients;
	}


	@Override
	public String toString() {
		return "ServerConnectedClientsResponse [connectedClients="
				+ connectedClients + "]";
	}
	
	

}
