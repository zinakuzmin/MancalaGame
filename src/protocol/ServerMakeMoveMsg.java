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
	private GameStatusEnum gameStatus;
	
	
	
	
	public ServerMakeMoveMsg(boolean isMoveAllowed, int[] pits,
			String nextTurn, GameStatusEnum gameStatus) {
		this.isMoveAllowed = isMoveAllowed;
		this.pits = pits;
		this.nextTurn = nextTurn;
		this.gameStatus = gameStatus;
	}
	
	
	public boolean isMoveAllowed() {
		return isMoveAllowed;
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
				+ ", gameStatus=" + gameStatus + "]";
	}
	
	

}
