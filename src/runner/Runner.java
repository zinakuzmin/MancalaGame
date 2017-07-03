package runner;

import java.io.File;

import client.ClientController;
import client.UI.ClientLobbyUI;
import server.MancalaServerController;
import server.db.DBBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Main runner - to execute Mancala game
 * @author Zina K
 *
 */
public class Runner extends Application {

	/**
	 * Create the client and open the client view
	 */
	private Button clientBtn = new Button("Start Client");

	/**
	 * Create the server socket and open the Server view
	 */
	private Button serverBtn = new Button("Start Server");

	/**
	 * runs the main stage of the application
	 * 
	 * @param primaryStage
	 *            - main stage of the application.
	 * @throws Exception
	 */


	@Override
	public void start(Stage primaryStage) throws Exception {
		clientBtn.setDisable(true);

		if (GlobalParams.SHOULD_INIT_DB)
			DBBuilder.runScript();

		if (!GlobalParams.AUTOMATIC_SERVER_START) {
			serverBtn.setOnAction(e -> {
				
					try {
						MancalaServerController controller = new MancalaServerController(new Stage());
						
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				serverBtn.setDisable(true);
				clientBtn.setDisable(false);

			});
		} else {
			serverBtn.setDisable(true);
			clientBtn.setDisable(false);
			MancalaServerController controller = new MancalaServerController(new Stage());
		}
		
		
		if (GlobalParams.FAST_START){
			
			try{
				
				ClientController clientController1 = new ClientController(new Stage());
				clientController1.sendLoginMessage("zina1@email.com", "password");
				ClientLobbyUI lobbyZina1 = new ClientLobbyUI(clientController1);
				Platform.runLater(lobbyZina1);
				
				
				
				ClientController clientController2 = new ClientController(new Stage());
				clientController2.sendLoginMessage("zina2@email.com", "password");
				ClientLobbyUI lobbyZina2 = new ClientLobbyUI(clientController2);
				Platform.runLater(lobbyZina2);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}

		clientBtn.setOnAction(e -> {
			try {
				ClientController clientController = new ClientController(new Stage());
//				ZRaceGameController raceClient = new ZRaceGameController(new Stage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		
		});

		HBox btnHBox = new HBox(50);
		btnHBox.getChildren().addAll(serverBtn, clientBtn);

		BorderPane pane = new BorderPane();

		pane.setCenter(btnHBox);

		btnHBox.translateXProperty().bind(pane.widthProperty().divide(6));
		btnHBox.translateYProperty().bind(pane.heightProperty().add(-100));

		Scene scene = new Scene(pane, 300, 150);
		primaryStage.setTitle("Mancala Game!"); // main pane title
		primaryStage.setScene(scene);
		
		Image titleIcon = new Image(new File("icon.jpg").toURI().toString());
		primaryStage.getIcons().add(titleIcon);
		
		BackgroundImage myBI= new BackgroundImage(new Image(new File("runnerbck.jpg").toURI().toString(),300,150,false,true),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		//then you set to your node
		pane.setBackground(new Background(myBI));
		
		
		
		primaryStage.show(); 
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
	}


	/**
	 * Runner main
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
