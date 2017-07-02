package protocol;

public class ClientLoginMsg extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	
	
	public ClientLoginMsg(String username, String password) {
		super();
		setUsername(username);
		setPassword(password);
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "ClientLoginMsg [username=" + username + ", password="
				+ password + "]";
	}
	
	

}
