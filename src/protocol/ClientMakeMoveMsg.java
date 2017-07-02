package protocol;

public class ClientMakeMoveMsg extends Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String player1SessionID;
	private String player2SessionID;
	private String moveSessionID;
	private int movePitIndex;
	
	
	
	
	
	public ClientMakeMoveMsg(String player1SessionID, String player2SessionID,
			String moveSessionID, int movePitIndex) {
		super();
		this.player1SessionID = player1SessionID;
		this.player2SessionID = player2SessionID;
		this.moveSessionID = moveSessionID;
		this.movePitIndex = movePitIndex;
	}
	public String getPlayer1SessionID() {
		return player1SessionID;
	}
	public void setPlayer1SessionID(String player1SessionID) {
		this.player1SessionID = player1SessionID;
	}
	public String getPlayer2SessionID() {
		return player2SessionID;
	}
	public void setPlayer2SessionID(String player2SessionID) {
		this.player2SessionID = player2SessionID;
	}
	public String getMoveSessionID() {
		return moveSessionID;
	}
	public void setMoveSessionID(String moveSessionID) {
		this.moveSessionID = moveSessionID;
	}
	public int getMovePitIndex() {
		return movePitIndex;
	}
	public void setMovePitIndex(int movePitIndex) {
		this.movePitIndex = movePitIndex;
	}
	@Override
	public String toString() {
		return super.toString() +
				"ClientMakeMoveMsg [player1SessionID=" + player1SessionID
				+ ", player2SessionID=" + player2SessionID + ", moveSessionID="
				+ moveSessionID + ", movePitIndex=" + movePitIndex + "]";
	}
	
	
	
}
