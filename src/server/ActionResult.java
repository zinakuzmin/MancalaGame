package server;

public class ActionResult {
	private boolean actionSucceeded;
	private String message;
	
	
	public ActionResult(boolean actionSucceeded, String message){
		setActionSucceeded(actionSucceeded);
		setMessage(message);
	}
	
	

	public boolean isActionSucceeded() {
		return actionSucceeded;
	}


	public void setActionSucceeded(boolean actionSucceeded) {
		this.actionSucceeded = actionSucceeded;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}



}
