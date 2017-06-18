package client.UI;

import java.io.IOException;

import protocol.ClientConnectMsg;
import protocol.ClientDisconnectMsg;
import client.ClientController;

import com.sun.media.jfxmediaimpl.platform.Platform;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientLoginUI extends Application {
	private ClientController clientController;
	private Text loginResult;

	public ClientLoginUI(ClientController clientController) {
		this.clientController = clientController;
		loginResult = new Text();

	}

	public static void main(String[] args) {
		Application.launch();
		// start(new Stage());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("JavaFX Welcome");

		primaryStage.show();
		buildLogin(primaryStage);

	}

	public void buildLogin(Stage primaryStage) {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);

		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("User Name:");
		grid.add(userName, 0, 1);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);

		Button btn = new Button("Sign in");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);

		Label signUplbl = new Label("Not registered yet? ");
		grid.add(signUplbl, 0, 5);

		Button btnSignUp = new Button("Sign up");
		HBox hbBtnSignUp = new HBox(10);
		hbBtnSignUp.setAlignment(Pos.BOTTOM_LEFT);
		hbBtnSignUp.getChildren().add(btnSignUp);
		grid.add(hbBtnSignUp, 1, 5);

		// final Text actiontarget = new Text();
		grid.add(loginResult, 1, 6);

		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				String username = userTextField.getText();
				String password = pwBox.getText();

				clientController.sendLoginMessage(username, password);

				while (clientController.getLoginResult() == null) {

					System.out.println(clientController.getLoginResult());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

				if (!clientController.getLoginResult().isActionSucceeded()) {
					System.out.println("Result not null!");
					loginResult.setFill(Color.FIREBRICK);
					loginResult.setText(clientController.getLoginResult()
							.getMessage());
					clientController.setLoginResult(null);
				} else {
					ClientLobbyUI lobby = new ClientLobbyUI(clientController);
					try {
						lobby.start(primaryStage);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					loginResult.setText("Logged in");
				}

			}
		});

		btnSignUp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ClientSignupUI signup = new ClientSignupUI(clientController);
				try {
					signup.start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				try {
					clientController.getServerListener().stopServerListener();
					clientController.getOut().writeObject(
							new ClientDisconnectMsg());
					// clientController.getSocket().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

}