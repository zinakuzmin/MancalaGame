package server;

import java.util.ArrayList;

import protocol.ServerMakeMoveMsg;



public class GameController {
	private String player1SessionID;
	private String player2SessionID;
	private String playerTurn;
	private int[] pits;
	private GameStatusEnum gameStatus;
	private static final int NUMBER_OF_STONES = 6;
	private static final int PLAYER1_MANCALA_INDEX = 6;
	private static final int PLAYER2_MANCALA_INDEX = 13;
	private MancalaServerController controller;
	
	
	
	public GameController(MancalaServerController controller, String player1SessionID, String player2SessionID) {
		this.controller = controller;
		setPlayer1SessionID(player1SessionID);
		setPlayer2SessionID(player2SessionID);
		setGameStatus(GameStatusEnum.waiting);
		
		pits = new int[14];
		
		//init board
		for (int i = 0; i < pits.length; i++ ){
			if ( i != PLAYER1_MANCALA_INDEX && i != PLAYER2_MANCALA_INDEX){
				pits[i] = NUMBER_OF_STONES;
			}
		}
		
		
		
		
	}
	
	private boolean checkIfUserOnline(String playerSessionID){
		ArrayList<Client> onlineClients = controller.getOnlineClients();
		for (Client client : onlineClients) {
			if (client.getClientId().equals(playerSessionID))
				return true;
		}
		
		return false;
	}
	
	private void makeMove(String moveSessionID, int movePitIndex){
		boolean isMoveAllowed = false;
		String nextTurn = playerTurn;
		
		//If both users are online
		if (checkIfUserOnline(player2SessionID) && checkIfUserOnline(player2SessionID)){
			//check if this is user's turn
			if (moveSessionID.equals(playerTurn)){
				isMoveAllowed = true;
			}
		}
		else{
			gameStatus = GameStatusEnum.youWin;
			controller.registerGame(player1SessionID, player2SessionID, player1SessionID, pits[PLAYER1_MANCALA_INDEX]);
			
		}
		ServerMakeMoveMsg message = new ServerMakeMoveMsg(isMoveAllowed, pits, nextTurn, gameStatus);
		controller.sendMove(player1SessionID, player2SessionID, message);
	}
	
	
	private boolean isLastStoneMancala(String moveSessionID, int movePitIndex){
		boolean lastStoneMancala = false;
		int numberOfStones = 0;
		int startFrom = 0;
		if (moveSessionID.equals(player1SessionID)){
			startFrom = 0 + movePitIndex;
			if (numberOfStones % 15 == PLAYER1_MANCALA_INDEX - movePitIndex){
				lastStoneMancala = true;
			}
		}
		else if (moveSessionID.equals(player2SessionID)){
			startFrom = PLAYER1_MANCALA_INDEX+1+movePitIndex;
			if (numberOfStones % 15 == PLAYER2_MANCALA_INDEX - movePitIndex){
				lastStoneMancala = true;
			}
		}
		return lastStoneMancala;
	}
		
	
	//Player1 pits are 0-6, player2 pits 7-13
	private void updateBoard(String moveSessionID, int movePitIndex){
		System.out.println("Board before move" + pits);
		int startFrom = 0 + movePitIndex;
		if (moveSessionID.equals(player2SessionID))
			startFrom = PLAYER2_MANCALA_INDEX+1+movePitIndex;
		int numberOfStones = pits[startFrom];
		pits[startFrom++] = 0;
		while (numberOfStones > 0){
			if (startFrom >= 0 && startFrom < 14){
				pits[startFrom++]++;
				numberOfStones--;
			}
			if (startFrom == 14){
				startFrom = 0;
				pits[startFrom++]++;
				numberOfStones--;
				
			}
			
			
		}
		
		System.out.println("Board after move" + pits);
	}
	
	private boolean pitsAreEmpty(String moveSessionID){
		boolean pitsAreEmpty = true;
		if (moveSessionID.equals(player1SessionID)){
			for (int i = 0; i < PLAYER1_MANCALA_INDEX; i++){
				if (pits[i] != 0)
					pitsAreEmpty = false;
			}
		}
		else {
			for (int i = PLAYER1_MANCALA_INDEX+1; i < pits.length; i++){
				if (pits[i] != 0)
					pitsAreEmpty = false;
			}
			
		}
		
		return pitsAreEmpty;
	}
	
	
	public String getPlayer1SessionID() {
		return player1SessionID;
	}
	public void setPlayer1SessionID(String player1SessionID) {
		this.player1SessionID = player1SessionID;
	}
	public String getPlayer2SessionID() {
		return player2SessionID;
	}
	public void setPlayer2SessionID(String player2SessionID) {
		this.player2SessionID = player2SessionID;
	}
	public String getPlayerTurn() {
		return playerTurn;
	}
	public void setPlayerTurn(String playerTurn) {
		this.playerTurn = playerTurn;
	}
	public int[] getPits() {
		return pits;
	}
	public void setPits(int[] pits) {
		this.pits = pits;
	}
	public GameStatusEnum getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(GameStatusEnum gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	
	

}
