package runner;

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
//						ServerController controller = new ServerController(new Stage());
						
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				serverBtn.setDisable(true);
				clientBtn.setDisable(false);

			});
		} else {
			serverBtn.setDisable(true);
			clientBtn.setDisable(false);
//			ServerController controller = new ServerController(new Stage());
		}

		clientBtn.setOnAction(e -> {
			try {
				
//				ZRaceGameController raceClient = new ZRaceGameController(new Stage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		
		});

		HBox btnHBox = new HBox(50);
		btnHBox.getChildren().addAll(serverBtn, clientBtn);

		BorderPane pane = new BorderPane();

		pane.setCenter(btnHBox);

//		BackgroundImage myBI = new BackgroundImage(new Image(
//				GlobalParams.IMG_RUNNER_BACKGROUND),
//				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
//				BackgroundPosition.CENTER, new BackgroundSize(500, 150, false,
//						false, false, false));
//		pane.setBackground(new Background(myBI));

		btnHBox.translateXProperty().bind(pane.widthProperty().divide(4));
		btnHBox.translateYProperty().bind(pane.heightProperty().add(-100));

		Scene scene = new Scene(pane, 500, 150);
		primaryStage.setTitle("Mancala Game!"); // main pane title
		primaryStage.setScene(scene);
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
