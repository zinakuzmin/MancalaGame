package protocol;

public class ClientSignupMsg extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String userEmail;
	private String password;
	
	
	public ClientSignupMsg(String username,String userEmail, String password) {
		setUsername(username);
		setUserEmail(userEmail);
		setPassword(password);
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String toString() {
		return "ClientSignupMsg [username=" + username + ", userEmail="
				+ userEmail + ", password=" + password + "]";
	}
	
	

}
