package homePage;

import java.io.File;
import java.nio.file.Paths;
import diary.Diary;
import important.Important;
import important.SignUp;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import settings.Settings;


public class HomePage extends Application{
	public static Stage stage;
	private static boolean justOpened=true;
	public static void main(String[] args){
		launch(args);
	}

	public void start(Stage window){
		stage=window;
		stage.setScene(new Scene(new VBox(), 800, 500));
		stage.getIcons().add(new Image("file:res/asset/icon/diary-icon.png"));
		stage.show();
		if(Important.getStringFromTextFile(Paths.get("res/FirstTimeCheck.txt")).equals("true")) SignUp.show(800.0, 500.0);
		else if(Important.getStringFromTextFile(Paths.get("res/home page/password enable check.txt")).equals("true")) Login.show(800, 500);
		else show(800, 500);
	}
	
	public static void show(double width, double height) {
		stage.setResizable(true);
		String currentPath="res/home page/";
		
		VBox layout=new VBox(5);
		 
		Scene scene=new Scene(layout, width, height);
		scene.getStylesheets().add("homePage/HomePage.css");
		stage.setScene(scene);
		
		HBox row1=new HBox();
		row1.setPadding(new  Insets(10));
		Button settings=new Button("Settings");
		settings.setOnAction(e-> Settings.show(stage.getScene().getWidth(), stage.getScene().getHeight()));
		row1.getChildren().addAll(Important.hSpacer(), settings);
		
		HBox titleRow=new HBox();
		titleRow.setAlignment(Pos.CENTER);
		Label title=new Label("Home Page");
		title.getStyleClass().add("label-title");
		title.setScaleX(3);
		title.setScaleY(3);
		titleRow.setPadding(new Insets(20, 0, 0, 0));
		title.setPadding(new Insets(0, 0, 20, 0));
		titleRow.getChildren().add(title);
		
		HBox row=new HBox();
		row.setAlignment(Pos.CENTER);
		TableView<Diary> table=new TableView<Diary>();
		Label tablePrompt=new Label("Empty");
		tablePrompt.setScaleX(2);
		tablePrompt.setScaleY(2);
		table.setPlaceholder(tablePrompt);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.prefWidthProperty().bind(row.widthProperty().subtract(200));
		table.prefHeightProperty().bind(scene.heightProperty().subtract(100));
		TableColumn<Diary,ImageView> imageColumn=new TableColumn("");
		imageColumn.setCellValueFactory(new PropertyValueFactory<>("diaryIcon"));
		TableColumn<Diary,String> titleColumn=new TableColumn("Title");
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<Diary,String> createdDateColumn=new TableColumn("Created");
		createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAndTime"));
		TableColumn<Diary,String> visitedDateColumn=new TableColumn("Last Visited");
		visitedDateColumn.setCellValueFactory(new PropertyValueFactory<>("lastVisit"));
		TableColumn<Diary,Integer> pageNumberColumn=new TableColumn("Pages");
		pageNumberColumn.setCellValueFactory(new PropertyValueFactory<>("pageNumber"));
		table.getColumns().addAll(imageColumn, titleColumn, createdDateColumn, visitedDateColumn, pageNumberColumn);
		titleColumn.setStyle( "-fx-alignment: CENTER-LEFT;");
		pageNumberColumn.setStyle( "-fx-alignment: CENTER;");
		imageColumn.setStyle( "-fx-alignment: CENTER;");
		createdDateColumn.setStyle( "-fx-alignment: CENTER;");
		visitedDateColumn.setStyle( "-fx-alignment: CENTER;");
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		createdDateColumn.setMaxWidth(1800);
		visitedDateColumn.setMaxWidth(1800);
		pageNumberColumn.setMaxWidth(600);
		imageColumn.setMaxWidth(200);
		Button delete=new Button();
		delete.getStyleClass().add("button-transparent");
		delete.setOnMouseEntered(e->{
			delete.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-trash-filled-20.png")));
			delete.getStyleClass().remove("button-transparent");
			delete.getStyleClass().add("button-enteredTransparent");
			delete.setOnMouseExited(e1->{
				delete.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-trash-filled-white20.png")));
				delete.getStyleClass().remove("button-enteredTransparent");
				delete.getStyleClass().add("button-transparent");
			});
		});
		
		delete.setOnMouseExited(e-> delete.getStyleClass().add("button-transparent"));
		delete.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-trash-filled-white20.png")));
		delete.setOnAction(e-> {
			ObservableList<Diary>listOfDiary=table.getSelectionModel().getSelectedItems();
			for(Diary diary:listOfDiary) {
				Important.deleteDirectory(new File(currentPath+diary.getTitle()+"#"+diary.getDateAndTime()));
			}
			table.getItems().clear();
			loadFiles(currentPath, table);
		});
		row.getChildren().addAll(table, delete);
		
		HBox lastRow=new HBox();
		lastRow.setPadding(new Insets(30));
		lastRow.setAlignment(Pos.CENTER);
		Button newDiary=new Button("New Diary");
		newDiary.setScaleX(2);
		newDiary.setScaleY(2);
		
		newDiary.setOnAction(e->{
			Stage stage=new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			VBox titleLayout=new VBox(10);
			titleLayout.setAlignment(Pos.CENTER);
			HBox innerRow0=new HBox();
			innerRow0.setPadding(new Insets(20));
			innerRow0.setAlignment(Pos.CENTER);
			Label titleLabel=new Label("Diary Name");
			titleLabel.setScaleX(1.3);
			titleLabel.setScaleY(1.3);
			innerRow0.getChildren().add(titleLabel);
			HBox innerRow1=new HBox();
			innerRow1.setAlignment(Pos.CENTER);
			TextField titleField=new TextField();
			innerRow1.getChildren().add(titleField);
			
			HBox innerRow2=new HBox(5);
			innerRow2.setAlignment(Pos.CENTER);
			Button ok=new Button("create");
			ok.setOnAction(e1-> {
				String restrictedKey="? | # * : < > / \\ \"";
				if(titleField.getText().contains("#")) Important.popupShow("Title can not contain "+restrictedKey, 1500, 300, 100);
				else {
					try {
						add(titleField.getText(), currentPath, table, titleColumn, createdDateColumn, visitedDateColumn, pageNumberColumn);
						stage.close();
						Important.disableButton(newDiary, 1000);
					} catch(Exception ex) {
						Important.popupShow("Title can not contain "+restrictedKey, 1500, 300, 100);
					}
				}
			});
			titleField.setOnAction(ok.getOnAction());
			Button cancel=new Button("Cancel");
			cancel.setOnAction(e1-> stage.close());
			innerRow2.getChildren().addAll(ok, cancel);
			titleLayout.getChildren().addAll(innerRow0, innerRow1, innerRow2);
			Scene titleScene=new Scene(titleLayout,400,200);
			titleScene.getStylesheets().add("homePage/FlashScreen.css");
			stage.setScene(titleScene);
			stage.showAndWait();
		});
		
		
		lastRow.getChildren().add(newDiary);
		table.setOnMouseClicked(e-> {
			try {
				if(e.getClickCount()==2 && e.getButton()==MouseButton.PRIMARY) {
					Diary diary=(Diary) table.getSelectionModel().getSelectedItem();
					String path=currentPath+diary.getTitle()+"#"+diary.getDateAndTime()+"/";
					Important.writeToTextFile(new File(path+"last visit.txt"), Important.getDateAndTime());
					Diary.show(path, scene.getWidth(), scene.getHeight());
				}
			} catch(Exception ex) {}
		});
		
		layout.getChildren().addAll(row1, titleRow, row, lastRow);
		stage.setTitle("Diary");
		loadFiles(currentPath, table);
		if(justOpened==true) {
			stage.setMaximized(true);
			justOpened=false;
		}
	}

	private static void loadFiles(String currentPath, TableView table){
		File[] listOfFiles=Important.getFolderAndFileName(currentPath);
		int size=listOfFiles.length;
		
		for(int i=0; i<size; i++) {
			if(listOfFiles[i].isFile()) continue;
			String folderName=listOfFiles[i].getName();
			
			String[] folderNames=listOfFiles[i].getName().split("#");
			Diary diary=new Diary();
			diary.setTitle(folderNames[0]);
			diary.setDateAndTime(folderNames[1]);
			diary.setLastVisit(Important.getStringFromTextFile(Paths.get(currentPath+folderName+"/last visit.txt")));
			String pageNumber=Important.getStringFromTextFile(Paths.get(currentPath+folderName+"/pages.txt"));
			diary.setPageNumber(Integer.parseInt(pageNumber));
			table.getItems().add(diary);
		}
	}
	
	private static void add(String title, String currentPath, TableView table, TableColumn titleColumn, TableColumn createdDateColumn, TableColumn visitedDateColumn, TableColumn pageNumberColumn) throws Exception{
		String curDate=Important.getDateAndTime();
		Diary diary=new Diary();
		if(title.equals("")) diary.setTitle("Diary");
		else diary.setTitle(title);
		String path=currentPath+diary.getTitle()+"#"+curDate+"/";
		Important.createFolder(path);
		Important.createTextFile(path+"pages.txt");
		Important.createTextFile(path+"last visit.txt");
		Important.writeToTextFile(new File(path+"last visit.txt"), curDate);
		Important.writeToTextFile(new File(path+"pages.txt"), "0");
		diary.setDateAndTime(curDate);
		diary.setLastVisit(curDate);
		diary.setPageNumber(0);
		table.getItems().add(diary);
	}
	
}
