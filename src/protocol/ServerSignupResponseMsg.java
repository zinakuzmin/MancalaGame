package protocol;

import server.ActionResult;

public class ServerSignupResponseMsg extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActionResult result;
	
	public ServerSignupResponseMsg(ActionResult result) {
		setResult(result);
	}
	
	public ActionResult getResult() {
		return result;
	}
	public void setResult(ActionResult result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ServerSignupResponseMsg [result=" + result + "]";
	}

	
	
}
