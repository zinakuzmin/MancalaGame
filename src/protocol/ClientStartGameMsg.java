package protocol;

public class ClientStartGameMsg extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String player1Session;
	private String player2Session;
	private boolean startGameApproved;
	
	
	
	public ClientStartGameMsg(String player1Session, String player2Session,
			boolean startGameApproved) {
		super();
		this.player1Session = player1Session;
		this.player2Session = player2Session;
		this.startGameApproved = startGameApproved;
	}
	
	
	public String getPlayer1Session() {
		return player1Session;
	}
	public void setPlayer1Session(String player1Session) {
		this.player1Session = player1Session;
	}
	public String getPlayer2Session() {
		return player2Session;
	}
	public void setPlayer2Session(String player2Session) {
		this.player2Session = player2Session;
	}
	public boolean isStartGameApproved() {
		return startGameApproved;
	}
	public void setStartGameApproved(boolean startGameApproved) {
		this.startGameApproved = startGameApproved;
	}
	@Override
	public String toString() {
		return "ClientStartGameMsg [player1Session=" + player1Session
				+ ", player2Session=" + player2Session + ", startGameApproved="
				+ startGameApproved + "]";
	}
	
	
	

}
