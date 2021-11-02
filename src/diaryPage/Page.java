package diaryPage;

import java.io.File;
import java.io.IOException;
import important.Important;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Files;
import java.util.ArrayList;
import diary.Diary;
import homePage.HomePage;

public class Page{
	static ArrayList<MediaPlayer> listOfMedia=new ArrayList<MediaPlayer>();
	private String title="";
	private String createdDateAndTime="";
	private String lastVisitedDateAndTime="";
	private ImageView pageIcon=new ImageView(new Image("file:res/asset/icon/icons8-page-15.png"));


	public static void show(String currentPath, Double sceneWidth, Double sceneHeight) {
		VBox layout=new VBox(5);
		Scene scene=new Scene(layout, sceneWidth, sceneHeight);
		scene.getStylesheets().add("homePage/HomePage.css");
		HomePage.stage.setScene(scene);
		
		HBox row1=new HBox();
		row1.setPadding(new Insets(10));
		Button back=new Button("Back");
		

		row1.getChildren().add(back);
		
		HBox row2=new HBox();
		row2.setAlignment(Pos.CENTER);
		File currentFolder=new File(currentPath);
		String names[]=currentFolder.getName().split("#");
		Label pageTitle=new Label(names[0]);
		pageTitle.setPadding(new Insets(0, 0, 20, 0));
		pageTitle.getStyleClass().add("label-title");
		pageTitle.setScaleX(3);
		pageTitle.setScaleY(3);
		row2.setPadding(new Insets(20, 0, 0, 0));
		row2.getChildren().add(pageTitle);
		
		HBox row3=new HBox();
		row3.setAlignment(Pos.CENTER);
		Label textContentTitle=new Label("Text Content");
		textContentTitle.setStyle("-fx-font-weight: bold;-fx-text-fill: white");
		textContentTitle.setAlignment(Pos.CENTER);
		textContentTitle.prefWidthProperty().bind(scene.widthProperty());
		Label mediaContentTitle=new Label("Media Files");
		mediaContentTitle.setStyle("-fx-font-weight: bold;-fx-text-fill: white");
		mediaContentTitle.setAlignment(Pos.CENTER);
		mediaContentTitle.setMinWidth(200);
		row3.getChildren().addAll(textContentTitle, mediaContentTitle);
		
		HBox row4=new HBox(2);
		row4.setPadding(new Insets(0, 20, 0, 20));
		VBox vb=new VBox();
		vb.setStyle("-fx-background-color: gray;");
		row4.prefHeightProperty().bind(scene.heightProperty());
		row4.prefWidthProperty().bind(scene.widthProperty());
		row4.setAlignment(Pos.CENTER);
		TextArea textContent=new TextArea();
		textContent.prefWidthProperty().bind(scene.widthProperty());
		textContent.setPromptText("Write here");
		textContent.setStyle("-fx-font-size: 30");
		textContent.setWrapText(true);
		ScrollPane mediaContent=new ScrollPane();
		mediaContent.getStylesheets().add("diaryPage/MediaContent.css");
		mediaContent.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		mediaContent.setFitToHeight(true);
		mediaContent.setContent(vb);
		mediaContent.setMinWidth(150);
		row4.getChildren().addAll(textContent, mediaContent);
		
		HBox row5=new HBox();
		row5.setPadding(new Insets(10));
		row5.setAlignment(Pos.CENTER);
		Button addPhoto=new Button("Add Photo");
		Button addVideo=new Button("Add Video");
		Button addMusic=new Button("Add Audio");
		
		back.setOnAction(e-> {
			for(MediaPlayer player:listOfMedia){  
				player.stop();
				player.dispose();
			} 
			listOfMedia.clear();
			File file=new File(currentPath);
			String path=currentPath.replaceAll(file.getName()+"/", "");
			Diary.show(path, scene.getWidth(), scene.getHeight());
			Important.writeToTextFile(new File(currentPath+"text content.txt"), textContent.getText());
			HomePage.stage.setOnCloseRequest(null);
		});
		
		addPhoto.setOnAction(e-> AddPhoto.add(vb, currentPath));
		addVideo.setOnAction(e-> AddVideo.add(vb, currentPath));
		addMusic.setOnAction(e-> AddAudio.add(vb, currentPath));
		row5.getChildren().addAll(Important.hSpacer(), addPhoto, Important.hSpacer(), addVideo, Important.hSpacer(), addMusic, Important.hSpacer());
		layout.getChildren().addAll(row1, row2, Important.vSpacer(), row3, row4, row5);
		
		loadFiles(currentPath, vb, textContent);
		HomePage.stage.setOnCloseRequest(e-> Important.writeToTextFile(new File(currentPath+"text content.txt"), textContent.getText()));
	}
	
	
	private static void loadFiles(String currentPath, VBox vb, TextArea textContent){
		File[] listOfFiles=Important.getFolderAndFileName(currentPath);
		int size=listOfFiles.length;
		String ext = null;
		for(int i=0; i<size; i++) {
			try {
				ext=Files.probeContentType(listOfFiles[i].toPath());
			} catch (IOException e) {}
			if(ext.contains("video")) AddVideo.videoAdder(vb, listOfFiles[i].toURI().toString(), listOfFiles[i]);
			else if(ext.contains("audio")) AddAudio.audioAdder(vb, listOfFiles[i].toURI().toString(), listOfFiles[i]);
			else if(listOfFiles[i].toString().contains(".txt")) {
				if(listOfFiles[i].getName().equals("text content.txt")) {
					String text=Important.getStringFromTextFile(listOfFiles[i].toPath());
					textContent.setText(text);
				}
			}
			else   AddPhoto.photoAdder(vb, listOfFiles[i].toURI().toString(), listOfFiles[i]);
		}
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getCreatedDateAndTime() {
		return createdDateAndTime;
	}


	public void setCreatedDateAndTime(String createdDateAndTime) {
		this.createdDateAndTime = createdDateAndTime;
	}


	public String getLastVisitedDateAndTime() {
		return lastVisitedDateAndTime;
	}


	public void setLastVisitedDateAndTime(String lastVisitedDateAndTime) {
		this.lastVisitedDateAndTime = lastVisitedDateAndTime;
	}


	public ImageView getPageIcon() {
		return pageIcon;
	}



}
