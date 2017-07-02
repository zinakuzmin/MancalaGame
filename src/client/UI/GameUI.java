package client.UI;

import java.io.File;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import protocol.ClientMakeMoveMsg;
import client.ClientController;

public class GameUI extends Application implements Runnable {
	private Label myName;
	private Label opponentName;
	private TextArea gameStatusField;
	private HBox myPitsHBOX;
	private Button[] myPits;
	private HBox opponentPitsHBOX;
	private Button[] opponentPits;
	private Button myMancala;
	private Button opponentMancala;
	private ClientController clientController;
	private final int NUMBER_OF_STONES_IN_PIT = 4;
	private boolean isPlayer1 = false;
	private final int PLAYER1_MANCALA_INDEX = 6;
	private final int PLAYER2_MANCALA_INDEX = 13;
	private final int MANCALA_BOARD_SIZE = 14;
	private Button[] pits;
	private String nextTurn;

	public static void main(String[] args) {
		Application.launch();
	}

	public GameUI(ClientController clientController, boolean isPlayer1) {
		this.clientController = clientController;
		this.isPlayer1 = isPlayer1;
		this.nextTurn = clientController.getSessionID();
		initParams();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage stage) throws Exception {

		buildUI(stage);

		// Test
		// javafx.application.Platform.runLater(new Runnable() {
		// @Override public void run() {
		// // try {
		// // Thread.sleep(10_000);
		// // } catch (InterruptedException e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// int[] board = {2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		// makeMoveUI(board);
		// }
		// });

	}

	public void initParams() {
		clientController.setHasActiveGame(true);
		myName = new Label(clientController.getUser().getUserName());
		opponentName = new Label(clientController.getOpponentUserName());
		gameStatusField = new TextArea(clientController.getGameStatus()
				.toString());
		// myMancala = new Button(0 + "");
		// opponentMancala = new Button(0 + "");
		// myPits = new Button[6];
		// for (int i = 0; i < myPits.length; i++) {
		// myPits[i] = new Button(NUMBER_OF_STONES_IN_PIT + "");
		// }
		//
		// opponentPits = new Button[6];
		// for (int i = 0; i < opponentPits.length; i++) {
		// opponentPits[i] = new Button(NUMBER_OF_STONES_IN_PIT + "");
		// }

		pits = new Button[MANCALA_BOARD_SIZE];
		for (int i = 0; i < pits.length; i++) {
			if (i != PLAYER1_MANCALA_INDEX && i != PLAYER2_MANCALA_INDEX) {
				pits[i] = new Button(NUMBER_OF_STONES_IN_PIT + "");
//				pits[i].setMaxSize(100, 100);
				pits[i].setMaxWidth(Double.MAX_VALUE);
				pits[i].setMaxHeight(Double.MAX_VALUE);
				HBox.setHgrow(pits[i], Priority.ALWAYS);
				VBox.setVgrow(pits[i], Priority.ALWAYS);
//				pits[i] = new Button(i + "");

			} else {
				pits[i] = new Button(0 + "");
				pits[i].setMaxWidth(Double.MAX_VALUE);
				pits[i].setMaxHeight(Double.MAX_VALUE);
				HBox.setHgrow(pits[i], Priority.ALWAYS);
				VBox.setVgrow(pits[i], Priority.ALWAYS);
//				pits[i].setMaxSize(100, 100);				
			}
		}
	}

	public void buildUI(Stage stage) {
		Image titleIcon = new Image(new File("icon.jpg").toURI().toString());
		stage.getIcons().add(titleIcon);
		
		

		myPitsHBOX = new HBox();
		// myPitsHBOX.getChildren().addAll(myPits);
		myPitsHBOX.getChildren().addAll(myName);
		opponentPitsHBOX = new HBox();
		// opponentPitsHBOX.getChildren().addAll(opponentPits);
		opponentPitsHBOX.getChildren().addAll(opponentName);
		
		
//		HBox.setHgrow(pits[0], Priority.ALWAYS);
//		HBox.setHgrow(pits[1], Priority.ALWAYS);
//		pits[0].setMaxWidth(Double.MAX_VALUE);
//		pits[1].setMaxWidth(Double.MAX_VALUE);
//		button2.setMaxWidth(Double.MAX_VALUE);
		

		BorderPane mainPane = new BorderPane();
		
		
		BackgroundImage myBI= new BackgroundImage(new Image(new File("background.jpg").toURI().toString(),630,630,false,true),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		//then you set to your node
		mainPane.setBackground(new Background(myBI));
		

		// if (amIPlayer1(clientController.getSessionID())){
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
		// mainPane.setRight(myMancala);
//		gameStatusField.setMaxWidth(30);
		gameStatusField.setMaxSize(300, 200);
		mainPane.setCenter(gameStatusField);
		// mainPane.setLeft(opponentMancala);
		Scene scene = new Scene(mainPane, 630, 630);
		stage.setScene(scene);

		stage.setTitle("Mancala Game");

		stage.show();

		// myMancala.setDisable(true);
		// opponentMancala.setDisable(true);
		// for (int j = 0; j < opponentPits.length; j++) {
		// opponentPits[j].setDisable(true);
		// }
		//
		// for (int i = 0; i < myPits.length; i++) {
		// myPits[i].setOnAction(new ButtonListener(clientController
		// .getSessionID(), i));
		// }
		for (int i = 0; i < pits.length; i++) {
			pits[i].setOnAction(new ButtonListener(clientController
					.getSessionID(), i));
		}
		
		skinButton();
	}
	
	
	private void skinButton(){
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
		
		
//		  -fx-background-color: 
//		        #090a0c,
//		        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),
//		        linear-gradient(#20262b, #191d22),
//		        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));
//		    -fx-background-radius: 5,4,3,5;
//		    -fx-background-insets: 0,1,2,0;
//		    -fx-text-fill: white;
//		    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );
//		    -fx-font-family: "Arial";
//		    -fx-text-fill: linear-gradient(white, #d0d0d0);
//		    -fx-font-size: 12px;
//		    -fx-padding: 10 20 10 20;
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

	// public void makeMoveUI(int[] board, boolean isGameCompleted) {
	//
	// }

	public void disableMove() {
		for (int i = 0; i < pits.length; i++) {
			pits[i].setDisable(true);
		}
	}

	public void enableMove() {
		if (isPlayer1) {
			for (int i = 0; i < pits.length / 2 - 2; i++) {
				pits[i].setDisable(false);

			}
		}
		else{
			for (int i = PLAYER1_MANCALA_INDEX +1 ; i < PLAYER2_MANCALA_INDEX; i++) {
				pits[i].setDisable(false);

			}
		}
	}

	public void makeMoveUI(int[] board, boolean isGameCompleted) {
		gameStatusField.setText(clientController.getGameStatus().toString());

		for (int i = 0; i < board.length; i++) {
			pits[i].setText(board[i] + "");
		}

		if (nextTurn.equals(clientController.getSessionID())) {
			gameStatusField.setText("Next turn: "
					+ clientController.getUser().getUserName());

		} else {
			gameStatusField.setText("Next turn: "
					+ clientController.getOpponentUserName());

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
							+ board[PLAYER1_MANCALA_INDEX] + "points");
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

	public Button[] getMyPits() {
		return myPits;
	}

	public void setMyPits(Button[] myPits) {
		this.myPits = myPits;
	}

	public HBox getOpponentPitsHBOX() {
		return opponentPitsHBOX;
	}

	public void setOpponentPitsHBOX(HBox opponentPitsHBOX) {
		this.opponentPitsHBOX = opponentPitsHBOX;
	}

	public Button[] getOpponentPits() {
		return opponentPits;
	}

	public void setOpponentPits(Button[] opponentPits) {
		this.opponentPits = opponentPits;
	}

	public Button getMyMancala() {
		return myMancala;
	}

	public void setMyMancala(Button myMancala) {
		this.myMancala = myMancala;
	}

	public Button getOpponentMancala() {
		return opponentMancala;
	}

	public void setOpponentMancala(Button opponentMancala) {
		this.opponentMancala = opponentMancala;
	}

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
