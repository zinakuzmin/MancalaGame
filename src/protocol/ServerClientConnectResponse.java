package protocol;

public class ServerClientConnectResponse extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sesssionID;
	
	public ServerClientConnectResponse(String sessionID) {
		setSesssionID(sessionID);

	}
	
	
	public String getSesssionID() {
		return sesssionID;
	}
	public void setSesssionID(String sesssionID) {
		this.sesssionID = sesssionID;
	}


	@Override
	public String toString() {
		return "ServerClientConnectResponse [sesssionID=" + sesssionID + "]";
	}

	
	
}
