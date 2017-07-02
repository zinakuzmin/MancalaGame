package protocol;

import java.io.Serializable;

public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int messageID = 0;
	
	
	public Message() {
		messageID++;
	}
	public static int getMessageID() {
		return messageID;
	}
	public static void setMessageID(int messageID) {
		Message.messageID = messageID;
	}
	@Override
	public String toString() {
		return "MessageID " + messageID;
	}
	
	
	
	
//	Protocol messages:
//		SignUpRequestMsg
//		SignUpResponceMsg
//		LogInRequestMsg
//		LogInResponceMsg	
//		OnlineUsersMsg
//		StartGameRequestMsg
//		StartGameResponseMsg
//		MakeMoveRequestMsg
//		MakeMoveResponseMsg
//		GameEndedMsg
//		ViewScoresRequestMsg
//		ViewScoresResponseMsg



}
