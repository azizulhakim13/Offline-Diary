package diary;

import java.io.File;
import java.nio.file.Paths;
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
import homePage.HomePage;
import diaryPage.Page;
import important.Important;

public class Diary {
	private String title="";
	private String dateAndTime="";
	private String lastVisit="";
	private Integer pageNumber=0;
	private ImageView diaryIcon=new ImageView(new Image("file:res/asset/icon/icons8-spiral-bound-booklet-15.png"));
	
	public static void show(String currentPath, Double width, Double height) {
		VBox layout=new VBox(5);
		Scene scene=new Scene(layout, width, height);
		scene.getStylesheets().add("homePage/HomePage.css");
		HomePage.stage.setScene(scene);
		
		HBox row1=new HBox();
		row1.setPadding(new Insets(10));
		Button back=new Button("Back");
		back.setOnAction(e-> HomePage.show(scene.getWidth(), scene.getHeight()));
		Button settings=new Button("Settings");
		
		settings.setOnAction(e-> Settings.show(scene.getWidth(), scene.getHeight()));
		row1.getChildren().addAll(back, Important.hSpacer(), settings);
		
		HBox titleRow=new HBox();
		titleRow.setAlignment(Pos.CENTER);
		File currentFolder=new File(currentPath);
		String names[]=currentFolder.getName().split("#");
		Label title=new Label(names[0]);
		title.getStyleClass().add("label-title");
		title.setScaleX(3);
		title.setScaleY(3);
		titleRow.setPadding(new Insets(20, 0, 0, 0));
		title.setPadding(new Insets(0, 0, 20, 0));
		titleRow.getChildren().add(title);
		
		HBox row=new HBox();
		row.setAlignment(Pos.CENTER);
		TableView<Page> table=new TableView<Page>();
		Label tablePrompt=new Label("Click Add New Page to add page to this diary");
		tablePrompt.setScaleX(1.5);
		tablePrompt.setScaleY(1.5);
		table.setPlaceholder(tablePrompt);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.prefWidthProperty().bind(row.widthProperty().subtract(200));
		table.prefHeightProperty().bind(scene.heightProperty().subtract(100));
		TableColumn titleColumn=new TableColumn("Title");
		titleColumn.setCellValueFactory(new PropertyValueFactory<Page,String>("title"));
		TableColumn createdDateColumn=new TableColumn("Created");
		createdDateColumn.setCellValueFactory(new PropertyValueFactory<Page,String>("createdDateAndTime"));
		TableColumn visitedDateColumn=new TableColumn("Last Visited");
		visitedDateColumn.setCellValueFactory(new PropertyValueFactory<Page,String>("lastVisitedDateAndTime"));
		TableColumn imageColumn=new TableColumn("");
		imageColumn.setCellValueFactory(new PropertyValueFactory<Page, ImageView>("pageIcon"));
		table.getColumns().addAll(imageColumn, titleColumn, createdDateColumn, visitedDateColumn);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		createdDateColumn.setMaxWidth(1600);
		visitedDateColumn.setMaxWidth(1600);
		imageColumn.setMaxWidth(180);
		createdDateColumn.setStyle("-fx-alignment: CENTER;");
		titleColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		visitedDateColumn.setStyle("-fx-alignment: CENTER;");
		imageColumn.setStyle("-fx-alignment: CENTER;");
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
		delete.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-trash-filled-white20.png")));
		delete.setOnAction(e-> {
			ObservableList<Page>listOfPage=table.getSelectionModel().getSelectedItems();
			for(Page page:listOfPage) {
				Important.deleteDirectory(new File(currentPath+page.getTitle()+"#"+page.getCreatedDateAndTime()));
			}
			String number=Important.getStringFromTextFile(Paths.get(currentPath+"pages.txt"));
			int pageNumber=Integer.parseInt(number)-listOfPage.size();
			Important.writeToTextFile(new File(currentPath+"pages.txt"), Integer.toString(pageNumber));
			table.getItems().clear();
			loadFiles(currentPath, table);
		});
		row.getChildren().addAll(table, delete);
		
		HBox lastRow=new HBox();
		lastRow.setPadding(new Insets(30));
		lastRow.setAlignment(Pos.CENTER);
		Button newPage=new Button("Add New Page");
		newPage.setScaleX(2);
		newPage.setScaleY(2);
		
		
		newPage.setOnAction(e->{
			Stage stage=new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			VBox titleLayout=new VBox(10);
			titleLayout.setAlignment(Pos.CENTER);
			HBox innerRow0=new HBox();
			innerRow0.setPadding(new Insets(20));
			innerRow0.setAlignment(Pos.CENTER);
			Label titleLabel=new Label("Page Name");
			titleLabel.setScaleX(1.3);
			titleLabel.setScaleY(1.3);
			innerRow0.getChildren().add(titleLabel);
			HBox innerRow1=new HBox();
			innerRow1.setAlignment(Pos.CENTER);
			TextField titleField=new TextField();
			innerRow1.getChildren().add(titleField);
			
			HBox innerRow2=new HBox(5);
			innerRow2.setAlignment(Pos.CENTER);
			Button ok=new Button("Add");

			ok.setOnAction(e1-> {
				String restrictedKey="? | # * : < > / \\ \"";
				if(titleField.getText().contains("#")) Important.popupShow("Title can not contain "+restrictedKey, 1500, 300, 100);
				else {
					try {
						add(titleField.getText(), currentPath, table, titleColumn, createdDateColumn, visitedDateColumn);
						stage.close();
						Important.disableButton(newPage, 1000);
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
		
		
		lastRow.getChildren().add(newPage);
		table.setOnMouseClicked(e-> {
			try {
				if(e.getClickCount()==2 && e.getButton()==MouseButton.PRIMARY) {
					Page page=(Page) table.getSelectionModel().getSelectedItem();
					String path=currentPath+page.getTitle()+"#"+page.getCreatedDateAndTime()+"/";
					Important.writeToTextFile(new File(path+"last visit.txt"), Important.getDateAndTime());
					Page.show(path, scene.getWidth(), scene.getHeight());
				}
			} catch(Exception ex) {}
		});
		
		layout.getChildren().addAll(row1, titleRow, row, lastRow);
		loadFiles(currentPath, table);
	}
	
	private static void add(String title, String currentPath, TableView table, TableColumn titleColumn, TableColumn createdDateColumn, TableColumn visitedDateColumn) {
		String curDate=Important.getDateAndTime();
		Page page=new Page();
		if(title.equals("")) page.setTitle("Page");
		else page.setTitle(title);
		String path=currentPath+page.getTitle()+"#"+curDate+"/";
		Important.createFolder(path);
		Important.createTextFile(path+"text content.txt");
		Important.createTextFile(path+"last visit.txt");
		Important.writeToTextFile(new File(path+"last visit.txt"), curDate);
		String number=Important.getStringFromTextFile(Paths.get(currentPath+"pages.txt"));
		int pageNumber=Integer.parseInt(number)+1;
		Important.writeToTextFile(new File(currentPath+"pages.txt"), Integer.toString(pageNumber));
		page.setCreatedDateAndTime(Important.getDateAndTime());
		page.setLastVisitedDateAndTime(Important.getDateAndTime());
		table.getItems().add(page);
	}
	
	private static void loadFiles(String currentPath, TableView table){
		File[] listOfFiles=Important.getFolderAndFileName(currentPath);
		int size=listOfFiles.length;
		
		for(int i=0; i<size; i++) {
			if(listOfFiles[i].isFile()) continue;
			String folderName=listOfFiles[i].getName();
			String[] folderNames=listOfFiles[i].getName().split("#");
			Page page=new Page();
			page.setTitle(folderNames[0]);
			page.setCreatedDateAndTime(folderNames[1]);
			page.setLastVisitedDateAndTime(Important.getStringFromTextFile(Paths.get(currentPath+folderName+"/last visit.txt")));
			table.getItems().add(page);
		}
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getLastVisit() {
		return lastVisit;
	}

	public void setLastVisit(String lastVisit) {
		this.lastVisit = lastVisit;
	}

	public ImageView getDiaryIcon() {
		return diaryIcon;
	}

	
}
