package client.UI;

import java.io.File;

import client.ClientController;
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
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientSignupUI extends Application{
	private ClientController clientController;
	private Text signupResult;
	
	public ClientSignupUI(ClientController clientController) {
		this.clientController = clientController;
		signupResult = new Text();
	}
	
	public static void main(String[] args) {
		Application.launch();
//		start(new Stage());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Sign up page");

		buildSignUp(primaryStage);
		primaryStage.show();

	}

	public void buildSignUp(Stage primaryStage) {

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Scene scene = new Scene(grid, 630, 630);
		primaryStage.setScene(scene);
		
		BackgroundImage myBI= new BackgroundImage(new Image(new File("background2.jpg").toURI().toString(),630,630,false,true),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		//then you set to your node
		grid.setBackground(new Background(myBI));

		
		Text scenetitle = new Text("");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("User Name:");
		userName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		grid.add(userName, 0, 1);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);
		
		Label userEmail = new Label("User Email:");
		userEmail.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		grid.add(userEmail, 0, 2);

		TextField userEmailField = new TextField();
		grid.add(userEmailField, 1, 2);


		Label pw = new Label("Password:");
		pw.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		grid.add(pw, 0, 3);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 3);
		
		Button btn = new Button("Sign up");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);
		
		Button btnBackButton = new Button("Back to login");
		HBox hbBtnBackButton = new HBox(10);
		hbBtnBackButton.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtnBackButton.getChildren().add(btnBackButton);
		grid.add(hbBtnBackButton, 0, 5);
		
		
//		final Text actiontarget = new Text();
        grid.add(signupResult, 1, 6);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent e) {

				String username = userTextField.getText();
				String userEmail = userEmailField.getText();
				String password = pwBox.getText();

				clientController.sendSignupMessage(username, userEmail, password);

				while (clientController.getSignupResult() == null) {

					System.out.println(clientController.getSignupResult());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}

				if (!clientController.getSignupResult().isActionSucceeded()) {
					System.out.println("Result not null!");
					signupResult.setFill(Color.FIREBRICK);
					signupResult.setText(clientController.getSignupResult()
							.getMessage());
					clientController.setSignupResult(null);
				} else {
					signupResult.setText("Signed up");
				}

			
            }
        });
        
        
        btnBackButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ClientLoginUI login = new ClientLoginUI(clientController);
				try {
					login.start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
        	
		});

	}

}



