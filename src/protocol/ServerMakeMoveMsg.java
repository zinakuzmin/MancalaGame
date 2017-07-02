package protocol;

import java.util.Arrays;

import server.GameStatusEnum;

public class ServerMakeMoveMsg extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isMoveAllowed;
	private int[] pits;
	private String nextTurn;
	private String player1SessionID;
	private String player2SessionID;
	private GameStatusEnum gameStatus;
	
	
	
	
//	public ServerMakeMoveMsg(boolean isMoveAllowed, int[] pits,
//			String nextTurn, GameStatusEnum gameStatus) {
//		this.isMoveAllowed = isMoveAllowed;
//		this.pits = pits;
//		this.nextTurn = nextTurn;
//		this.gameStatus = gameStatus;
//	}
//	
	
	public boolean isMoveAllowed() {
		return isMoveAllowed;
	}
	public ServerMakeMoveMsg(boolean isMoveAllowed, int[] pits, String nextTurn,
		String player1SessionID, String player2SessionID,
		GameStatusEnum gameStatus) {
	super();
	this.isMoveAllowed = isMoveAllowed;
	this.pits = pits;
	this.nextTurn = nextTurn;
	this.player1SessionID = player1SessionID;
	this.player2SessionID = player2SessionID;
	this.gameStatus = gameStatus;
}
	public void setMoveAllowed(boolean isMoveAllowed) {
		this.isMoveAllowed = isMoveAllowed;
	}
	public int[] getPits() {
		return pits;
	}
	public void setPits(int[] pits) {
		this.pits = pits;
	}
	public String getNextTurn() {
		return nextTurn;
	}
	public void setNextTurn(String nextTurn) {
		this.nextTurn = nextTurn;
	}
	public GameStatusEnum getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(GameStatusEnum gameStatus) {
		this.gameStatus = gameStatus;
	}


	

	@Override
	public String toString() {
		return "ServerMakeMoveMsg [isMoveAllowed=" + isMoveAllowed + ", pits="
				+ Arrays.toString(pits) + ", nextTurn=" + nextTurn
				+ ", player1SessionID=" + player1SessionID
				+ ", player2SessionID=" + player2SessionID + ", gameStatus="
				+ gameStatus + "]";
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
	
	

}
