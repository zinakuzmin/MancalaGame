package protocol;

public class ClientAbortedGameMsg extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clientAskedToAbortTheGame;
	private String otherClient;
	
	
	
	
	
	
	
	public ClientAbortedGameMsg(String clientAskedToAbortTheGame,
			String otherClient) {
		super();
		this.clientAskedToAbortTheGame = clientAskedToAbortTheGame;
		this.otherClient = otherClient;
	}






	@Override
	public String toString() {
		return "ClientAbortedGameMsg [clientAskedToAbortTheGame="
				+ clientAskedToAbortTheGame + ", otherClient=" + otherClient
				+ "]";
	}






	public String getClientAskedToAbortTheGame() {
		return clientAskedToAbortTheGame;
	}
	public void setClientAskedToAbortTheGame(String clientAskedToAbortTheGame) {
		this.clientAskedToAbortTheGame = clientAskedToAbortTheGame;
	}


	public String getOtherClient() {
		return otherClient;
	}


	public void setOtherClient(String otherClient) {
		this.otherClient = otherClient;
	}
	
	

}
