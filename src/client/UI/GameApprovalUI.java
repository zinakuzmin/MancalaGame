package client.UI;

import protocol.ClientStartGameMsg;
import client.ClientController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameApprovalUI extends Application implements Runnable{
	private ClientController clientController;
	private Stage stage;
	
	
	public static void main(String[] args) {
		Application.launch();
	}
	
	
	public GameApprovalUI(ClientController clientController) {
		this.clientController = clientController;
	}
	
	

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setTitle("Mancala Game");

		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Scene scene = new Scene(grid, 300, 275);
		stage.setScene(scene);
//
//		Text scenetitle = new Text("Welcome");
//		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//		grid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("Player " + clientController.getOpponentUserName() + " wants to start the game ");
		grid.add(userName, 0, 1);
		
		Button approve = new Button("Approve");
		Button decline = new Button("Decline");
		
		grid.add(approve, 0, 2);
		grid.add(decline, 1, 2);
		
		stage.show();
		
		
		
		
		approve.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				clientController.sendMessageToServer(new ClientStartGameMsg(clientController.getOpponentSessionID(), clientController.getSessionID(), true));
				stage.close();
//				GameUI game  = new GameUI(clientController);
//				try {
//					game.start(new Stage());
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
			}
		});
		
		
		decline.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				clientController.sendMessageToServer(new ClientStartGameMsg(clientController.getOpponentSessionID(), clientController.getSessionID(), false));
				stage.close();
				
			}
		});
		
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

}
