package model;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userID;
	private String userEmail;
	private String userName;
	private String password;
	private int bestScore;
	
	
	public User(int userID, String userEmail, String userName, String password, int bestScore) {
		setUserID(userID);
		setUserEmail(userEmail);
		setUserName(userName);
		setPassword(password);
		setBestScore(bestScore);
	}
	
	public User(String userEmail, String userName, String password, int bestScore) {
		setUserEmail(userEmail);
		setUserName(userName);
		setPassword(password);
		setBestScore(bestScore);
	}
	
	
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getBestScore() {
		return bestScore;
	}
	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", userEmail=" + userEmail
				+ ", userName=" + userName + ", password=" + password
				+ ", bestScore=" + bestScore + "]";
	}
	

}
