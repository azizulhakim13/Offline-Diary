package important;

import java.io.File;
import homePage.HomePage;
import important.Important;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class SignUp {
	public static void show(double sceneWidth, double sceneHeight) {
		VBox layout=new VBox(10);
		layout.setAlignment(Pos.CENTER);
		
		ImageView loginPhoto=new ImageView(new Image("file:res/asset/icon/icons8-male-user-filled-50.png"));
		HBox row1=new HBox(10);
		row1.setAlignment(Pos.CENTER);
		Label passLabel=new Label("Password              ");
		PasswordField passField=new PasswordField();
		row1.getChildren().addAll(passLabel, passField);
		
		HBox row2=new HBox(10);
		row2.setAlignment(Pos.CENTER);
		Label confirmPassLabel=new Label("Confirm Password");
		PasswordField confirmPassField=new PasswordField();
		row2.getChildren().addAll(confirmPassLabel, confirmPassField);
		
		CheckBox passwordCheck=new CheckBox("Enable password protection.");
		passwordCheck.setOnAction(f-> {
			if(passwordCheck.isSelected()) Important.writeToTextFile(new File("res/home page/password enable check.txt"), "true");
			else Important.writeToTextFile(new File("res/home page/password enable check.txt"), "false");
		});
		
		HBox row3=new HBox(10);
		row3.setAlignment(Pos.CENTER);
		Important.hSpacing(row3, 2);
		Button signUpButton=new Button("Sign Up");
		signUpButton.setOnAction(e-> {
			if(passField.getText().equals("") || confirmPassField.getText().equals("")) Important.popupShow("Field cannot be empty", 1000, 400, 100);
			else if(passField.getText().equals(confirmPassField.getText())) {
				HomePage.show(800, 500);
				Important.writeToTextFile(new File("res/FirstTimeCheck.txt"), "false");
				Important.writeToTextFile(new File("res/home page/Password.txt"), passField.getText());
			}
			else Important.popupShow("Password Mismatch", 1500, 400, 150);
		});
		confirmPassField.setOnAction(signUpButton.getOnAction());
		row3.getChildren().add(signUpButton);
		
		layout.getChildren().addAll(loginPhoto, row1, row2, passwordCheck, row3);
		
		Scene scene=new Scene(layout, sceneWidth, sceneHeight);
		scene.getStylesheets().add("settings/SignUp.css");
		HomePage.stage.setScene(scene);
		HomePage.stage.setTitle("Sign Up");
		HomePage.stage.setResizable(false);
		
	}
}
