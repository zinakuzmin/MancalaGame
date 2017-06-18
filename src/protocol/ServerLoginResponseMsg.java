package protocol;

import model.User;
import server.ActionResult;

public class ServerLoginResponseMsg extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActionResult result;
	private User user;
	
	
	public ServerLoginResponseMsg(ActionResult result, User user) {
		setResult(result);
		setUser(user);
	}


	public ActionResult getResult() {
		return result;
	}


	public void setResult(ActionResult result) {
		this.result = result;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return "ServerLoginResponseMsg [result=" + result + ", user=" + user
				+ "]";
	}

	
	
}
