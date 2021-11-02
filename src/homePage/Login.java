package homePage;

import java.nio.file.Paths;
import important.Important;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Login {
	
	public static void show(double sceneWidth, double sceneHeight) {
		VBox layout=new VBox(10);
		layout.setAlignment(Pos.CENTER);
		ImageView loginPhoto=new ImageView(new Image("file:res/asset/icon/icons8-male-user-filled-50.png"));
		HBox row1=new HBox(10);
		row1.setAlignment(Pos.CENTER);
		Label passLabel=new Label("Password");
		PasswordField passField=new PasswordField();
		row1.getChildren().addAll(passLabel, passField);
		HBox row2=new HBox(10);
		row2.setAlignment(Pos.CENTER);
		Button loginButton=new Button("Log In");
		Button exitButton=new Button("Exit");
		exitButton.setOnAction(e-> HomePage.stage.close());
		loginButton.setOnAction(e-> {
			String typedText=passField.getText();
			String password=Important.getStringFromTextFile(Paths.get("res/home page/Password.txt"));
			if(typedText.equals(password)) HomePage.show(800, 500);
			else Important.popupShow("Inconrrect Password", 1500, 400, 100);
		});
		passField.setOnAction(loginButton.getOnAction());
		Important.hSpacing(row2, 15);
		row2.getChildren().addAll(loginButton, exitButton);
		layout.getChildren().addAll(loginPhoto, row1, row2);
		Scene scene=new Scene(layout, sceneWidth, sceneHeight);
		scene.getStylesheets().add("homePage/Login.css");
		HomePage.stage.setScene(scene);
		HomePage.stage.setTitle("Log In");
		HomePage.stage.setResizable(false);
	}

}
