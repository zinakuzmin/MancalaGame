package server.view;
import java.sql.ResultSet;

import server.db.DBHandler;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class ServerUI extends Application{

	
	/**
	 * Text area for server logs
	 */
	private TextArea textArea;

	/**
	 * Initialize server UI
	 * @param logActivity
	 */
	public ServerUI(TextArea logActivity) {
		this.textArea = logActivity;
	}


	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * Start ServerMainView application
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		buildActivityViewer(arg0);
		

	}

	/**
	 * Build UI for server
	 * @param stage
	 */
	public void buildActivityViewer(Stage stage) {

		stage.setTitle("Zrace server");

		Group root = new Group();
		Scene scene = new Scene(root, 500, 500, Color.WHITE);

		TabPane tabPane = new TabPane();

		BorderPane borderPane = new BorderPane();

		tabPane.getTabs().add(createActivityTab(scene));
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

	/**
	 * Create activity tab UI
	 * @param scene
	 * @return Tab
	 */
	private Tab createActivityTab(Scene scene) {
		Tab activityTab = new Tab();
		activityTab.setClosable(false);

		Pane pane = new Pane();

		Label activityLabel = new Label("View activity log");
		pane.getChildren().add(activityLabel);

		pane.prefWidthProperty().bind(scene.widthProperty());
		pane.prefHeightProperty().bind(scene.heightProperty());

		ScrollPane scroll = new ScrollPane();

		scroll.layoutXProperty().bind(
				pane.widthProperty().divide(2)
						.subtract(scroll.widthProperty().divide(2)));
		scroll.layoutYProperty().bind(
				pane.heightProperty().divide(2)
						.subtract(scroll.heightProperty().divide(2)));
		scroll.setPrefSize(400, 400);

		textArea.prefWidthProperty().bind(scroll.widthProperty());
		textArea.prefHeightProperty().bind(scroll.heightProperty());
		
		scroll.setContent(textArea);

		pane.getChildren().addAll(scroll);

		activityTab.setContent(pane);

		activityTab.setText("View activity log");
		return activityTab;

	}

	/**
	 * Create db viewer tab UI
	 * @return Tab
	 */
	private Tab createDBViewerTab() {
		DBHandler db = new DBHandler();
		TableView<?> tableView = new TableView<Object>();
		Button btShowContents = new Button("Show Contents");
		Label lblStatus = new Label();
//
//		ComboBox<String> comboBox = new ComboBox<>(setComboboxOptions());
//		comboBox.setVisibleRowCount(7);
//		comboBox.getSelectionModel().selectFirst();

		HBox hBox = new HBox(5);
		hBox.getChildren().add(new Label("DB selector"));
//		hBox.getChildren().add(comboBox);
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
//		btShowContents.setOnAction(e -> showContents(comboBox.getValue(),
//				tableView, db));
		return dbViewerTab;

	}

	/**
	 * Set ObservableList of combo box options
	 * @return ObservableList
	 */
//	private ObservableList<String> setComboboxOptions() {
//		ObservableList<String> options = FXCollections.observableArrayList(
//				DataBaseViewer.GET_CARS_QUERY,
//				DataBaseViewer.GET_RACE_BETS_QUERY,
//				DataBaseViewer.GET_RACES_STATS_QUERY,
//				DataBaseViewer.GET_SYSTEM_BALANCE_QUERY,
//				DataBaseViewer.GET_USERS_BALANCE_QUERY,
//				DataBaseViewer.GET_REVENUE_BY_RACE_QUERY);
//	
//
//		return options;
//	}

	
	/**
	 * Show relevant data according to user choice in combobox
	 * @param comboboxChoise
	 * @param tableView
	 */
	@SuppressWarnings("rawtypes")
	private void showContents(String comboboxChoise, TableView tableView, DBHandler db) {
		ResultSet result = db.getAllUsers();
//		ResultSet result = null;
//		if (comboboxChoise.equals(DataBaseViewer.GET_CARS_QUERY)) {
//			result = db.getAllCarsStatistics();
//		} else if (comboboxChoise
//				.equals(DataBaseViewer.GET_SYSTEM_BALANCE_QUERY)) {
//			result = db.getAllSystemRevenue();
//		} else if (comboboxChoise
//				.equals(DataBaseViewer.GET_USERS_BALANCE_QUERY)) {
//			result = db.getAllUsersRevenue();
//		} else if (comboboxChoise
//				.equals(DataBaseViewer.GET_RACES_STATS_QUERY)) {
//			result = db.getRacesBets();
//		} else if (comboboxChoise
//				.equals(DataBaseViewer.GET_RACE_BETS_QUERY)) {
//			result = db.getAllBets();
//		} else if (comboboxChoise
//				.equals(DataBaseViewer.GET_USERS_BALANCE_QUERY)) {
//			result = db.getUsersBalance();
//		} else if (comboboxChoise
//				.equals(DataBaseViewer.GET_REVENUE_BY_RACE_QUERY)) {
//			result = db.getRevenueByRace();
//		}
//		else{
//			try {
//				throw new Exception("Invalid string set as quesy request");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		
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
//		tableView.getColumns().clear();
//		ObservableList<ObservableList> data = FXCollections
//				.observableArrayList();
//		try {
//
//			/**********************************
//			 * TABLE COLUMN ADDED DYNAMICALLY *
//			 **********************************/
//			for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
//				// We are using non property style for making dynamic table
//				final int j = i;
//				TableColumn col = new TableColumn(resultSet.getMetaData()
//						.getColumnName(i + 1));
//
//				col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
//					public ObservableValue<String> call(
//							CellDataFeatures<ObservableList, String> param) {
//						if (param.getValue().get(j) != null)
//							return new SimpleStringProperty(param.getValue()
//									.get(j).toString());
//						else
//							return null;
//					}
//				});
//
//				tableView.getColumns().addAll(col);
////				System.out.println("Column [" + i + "] ");
//			}
//
//			/********************************
//			 * Data added to ObservableList *
//			 ********************************/
//			while (resultSet.next()) {
//				// Iterate Row
//				ObservableList<String> row = FXCollections
//						.observableArrayList();
//				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
//					// Iterate Column
//					row.add(resultSet.getString(i));
//				}
////				System.out.println("Row [1] added " + row);
//				data.add(row);
//
//			}
//
//			// FINALLY ADDED TO TableView
//			tableView.setItems(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Error on Building Data");
//		}
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

