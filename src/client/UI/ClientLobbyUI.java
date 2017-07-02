package client.UI;


import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Map;

import protocol.ClientDisconnectMsg;
import protocol.ClientStartGameMsg;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.GameStatusEnum;
import server.db.DBHandler;
import client.ClientController;
import client.OnlineUsersListener;

public class ClientLobbyUI extends Application implements Runnable{
	private ClientController clientController;
	private Label lblWelcome = new Label("Welcome user");
	private Button btnStartGame = new Button("Start Game");
	private ListView<String> lvOnlineUsers = new ListView<String>();
	private Stage stage;
	private boolean RunOnlineUsersListener = true;

//	private Stage stage;

	private ObservableList<String> onlineUsersList = FXCollections
			.observableArrayList();
	
	
	
	public static void main(String[] args) {
		Application.launch();
		// start(new Stage());
	}

//	public ClientLobbyUI() {
//		onlineUsersList.add("Zina");
//		onlineUsersList.add("Dima");
//		buildBehavior();
//		// TODO Auto-generated constructor stub
//	}
	
	public ClientLobbyUI(ClientController clientController) {
		this.clientController = clientController;
		
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		lblWelcome.setText(clientController.getUser().getUserName());
		buildBehavior();
		buildActivityViewer(stage);
		
		OnlineUsersListener onlineUsersListener = new OnlineUsersListener(this);
		new Thread(onlineUsersListener).start();
		

	}
	
	public void buildBehavior() {
//		lvOnlineUsers.setItems((ObservableList<String>) clientController.getOnlineUsers());
		lvOnlineUsers.setItems(onlineUsersList);
//		setOnlineUsers();
	}
	
	public void setOnlineUsers(){
		System.out.println("UPdating user list");
		System.out.println("Before " + onlineUsersList);
		Map<String, String> onlineUsersFromServer = clientController.getOnlineUsers();
		
		
		System.out.println("List from server " + onlineUsersFromServer);
		for (String key : onlineUsersFromServer.keySet()) {
			if (!key.equals(clientController.getSessionID()) && !(onlineUsersList.contains(onlineUsersFromServer.get(key))))
				onlineUsersList.add(onlineUsersFromServer.get(key));
		}
		System.out.println("After " + onlineUsersList);
		
	}
	
	
	
	public void buildActivityViewer(Stage stage) {

		stage.setTitle("Mancala Game");
		Image titleIcon = new Image(new File("icon.jpg").toURI().toString());
		stage.getIcons().add(titleIcon);

		Group root = new Group();
		Scene scene = new Scene(root, 630, 630, Color.GRAY);

		TabPane tabPane = new TabPane();

		BorderPane borderPane = new BorderPane();
		
		BackgroundImage myBI= new BackgroundImage(new Image(new File("runnerbck.jpg").toURI().toString(),630,630,false,true),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		//then you set to your node
		borderPane.setBackground(new Background(myBI));
		
		

		tabPane.getTabs().add(createStartGameTab(scene));
		tabPane.getTabs().add(createDBViewerTab());

		// bind to take available space
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());

		borderPane.setCenter(tabPane);
		root.getChildren().add(borderPane);
		stage.setScene(scene);
//		stage.show();
		
		btnStartGame.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				//clientController.setOpponentUserName(opponentUserName);
				String selectedOpponent = lvOnlineUsers.getSelectionModel().getSelectedItem();
				clientController.setOpponentUserName(selectedOpponent);
				clientController.setOpponentSessionID(clientController.findOpponentByUsername(selectedOpponent));
				GameUI game = new GameUI(clientController, true);
//				game.setPlayer1(true);
				clientController.setTheGame(game);
				clientController.setGameStatus(GameStatusEnum.waiting);
				clientController.sendMessageToServer(new ClientStartGameMsg(clientController.getSessionID(), clientController.getOpponentSessionID(), false));
				try {
					game.start(stage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				try {
					setRunOnlineUsersListener(false);
					clientController.sendMessageToServer(new ClientDisconnectMsg());
					stage.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
//		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent e) {
//                e.consume();
//            }
//        });
		stage.show();

	}

	
	private Tab createStartGameTab(Scene scene) {
		Tab startGameTab = new Tab();
		startGameTab.setClosable(true);	
		
		lvOnlineUsers.getSelectionModel().setSelectionMode(
				SelectionMode.SINGLE);

		BorderPane mainPane = new BorderPane();
		mainPane.setTop(lblWelcome);
		mainPane.setBottom(btnStartGame);
//		mainPane.setRight(lvOnlineUsers);
		mainPane.setCenter(lvOnlineUsers);

//		sceneChat = new Scene(mainPane);

		startGameTab.setContent(mainPane);

		startGameTab.setText("Start Game Tab");
		return startGameTab;

	}

	/**
	 * Create db viewer tab UI
	 * @return Tab
	 */
	private Tab createDBViewerTab() {
		DBHandler db = new DBHandler();
		TableView<?> tableView = new TableView<Object>();
		Button btShowContents = new Button("Show Scores");
		Label lblStatus = new Label();

		HBox hBox = new HBox(5);
		hBox.getChildren().add(btShowContents);
		hBox.setAlignment(Pos.CENTER);
		BorderPane pane = new BorderPane();
		pane.setCenter(tableView);
		pane.setTop(hBox);
		pane.setBottom(lblStatus);

		Tab dbViewerTab = new Tab();
		dbViewerTab.setClosable(false);
		dbViewerTab.setContent(pane);
		dbViewerTab.setText("View DB statistics");
		btShowContents.setOnAction(e -> showContents(tableView, db));
		return dbViewerTab;

	}
	
private void showContents(TableView tableView, DBHandler db) {
		
		ResultSet result = db.getAllUsersOrderByScore();
		if (result != null){
			populateTableView(result, tableView);
			
		}

	}

	/**
	 * Generate dynamically table view by data in result set
	 * @param 
	 * @param 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateTableView(ResultSet resultSet, TableView tableView) {
	
		tableView.getColumns().clear();
		tableView.setItems(FXCollections.observableArrayList());
		try { 
			int numOfColums = resultSet.getMetaData().getColumnCount();
			for(int i = 1 ; i <= numOfColums ; i++)
			{
				final int columNum = i - 1;
				TableColumn<String[],String> column = new TableColumn<>(resultSet.getMetaData().getColumnLabel(i));
				column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[columNum]));
//				column.setCellFactory(e -> new EditCell());
				column.setEditable(false);
				tableView.getColumns().add(column);
			}
			while(resultSet.next())
			{
				String[] cells = new String[numOfColums];
				for(int i = 1 ; i <= numOfColums ; i++)
					cells[i - 1] = resultSet.getString(i);
				tableView.getItems().add(cells);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
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

	public boolean isRunOnlineUsersListener() {
		return RunOnlineUsersListener;
	}

	public void setRunOnlineUsersListener(boolean runOnlineUsersListener) {
		RunOnlineUsersListener = runOnlineUsersListener;
	}

}
