package client.UI;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import protocol.ClientMakeMoveMsg;
import client.ClientController;

public class GameUI extends Application {
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

	public static void main(String[] args) {
		Application.launch();
	}

	
	public GameUI(ClientController clientController) {
		this.clientController = clientController;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage stage) throws Exception {
		myName = new Label("My Name");
		opponentName = new Label("Opponent name");
		gameStatusField = new TextArea("Status is here");
		myMancala = new Button("My mancala");
		opponentMancala = new Button("Opponent mancala");
		myPits = new Button[6];
		for (int i = 0; i < myPits.length; i++) {
			myPits[i] = new Button(i + "");
		}

		opponentPits = new Button[6];
		for (int i = 0; i < opponentPits.length; i++) {
			opponentPits[i] = new Button(i + "");
		}

		myPitsHBOX = new HBox();
		myPitsHBOX.getChildren().addAll(myPits);
		myPitsHBOX.getChildren().addAll(myName);
		opponentPitsHBOX = new HBox();
		opponentPitsHBOX.getChildren().addAll(opponentPits);
		opponentPitsHBOX.getChildren().addAll(opponentName);

		BorderPane mainPane = new BorderPane();

		mainPane.setTop(opponentPitsHBOX);
		mainPane.setBottom(myPitsHBOX);
		mainPane.setRight(myMancala);
		mainPane.setCenter(gameStatusField);
		mainPane.setLeft(opponentMancala);

		Scene scene = new Scene(mainPane, 300, 275);
		stage.setScene(scene);

		stage.setTitle("Mancala Game");

		stage.show();
		
		
		myMancala.setDisable(true);
		opponentMancala.setDisable(true);
		for (int j = 0; j < opponentPits.length; j++){
			opponentPits[j].setDisable(true);
		}
		
		
		for ( int i = 0; i < myPits.length; i++){
			myPits[i].setOnAction(new ButtonListener(clientController.getSessionID(), i));
		}
		
		
		
		
		//Test
		javafx.application.Platform.runLater(new Runnable() {
            @Override public void run() {
//            	try {
//        			Thread.sleep(10_000);
//        		} catch (InterruptedException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
            	int[] board = {2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        		makeMoveUI(board);
            }
        });
		

	}

	
	public void makeMove(){
		
	}
	
	public void makeMoveUI(int[] board) {
		int i;
		//Set my pits
		for (i = 0; i < (board.length / 2) - 1; i++) {
			myPits[i].setText(board[i] + "");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

		//Set myMancala
		if (i == 6)
			myMancala.setText(board[i++] + "");
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		int j = 0;
		for (i = 7; i < board.length - 1; i++) {
			opponentPits[j++].setText(board[i] + "");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		if (i == 13)
			opponentMancala.setText(board[i] + "");

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

	
	class ButtonListener implements EventHandler{
		private String playerSessionID;
		private int movePitIndex;
		
		public ButtonListener(String playerSessionID, int movePitIndex) {
			this.playerSessionID = playerSessionID;
			this.movePitIndex = movePitIndex;
		}

		@Override
		public void handle(Event arg0) {
			clientController.sendMessageToServer(new ClientMakeMoveMsg(playerSessionID, movePitIndex));
			
		}
		
	}
}
