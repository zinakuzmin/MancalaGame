package model;

public class Game {
	private int gameID;
	private int user1ID;
	private int user2ID;
	private boolean isCompleted;
	private int winnerID;
	private int winnerScore;
	
	
	public Game(int gameID, int user1ID, int user2ID, boolean isCompleted, int winnerID, int winnerScore) {
		setGameID(gameID);
		setUser1ID(user1ID);
		setUser2ID(user2ID);
		setCompleted(isCompleted);
		setWinnerID(winnerID);
		setWinnerScore(winnerScore);
	}
	
	public Game(int user1ID, int user2ID, boolean isCompleted, int winnerID, int winnerScore) {
		setUser1ID(user1ID);
		setUser2ID(user2ID);
		setCompleted(isCompleted);
		setWinnerID(winnerID);
		setWinnerScore(winnerScore);
	}
	
	public int getGameID() {
		return gameID;
	}
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	public int getUser1ID() {
		return user1ID;
	}
	public void setUser1ID(int user1id) {
		user1ID = user1id;
	}
	public int getUser2ID() {
		return user2ID;
	}
	public void setUser2ID(int user2id) {
		user2ID = user2id;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public int getWinnerID() {
		return winnerID;
	}
	public void setWinnerID(int winnerID) {
		this.winnerID = winnerID;
	}
	public int getWinnerScore() {
		return winnerScore;
	}
	public void setWinnerScore(int winnerScore) {
		this.winnerScore = winnerScore;
	}
	@Override
	public String toString() {
		return "Game [gameID=" + gameID + ", user1ID=" + user1ID + ", user2ID="
				+ user2ID + ", isCompleted=" + isCompleted + ", winnerID="
				+ winnerID + ", winnerScore=" + winnerScore + "]";
	}
	
	

}
