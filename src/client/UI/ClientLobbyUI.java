package client.UI;


import java.sql.ResultSet;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.db.DBHandler;
import client.ClientController;

public class ClientLobbyUI extends Application{
	private ClientController clientController;
	private Label lblWelcome = new Label("Welcome user");
	
	
	private Scene sceneChat;
	private TextArea taViewMsg = new TextArea();
	private Label lblUserName = new Label("User Name:");
	private TextField tfNewMsg = new TextField();
	private Button btnStartGame = new Button("Start Game");
	private ListView<String> lvOnlineUsers = new ListView<String>();

	private Stage stage;

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
		
		lblWelcome.setText(clientController.getUser().getUserName());
		onlineUsersList.add("Zina");
		onlineUsersList.add("Dima");
		buildBehavior();
		buildActivityViewer(stage);

	}
	
	public void buildBehavior() {
		lvOnlineUsers.setItems(onlineUsersList);
	}
	

	
	public void buildActivityViewer(Stage stage) {

		stage.setTitle("Mancala Game");

		Group root = new Group();
		Scene scene = new Scene(root, 500, 500, Color.WHITE);

		TabPane tabPane = new TabPane();

		BorderPane borderPane = new BorderPane();

		tabPane.getTabs().add(createStartGameTab(scene));
		tabPane.getTabs().add(createDBViewerTab());

		// bind to take available space
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());

		borderPane.setCenter(tabPane);
		root.getChildren().add(borderPane);
		stage.setScene(scene);
//		stage.show();
		
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                e.consume();
            }
        });
		stage.show();

	}

	
	private Tab createStartGameTab(Scene scene) {
		Tab startGameTab = new Tab();
		startGameTab.setClosable(false);

//		Pane pane = new Pane();
//
//		Label activityLabel = new Label("View activity log");
//		pane.getChildren().add(activityLabel);
//
//		pane.prefWidthProperty().bind(scene.widthProperty());
//		pane.prefHeightProperty().bind(scene.heightProperty());
//
//		ScrollPane scroll = new ScrollPane();
//
//		scroll.layoutXProperty().bind(
//				pane.widthProperty().divide(2)
//						.subtract(scroll.widthProperty().divide(2)));
//		scroll.layoutYProperty().bind(
//				pane.heightProperty().divide(2)
//						.subtract(scroll.heightProperty().divide(2)));
//		scroll.setPrefSize(400, 400);
//
//		textArea.prefWidthProperty().bind(scroll.widthProperty());
//		textArea.prefHeightProperty().bind(scroll.heightProperty());
//		
//		scroll.setContent(textArea);
//
//		pane.getChildren().addAll(scroll);
		
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



}
