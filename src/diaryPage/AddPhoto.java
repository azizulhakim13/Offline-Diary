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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddPhoto {
	
	static FileChooser fc=new FileChooser();
	static double x,y;
	
	static void add(VBox vb, String currentPath) {
		FileChooser.ExtensionFilter filter=new FileChooser.ExtensionFilter("Select Photo or Gif", "*.png", "*.jpg", "*.gif", "*.bmp");
		fc.getExtensionFilters().add(filter);
		String filePath=null;
		try {
			File file=fc.showOpenDialog(null);
			filePath=file.toURI().toString();
			if(filePath!=null) {
				currentPath+=file.getName();
				//if(new File(currentPath).exists()) System.out.println("exists");
				Path sourcePath=file.toPath();
				Files.copy(sourcePath, Paths.get(currentPath));
				file=new File(currentPath);
				photoAdder(vb, filePath, file);
			}
		}catch(Exception e) {}
	}
	
	static void photoAdder(VBox vb, String filePath, File file) {
		ImageView iV=new ImageView(new Image(filePath));
		VBox layout=new VBox();
		iV.setFitWidth(134);
		iV.setPreserveRatio(true);
		iV.setSmooth(true);
		
		
		HBox buttonRow=new HBox();
		buttonRow.setAlignment(Pos.CENTER);
		Button delete=new Button();
		delete.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-delete-15.png")));
		delete.setOnAction(e-> {
			vb.getChildren().remove(layout);
			file.delete();
	    });
		Button fullScreen=new Button();
		fullScreen.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-full-screen-15.png")));
		delete.setScaleShape(true);
		
		fullScreen.setOnAction(e-> {
			Stage stage=new Stage();
			buttonRow.setDisable(true);
			StackPane sp=new StackPane();
			sp.setStyle("-fx-background-color : #000000;");
			ImageView image=new ImageView(iV.getImage());
			Scene scene=new Scene(sp, 800, 500);
			scene.getStylesheets().add("diaryPage/MediaContent.css");
			image.fitWidthProperty().bind(sp.widthProperty());
			image.fitHeightProperty().bind(sp.heightProperty());
			image.setPreserveRatio(true);
			HBox buttons=new HBox();
			buttons.setPadding(new Insets(20));
			Button back=new Button();
			back.setGraphic(new ImageView(new Image("file:res/asset/icon/icons8-go-back-15.png")));
			
			stage.setOnCloseRequest(f-> buttonRow.setDisable(false));
			
			back.setOnAction(f-> {
				buttonRow.setDisable(false);
				stage.close();
			});
			scene.setOnMousePressed(f-> {
				x=f.getSceneX()+10;
				y=f.getSceneY()+30;
			});
			scene.setOnMouseDragged(f -> {
				if(!stage.isFullScreen()) {
					stage.setX(f.getScreenX()-x);
					stage.setY(f.getScreenY()-y);
				}
			});
			
			buttons.getChildren().addAll(back, Important.hSpacer());
			sp.getChildren().addAll(image, buttons);
			stage.setTitle(file.getName());
			stage.setScene(scene);
			
			
			
			stage.setFullScreen(true);
			scene.setOnMouseClicked(f-> {
				if(f.getClickCount()==2 && stage.isFullScreen()) stage.setFullScreen(false);
				else if(f.getClickCount()==2) stage.setFullScreen(true);
			});
			stage.show();
		});
		
		buttonRow.getChildren().addAll(delete, fullScreen);
		layout.getChildren().addAll(iV, buttonRow);
		vb.getChildren().add(layout);
		
	}
}
