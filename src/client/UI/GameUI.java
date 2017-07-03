package client.UI;

import java.io.File;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import protocol.ClientAbortedGameMsg;
import protocol.ClientMakeMoveMsg;
import client.ClientController;

public class GameUI extends Application implements Runnable {
	private TextArea gameStatusField;
	private HBox myPitsHBOX;
	private HBox opponentPitsHBOX;
	private ClientController clientController;
	private final int NUMBER_OF_STONES_IN_PIT = 4;
	private boolean isPlayer1 = false;
	private final int PLAYER1_MANCALA_INDEX = 6;
	private final int PLAYER2_MANCALA_INDEX = 13;
	private final int MANCALA_BOARD_SIZE = 14;
	private Button[] pits;
	private String nextTurn;
	private Stage stage;

	public static void main(String[] args) {
		Application.launch();
	}

	public GameUI(ClientController clientController, boolean isPlayer1, Stage stage) {
		this.clientController = clientController;
		this.isPlayer1 = isPlayer1;
		this.nextTurn = "";
		this.stage = stage;
		initParams();

	}

	
	@Override
	public void start(Stage stage) throws Exception {
		buildUI();
	}

	public void initParams() {
		clientController.setHasActiveGame(true);
		gameStatusField = new TextArea();

		if (clientController.getGameStatus()!= null)
			gameStatusField.setText(clientController.getGameStatus().toString());
		

		pits = new Button[MANCALA_BOARD_SIZE];
		for (int i = 0; i < pits.length; i++) {
			if (i != PLAYER1_MANCALA_INDEX && i != PLAYER2_MANCALA_INDEX) {
				pits[i] = new Button(NUMBER_OF_STONES_IN_PIT + "");
				pits[i].setMaxWidth(Double.MAX_VALUE);
				pits[i].setMaxHeight(Double.MAX_VALUE);
				HBox.setHgrow(pits[i], Priority.ALWAYS);
				VBox.setVgrow(pits[i], Priority.ALWAYS);

			} else {
				pits[i] = new Button(0 + "");
				pits[i].setMaxWidth(Double.MAX_VALUE);
				pits[i].setMaxHeight(Double.MAX_VALUE);
				HBox.setHgrow(pits[i], Priority.ALWAYS);
				VBox.setVgrow(pits[i], Priority.ALWAYS);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void buildUI() {
		Image titleIcon = new Image(new File("icon.jpg").toURI().toString());
		stage.getIcons().add(titleIcon);
		myPitsHBOX = new HBox();
		opponentPitsHBOX = new HBox();
			
		BorderPane mainPane = new BorderPane();
		
		
		BackgroundImage myBI= new BackgroundImage(new Image(new File("background.jpg").toURI().toString(),630,630,false,true),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);

		mainPane.setBackground(new Background(myBI));
		

		if (isPlayer1) {
			// add buttons 0 - 5 to my side
			for (int i = 0; i < (pits.length / 2) - 1; i++) {
				myPitsHBOX.getChildren().add(pits[i]);
			}
			// add buttons 7 - 12 to opponent side
			for (int i = pits.length - 2; i >= pits.length / 2; i--) {
				opponentPitsHBOX.getChildren().add(pits[i]);
				pits[i].setDisable(true);
			}

			mainPane.setRight(pits[PLAYER1_MANCALA_INDEX]);
			mainPane.setLeft(pits[PLAYER2_MANCALA_INDEX]);

		} else {
			// add buttons 0 - 5 to opponent side
			for (int i = (pits.length / 2) - 2; i >= 0; i--) {
				opponentPitsHBOX.getChildren().add(pits[i]);
				pits[i].setDisable(true);
			}
			// add buttons 7 - 12 to my side
			for (int i = pits.length / 2; i < pits.length - 1; i++) {
				myPitsHBOX.getChildren().add(pits[i]);
			}

			mainPane.setRight(pits[PLAYER2_MANCALA_INDEX]);
			mainPane.setLeft(pits[PLAYER1_MANCALA_INDEX]);

		}
		pits[PLAYER1_MANCALA_INDEX].setDisable(true);
		pits[PLAYER2_MANCALA_INDEX].setDisable(true);

		mainPane.setTop(opponentPitsHBOX);
		mainPane.setBottom(myPitsHBOX);
		gameStatusField.setMaxSize(300, 200);
		mainPane.setCenter(gameStatusField);
		Scene scene = new Scene(mainPane, 630, 630);
		stage.setScene(scene);

		stage.setTitle("Mancala Game: " + clientController.getUser().getUserName() + " VS " + clientController.getOpponentUserName());

		stage.show();

		//Set listeners
		for (int i = 0; i < pits.length; i++) {
			pits[i].setOnAction(new ButtonListener(clientController
					.getSessionID(), i));
		}
		
		skinButtons();
		
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				try {
					clientController.sendMessageToServer(new ClientAbortedGameMsg(clientController.getSessionID(), clientController.getOpponentSessionID()));
					ClientLobbyUI lobby = new ClientLobbyUI(clientController);
					javafx.application.Platform.runLater(lobby);
					stage.close();
					clientController.resetGame();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	
	private void skinButtons(){
		for (int i = 0; i < pits.length; i++){
			pits[i].setStyle("-fx-background-color:"
        + "#090a0c,"
        + "linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),"
        + "linear-gradient(#20262b, #191d22),"
        + "radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));"
    + "-fx-background-radius: 5,4,3,5;"
    + "-fx-background-insets: 0,1,2,0;"
    + "-fx-text-fill: white;"
    + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );"
    + "-fx-text-fill: linear-gradient(white, #d0d0d0);"
    + "-fx-font-size: 30px;"
    + "-fx-padding: 10 20 10 20;");
		}
		
	}

	/**
	 * @param playerSession
	 * @return 0 if playerSession is game initiator - player1, otherwise return
	 *         1
	 */
	public boolean amIPlayer1(String playerSession) {
		if (clientController.getGameInitiator().equals(playerSession))
			return true;
		else
			return false;

	}

	

	public void disableMove() {
		for (int i = 0; i < pits.length; i++) {
			pits[i].setDisable(true);
		}
	}

	public void enableMove() {
		if (isPlayer1) {
			for (int i = 0; i <= pits.length / 2 - 2; i++) {
				pits[i].setDisable(false);

			}
		}
		else{
			for (int i = PLAYER1_MANCALA_INDEX + 1  ; i < PLAYER2_MANCALA_INDEX; i++) {
				pits[i].setDisable(false);

			}
		}
	}
	
	public void abortGame(){
		try{
			disableMove();
			gameStatusField.setText("Opponent has left the game");
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	

	public void makeMoveUI(boolean moveAllowed, int[] board, boolean isGameCompleted) {
		gameStatusField.setText(clientController.getGameStatus().toString());

		if (moveAllowed){
			for (int i = 0; i < board.length; i++) {
				pits[i].setText(board[i] + "");
			}
			
		}

		System.out.println("Next turn is: " + nextTurn + " mySession is: " + clientController.getSessionID());
		if (nextTurn.equals(clientController.getSessionID())) {
			gameStatusField.setText("Your turn");
			enableMove();

		} else {
			gameStatusField.setText("Opponent's turn");
			disableMove();

		}

		if (isGameCompleted) {
			for (int i = 0; i < board.length; i++) {
				pits[i].setDisable(true);
			}
			if (isPlayer1) {
				if (board[PLAYER1_MANCALA_INDEX] > board[PLAYER2_MANCALA_INDEX]) {
					gameStatusField.setText("You win! "
							+ board[PLAYER1_MANCALA_INDEX] + "points");
				} else if (board[PLAYER1_MANCALA_INDEX] < board[PLAYER2_MANCALA_INDEX]) {
					gameStatusField.setText("You lost! ");
				} else {
					gameStatusField.setText("It's a tie! ");
				}
			} else {
				if (board[PLAYER2_MANCALA_INDEX] > board[PLAYER1_MANCALA_INDEX]) {
					gameStatusField.setText("You win! "
							+ board[PLAYER2_MANCALA_INDEX] + "points");
				} else if (board[PLAYER2_MANCALA_INDEX] < board[PLAYER1_MANCALA_INDEX]) {
					gameStatusField.setText("You lost! ");
				} else {
					gameStatusField.setText("It's a tie! ");
				}

			}
		}

	}

	public TextArea getGameStatusField() {
		return gameStatusField;
	}

	public void setGameStatusField(TextArea gameStatusField) {
		this.gameStatusField = gameStatusField;
	}

	public HBox getMyPitsHBOX() {
		return myPitsHBOX;
	}

	public void setMyPitsHBOX(HBox myPitsHBOX) {
		this.myPitsHBOX = myPitsHBOX;
	}

	public HBox getOpponentPitsHBOX() {
		return opponentPitsHBOX;
	}

	public void setOpponentPitsHBOX(HBox opponentPitsHBOX) {
		this.opponentPitsHBOX = opponentPitsHBOX;
	}


	@SuppressWarnings("rawtypes")
	class ButtonListener implements EventHandler {
		private String playerSessionID;
		private int movePitIndex;

		public ButtonListener(String playerSessionID, int movePitIndex) {
			this.playerSessionID = playerSessionID;
			this.movePitIndex = movePitIndex;
		}

		@Override
		public void handle(Event arg0) {
			clientController.sendMessageToServer(new ClientMakeMoveMsg(
					clientController.getSessionID(), clientController
							.getOpponentSessionID(), playerSessionID,
					movePitIndex));

		}

	}

	@Override
	public void run() {
		try {
			start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isPlayer1() {
		return isPlayer1;
	}

	public void setPlayer1(boolean isPlayer1) {
		this.isPlayer1 = isPlayer1;
	}

	public String getNextTurn() {
		return nextTurn;
	}

	public void setNextTurn(String nextTurn) {
		this.nextTurn = nextTurn;
	}
}
