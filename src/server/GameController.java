package server;

import java.util.ArrayList;
import java.util.Arrays;

import protocol.ServerMakeMoveMsg;

public class GameController {
	private String player1SessionID;
	private String player2SessionID;
	private String playerTurn;
	private int[] pits;
	private GameStatusEnum gameStatus;
	private static final int NUMBER_OF_STONES = 4;
	private static final int PLAYER1_MANCALA_INDEX = 6;
	private static final int PLAYER2_MANCALA_INDEX = 13;
	private MancalaServerController controller;

	public GameController(MancalaServerController controller,
			String player1SessionID, String player2SessionID) {
		this.controller = controller;
		setPlayer1SessionID(player1SessionID);
		setPlayer2SessionID(player2SessionID);
		setGameStatus(GameStatusEnum.waiting);
		setPlayerTurn(player1SessionID);

		pits = new int[14];

		// init board
		for (int i = 0; i < pits.length; i++) {
			if (i != PLAYER1_MANCALA_INDEX && i != PLAYER2_MANCALA_INDEX) {
				pits[i] = NUMBER_OF_STONES;
			}
		}

	}

	private boolean checkIfUserOnline(String playerSessionID) {
		ArrayList<Client> onlineClients = controller.getOnlineClients();
		for (Client client : onlineClients) {
			if (client.getClientId().equals(playerSessionID))
				return true;
		}

		return false;
	}

	public void makeMove(String moveSessionID, int movePitIndex) {
		boolean isMoveAllowed = false;
		String nextTurn = playerTurn;

		// If both users are online
		if ((checkIfUserOnline(player2SessionID)
				&& checkIfUserOnline(player2SessionID)) && movePitIndex != -1) {
			// check if this is user's turn
			if (moveSessionID.equals(playerTurn)) {

				// check if game is not completed and there are stones on both sides
				if (gameStatus.equals(GameStatusEnum.inProgress)
						&& !pitsAreEmpty(player1SessionID)
						&& !pitsAreEmpty(player2SessionID)) {

					// check index validity
					if (moveSessionID.equals(player1SessionID)
							&& movePitIndex >= 0
							&& movePitIndex < PLAYER1_MANCALA_INDEX) {
						isMoveAllowed = true;

					}

					else if (moveSessionID.equals(player2SessionID)
							&& movePitIndex >= PLAYER1_MANCALA_INDEX + 1
							&& movePitIndex < PLAYER2_MANCALA_INDEX) {
						isMoveAllowed = true;

					}

					else
						isMoveAllowed = false;

					if (isMoveAllowed){
						if (isLastStoneMancala(moveSessionID, movePitIndex)) {
							nextTurn = moveSessionID;
							
						} else {
							if (moveSessionID.equals(player1SessionID))
								nextTurn = player2SessionID;
							else
								nextTurn = player1SessionID;
						}
						
						
						// Update board
						updateBoard(moveSessionID, movePitIndex);
					}
					

				} else {
					//At least one of sides is empty, game is completed
					gameStatus = GameStatusEnum.competed;
				}

			}
		} else {
			gameStatus = GameStatusEnum.aborted;
	

		}
//		System.out.println("Board after update");
//		printBoard(pits);
		if (isLastMoveCompletedTheGame()){
			gameStatus = GameStatusEnum.competed;
			
		}
		ServerMakeMoveMsg message = new ServerMakeMoveMsg(isMoveAllowed,pits,nextTurn, player1SessionID, player2SessionID,gameStatus);
		controller.sendMove(player1SessionID, player2SessionID, message);
		setPlayerTurn(nextTurn);
		
		if (gameStatus.equals(GameStatusEnum.competed) || gameStatus.equals(GameStatusEnum.aborted) || gameStatus.equals(GameStatusEnum.declined)){
//			controller.registerGame(player1SessionID, player2SessionID,player1SessionID, pits[PLAYER1_MANCALA_INDEX]);
//			gameStatus = GameStatusEnum.competed;
			if (pits[PLAYER1_MANCALA_INDEX] > pits[PLAYER2_MANCALA_INDEX]) {
				controller.registerGame(player1SessionID,
						player2SessionID, player1SessionID,
						pits[PLAYER1_MANCALA_INDEX]);

			} else if (pits[PLAYER1_MANCALA_INDEX] < pits[PLAYER2_MANCALA_INDEX]) {
				controller.registerGame(player1SessionID,
						player2SessionID, player2SessionID,
						pits[PLAYER2_MANCALA_INDEX]);

			}
			
			controller.gameCompleted(this);
//			if (gameStatus.equals(GameStatusEnum.aborted) || gameStatus.equals(GameStatusEnum.declined)){
////				controller.gameCompleted(player1SessionID, player2SessionID);
//				controller.gameCompleted(this);
//			}
					
			
			
			
		}
			
	}
	
	public boolean isLastMoveCompletedTheGame(){
		if (pitsAreEmpty(player1SessionID) || pitsAreEmpty(player2SessionID))
			return true;
		return false;
	}

	private boolean isLastStoneMancala(String moveSessionID, int movePitIndex) {
		boolean lastStoneMancala = false;
		int numberOfStones = 0;
		int startFrom = 0;
		startFrom = 0 + movePitIndex;
		if (moveSessionID.equals(player1SessionID)) {
			numberOfStones = pits[startFrom];
			if (numberOfStones % 15 == PLAYER1_MANCALA_INDEX - movePitIndex) {
				lastStoneMancala = true;

			}
		} else if (moveSessionID.equals(player2SessionID)) {
//			startFrom = PLAYER1_MANCALA_INDEX + 1 + movePitIndex;
			numberOfStones = pits[startFrom];
			if (numberOfStones % 15 == PLAYER2_MANCALA_INDEX - movePitIndex) {
				lastStoneMancala = true;
			}
		}
		return lastStoneMancala;
	}

	// Player1 pits are 0-5, player2 pits 7-12
	private void updateBoard(String moveSessionID, int movePitIndex) {
		System.out.println("Board before move \n");
		printBoard(pits);

		int startFrom = movePitIndex;
		int numberOfStones = pits[startFrom];
		pits[startFrom++] = 0;
		while (numberOfStones > 0) {

			if (startFrom >= 0 && startFrom < 14) {
				// Check if last stone on empty pit
				if (numberOfStones == 1) {
					int numOfStonesInLastPit = pits[startFrom];
					if (numOfStonesInLastPit == 0){
						if (startFrom != PLAYER1_MANCALA_INDEX && startFrom != PLAYER2_MANCALA_INDEX){
							pits[startFrom]++;
							numberOfStones--;
							int oppositePit = getPitOnOtherSide(startFrom);
							int numOfStonesInOppositePit = pits[oppositePit];
							putStonesInMyMancala(moveSessionID,
									numOfStonesInOppositePit);
							pits[oppositePit] = 0;
						}
						else {
							pits[startFrom++]++;
							numberOfStones--;
							
					}
							
					}
					else {
						pits[startFrom++]++;
						numberOfStones--;
					}
				}
				else {
					pits[startFrom++]++;
					numberOfStones--;
					
				}
			}
			else if (startFrom == 14) {
				startFrom = 0;
				pits[startFrom++]++;
				numberOfStones--;

			}

		}
		boardIsValidTest();
		System.out.println("Board after move \n");
		printBoard(pits);
	}

	private void printBoard(int[] board) {
		System.out.println("pits = " + Arrays.toString(board));
	}

	private boolean pitsAreEmpty(String moveSessionID) {
		boolean pitsAreEmpty = true;
		if (moveSessionID.equals(player1SessionID)) {
			for (int i = 0; i < PLAYER1_MANCALA_INDEX; i++) {
				if (pits[i] != 0){
					pitsAreEmpty = false;
					break;
				}
				
			}
		} else {
			for (int i = PLAYER1_MANCALA_INDEX + 1; i < pits.length - 1; i++) {
				if (pits[i] != 0){
					pitsAreEmpty = false;
					break;
				}
			}

		}

		return pitsAreEmpty;
	}

	
	private int getPitOnOtherSide(int pitIndex) {
		int oppositSide = -1;
		switch (pitIndex) {
		case 0:
			oppositSide = 12;
			break;
		case 1:
			oppositSide = 11;
			break;
		case 2:
			oppositSide = 10;
			break;
		case 3:
			oppositSide = 9;
			break;
		case 4:
			oppositSide = 8;
			break;
		case 5:
			oppositSide = 7;
			break;
		case 6:
			oppositSide = 6;
			break;
		case 7:
			oppositSide = 5;
			break;
		case 8:
			oppositSide = 4;
			break;
		case 9:
			oppositSide = 3;
			break;
		case 10:
			oppositSide = 2;
			break;
		case 11:
			oppositSide = 1;
			break;
		case 12:
			oppositSide = 0;
			break;

		default:
			break;
		}

		return oppositSide;
	}

	public boolean isLastStoneOnMySide(String playerID, int moveIndex) {
		boolean lastStoneOnMySide = false;
		if (playerID.equals(player1SessionID) && moveIndex >= 0
				&& moveIndex < PLAYER1_MANCALA_INDEX) {
			lastStoneOnMySide = true;
		} else if (playerID.equals(player2SessionID)
				&& moveIndex > PLAYER1_MANCALA_INDEX
				&& moveIndex < PLAYER2_MANCALA_INDEX)
			lastStoneOnMySide = true;
		return lastStoneOnMySide;
	}

	public void putStonesInMyMancala(String playerId, int numberOfStones) {
		if (playerId.equals(player1SessionID)) {
			pits[PLAYER1_MANCALA_INDEX] += numberOfStones;
		} else if (playerId.equals(player2SessionID)) {
			pits[PLAYER2_MANCALA_INDEX] += numberOfStones;
		} else {
			System.out.println("invalid playerID");
		}
	}

	private void boardIsValidTest() {
		int sumOfStones = 0;
		for (int i = 0; i < pits.length; i++) {
			sumOfStones += pits[i];
		}
		if (NUMBER_OF_STONES * 12 == sumOfStones)
			System.out.println("Numbers of stones on the board is correct: "
					+ sumOfStones);
		if (NUMBER_OF_STONES * 12 != sumOfStones)
			System.out.println("Numbers of stones on the board is wrong: "
					+ sumOfStones);
	}

	public boolean isPlayerInGame(String playerSessionID) {
		boolean inGame = false;
		if (playerSessionID.equals(player1SessionID)
				|| playerSessionID.equals(player2SessionID))
			inGame = true;
		return inGame;
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
