package server;

import java.io.Serializable;

public class ActionResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean actionSucceeded;
	private String message;
	private Object object;
	
	

	public ActionResult(boolean actionSucceeded, String message){
		setActionSucceeded(actionSucceeded);
		setMessage(message);
	}
	
	public ActionResult(boolean actionSucceeded, String message, Object object){
		setActionSucceeded(actionSucceeded);
		setMessage(message);
		setObject(object);
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


	public Object getObject() {
		return object;
	}



	public void setObject(Object object) {
		this.object = object;
	}

	

	@Override
	public String toString() {
		return "ActionResult [actionSucceeded=" + actionSucceeded
				+ ", message=" + message + "]";
	}

	


}
