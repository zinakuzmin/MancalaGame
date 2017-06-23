package protocol;

public class ClientMakeMoveMsg extends Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clientSessionId;
	private int movePitIndex;
	
	
	public ClientMakeMoveMsg(String clientSessionId,int movePitIndex ) {
		setClientSessionId(clientSessionId);
		setMovePitIndex(movePitIndex);
	}
	
	public String getClientSessionId() {
		return clientSessionId;
	}
	public void setClientSessionId(String clientSessionId) {
		this.clientSessionId = clientSessionId;
	}
	public int getMovePitIndex() {
		return movePitIndex;
	}
	public void setMovePitIndex(int movePitIndex) {
		this.movePitIndex = movePitIndex;
	}

	@Override
	public String toString() {
		return "ClientMakeMoveMsg [clientSessionId=" + clientSessionId
				+ ", movePitIndex=" + movePitIndex + "]";
	}
	

}
