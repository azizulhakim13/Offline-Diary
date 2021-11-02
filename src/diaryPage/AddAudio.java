package diaryPage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import important.Important;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AddAudio {
	
	static boolean dragged=false;
	static FileChooser fc=new FileChooser();
	static boolean playing=false;
	static double x,y;
	
	static void add(VBox vb, String currentPath) {
		StackPane layout=new StackPane();
		vb.getChildren().add(layout);
		FileChooser.ExtensionFilter filter=new FileChooser.ExtensionFilter("Select audio", "*.mp3", "*.wav", "*.aac");
		fc.getExtensionFilters().add(filter);
		String filePath=null;
		try {
			File file=fc.showOpenDialog(null);
			filePath=file.toURI().toString();
			if(filePath!=null) {
				currentPath+=file.getName();
				Path sourcePath=file.toPath();
				Files.copy(sourcePath, Paths.get(currentPath));
				file=new File(currentPath);
				audioAdder(vb, filePath, file);
			}
		}catch(Exception e) {}
	}
	
	static void audioAdder(VBox vb, String filePath, File file) {
		VBox layout=new VBox();
		MediaPlayer player=new MediaPlayer(new Media(filePath));
		MediaPlayer vPlayer=new MediaPlayer(new Media(filePath));
		
		Page.listOfMedia.add(player);
		Page.listOfMedia.add(vPlayer);
		ImageView image=new ImageView("file:res/asset/photo/audio.jpg");
		image.setFitWidth(134);
		image.setPreserveRatio(true);
		image.setSmooth(true);
		StackPane sp=new StackPane();
		Label mediaTitle=new Label(file.getName());
		mediaTitle.setMaxWidth(134);
		mediaTitle.setTextFill(Color.web("#FFFFFF"));
		sp.getChildren().addAll(image,mediaTitle);
		StackPane.setAlignment(mediaTitle, Pos.TOP_CENTER);
		HBox buttonRow=new HBox();
		buttonRow.setAlignment(Pos.CENTER);
		Button playPause=new Button();
		player.setVolume(.25);
		vPlayer.setVolume(0);
		playPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
		player.setOnEndOfMedia(()-> {
			player.stop();
			player.pause();
			vPlayer.stop();
			vPlayer.pause();
			playPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
		});
		
		playPause.setOnAction(e-> {
			
			if(player.getStatus().equals(Status.READY) || player.getStatus().equals(Status.PAUSED)) {
				player.play();
				vPlayer.play();
				playPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-pause-15.png")));
			}
			else {
				player.pause();
				vPlayer.pause();
				playPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
			}
		});
		
		Button fullScreen=new Button();
		fullScreen.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-full-screen-15.png")));
		
		Button delete=new Button();
		delete.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-delete-15.png")));
		delete.setOnAction(e-> {
			player.stop();
			vPlayer.stop();
			player.dispose();
			vPlayer.dispose();
			vb.getChildren().remove(layout);
			if(file.delete()) System.out.println("deleted");
			else System.out.println("can't delete");
			Page.listOfMedia.remove(player);
			Page.listOfMedia.remove(vPlayer);
	    });
		Button sound=new Button();
		sound.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-audio-15.png")));
		sound.setOnAction(e-> {
			if(player.isMute() || player.getVolume()==0) {
				player.setMute(false);
				if(player.getVolume()==0) player.setVolume(.25);
				sound.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-audio-15.png")));
			}
			else {
				player.setMute(true);
				sound.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-no-audio-15.png")));
			}
		});
		fullScreen.setOnAction(e-> fullScreenPlayer(vPlayer, player, buttonRow, playPause, sound, file));
		buttonRow.getChildren().addAll(delete, playPause, sound, fullScreen);
		layout.getChildren().addAll(sp, buttonRow);
		vb.getChildren().add(layout);
	}
	
	
	
	private static void fullScreenPlayer(MediaPlayer vPlayer, MediaPlayer player, HBox buttonRow, Button playPause, Button sound, File file) {
		buttonRow.setDisable(true);
		Stage stage=new Stage();
		stage.setTitle(file.getName());
		StackPane sp=new StackPane();
		sp.setStyle("-fx-background-color : #000000;");
		ImageView image=new ImageView("file:res/asset/photo/audio.jpg");
		player.pause();
		
		vPlayer.setVolume(player.getVolume());
		if(player.isMute()) vPlayer.setVolume(0);
		image.fitWidthProperty().bind(sp.widthProperty());
		image.fitHeightProperty().bind(sp.heightProperty());
		image.setPreserveRatio(true);
		
		VBox control=new VBox();
		control.setPadding(new Insets(0, 0, 10, 0));
		HBox seek=new HBox(5);
		seek.setAlignment(Pos.CENTER);
		Label currentTime=new Label("0.00");
		//currentTime.setStyle("-fx-background-color : #FFFFFF;");
		currentTime.setStyle("-fx-font-weight: bold");
		currentTime.setTextFill(Color.web("#6495ED"));
		Slider seekBar=new Slider();
		seekBar.setValue(player.getCurrentTime().toSeconds());
		seekBar.minWidthProperty().bind(sp.widthProperty().subtract(150));
		seekBar.setMax(player.getTotalDuration().toSeconds());
		seekBar.setOnMousePressed(v-> {
			if(vPlayer.getStatus().equals(Status.PLAYING)) playing=true;
			else playing=false;
			vPlayer.pause();
			vPlayer.seek(Duration.seconds(seekBar.getValue()));
		});
		seekBar.setOnMouseReleased(v-> {
			vPlayer.seek(Duration.seconds(seekBar.getValue()));
			if(playing==true) vPlayer.play();
			else vPlayer.pause();
			playing=false;
		});
		
		seekBar.setOnMouseDragged(v-> vPlayer.seek(Duration.seconds(seekBar.getValue())));
		vPlayer.currentTimeProperty().addListener( (v, oldValue, newValue)-> {
			seekBar.setValue(newValue.toSeconds());
			currentTime.setText(Important.getTimeFromSecond(vPlayer.getCurrentTime().toSeconds()));
		});
		Important.getTimeFromSecond(vPlayer.getTotalDuration().toSeconds());
		Label finishTime=new Label(Important.getTimeFromSecond(player.getTotalDuration().toSeconds()));
		//finishTime.setStyle("-fx-background-color : #FFFFFF;");
		finishTime.setStyle("-fx-font-weight: bold");
		finishTime.setTextFill(Color.web("#6495ED"));
		seek.getChildren().addAll(currentTime, seekBar, finishTime);
		HBox buttons=new HBox();
		buttons.setAlignment(Pos.CENTER);
		Button fullPlayPause=new Button();
		vPlayer.setOnEndOfMedia(()-> {
			vPlayer.seek(Duration.seconds(0.0));
			vPlayer.pause();
			fullPlayPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
		});
		fullPlayPause.setGraphic(playPause.getGraphic());
		fullPlayPause.setOnAction(f->{
			if(vPlayer.getStatus().equals(Status.READY) || vPlayer.getStatus().equals(Status.PAUSED)) {
				vPlayer.play();
				fullPlayPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-pause-15.png")));
			}
			else {
				vPlayer.pause();
				fullPlayPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
			}
		});
		Button back=new Button();
		back.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-go-back-15.png")));
		back.setOnAction(f-> {
			if(vPlayer.getStatus().equals(Status.PLAYING)) {
				player.play();
				playPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-pause-15.png")));
			}
			else playPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
			player.setVolume(vPlayer.getVolume());
			vPlayer.setVolume(0);
			player.seek(vPlayer.getCurrentTime());
			if(player.getVolume()==0) sound.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-no-audio-15.png")));
			else{
				player.setMute(false);
				sound.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-audio-15.png")));
			}
			stage.close();
			buttonRow.setDisable(false);
		});
		Label volume=new Label();
		volume.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-audio-15-white.png")));
		Slider volumeSlider=new Slider();
		volumeSlider.setValue(vPlayer.getVolume()*100);
		volumeSlider.valueProperty().addListener( (v, oldValue, newValue)-> {
			vPlayer.setVolume(volumeSlider.getValue()/100);
			player.setVolume(volumeSlider.getValue()/100);
		});			
		buttons.getChildren().addAll(back, fullPlayPause, volume, volumeSlider);			
		control.getChildren().addAll(seek, buttons);
		sp.getChildren().addAll(image, control);
		control.setAlignment(Pos.BOTTOM_CENTER);
		
		//on screen control
		sp.setOnMouseClicked(f-> {
			if(f.getClickCount()==1 && dragged==false) {
				if(vPlayer.getStatus().equals(Status.READY) || vPlayer.getStatus().equals(Status.PAUSED)) {
					vPlayer.play();
					fullPlayPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-pause-15.png")));
				}
				else {
					vPlayer.pause();
					fullPlayPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
				}
			}
			else if(f.getClickCount()==2) {
				
				if(vPlayer.getStatus().equals(Status.READY) || vPlayer.getStatus().equals(Status.PAUSED)) {
					vPlayer.play();
					fullPlayPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-pause-15.png")));
				}
				else {
					vPlayer.pause();
					fullPlayPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
				}
				
				if(stage.isFullScreen()) stage.setFullScreen(false);
				else stage.setFullScreen(true);
			}
		});
		
		stage.setOnCloseRequest(f-> {
			vPlayer.pause();
			vPlayer.setVolume(0);
			player.seek(vPlayer.getCurrentTime());
			playPause.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-play-15.png")));
			player.pause();
			if(player.getVolume()==0) sound.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-no-audio-15.png")));
			else{
				player.setMute(false);
				sound.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-audio-15.png")));
			}
			buttonRow.setDisable(false);
		});
		
		
		
		Scene scene=new Scene(sp, 800, 500);
		stage.setScene(scene);
		scene.setOnMousePressed(f-> {
			dragged=false;
			x=f.getSceneX()+10;
			y=f.getSceneY()+30;
		});
		scene.setOnMouseDragged(f -> {
			if(!stage.isFullScreen()) {
				if(dragged==false) dragged=true;
				stage.setX(f.getScreenX()-x);
				stage.setY(f.getScreenY()-y);
			}
		});
		stage.show();
	}
	
	
}
