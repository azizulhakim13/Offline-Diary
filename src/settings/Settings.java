package settings;

import java.io.File;
import java.nio.file.Paths;
import homePage.HomePage;
import important.Important;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Settings{
	
	public static void show(double sceneWidth, double sceneHeight) {
		HomePage.stage.setTitle("Settings");
		VBox layout=new VBox(10);
		layout.setPadding(new Insets(10));
		HBox row1=new HBox();
		Button back=new Button("Back");
		row1.getChildren().add(back);
		HBox row2=new HBox();
		row2.setPadding(new Insets(40, 0, 0, 60));
		Button changePassButton=new Button("Change Password");
		row2.getChildren().add(changePassButton);
		changePassButton.setOnAction(e-> {
			VBox innerLayout=new VBox(10);
			innerLayout.setAlignment(Pos.CENTER);
			
			HBox innerRow1=new HBox(10);
			innerRow1.setAlignment(Pos.CENTER);
			Label oldPassLabel=new Label("Old Pasword         ");
			PasswordField oldPassField=new PasswordField();
			innerRow1.getChildren().addAll(oldPassLabel, oldPassField);
			
			HBox innerRow1_5=new HBox(10);
			innerRow1_5.setAlignment(Pos.CENTER);
			Label newPassLabel=new Label("New Pasword       ");
			PasswordField newPassField=new PasswordField();
			innerRow1_5.getChildren().addAll(newPassLabel, newPassField);
			
			HBox innerRow2=new HBox(10);
			innerRow2.setAlignment(Pos.CENTER);
			Label confirmPassLabel=new Label("Confirm Password");
			PasswordField confirmPassField=new PasswordField();
			innerRow2.getChildren().addAll(confirmPassLabel, confirmPassField);
			
			//Important.writeToTextFile(new File("res/home page/Password.txt"), oldPassField.getText());
			Stage stage=new Stage();
			stage.setResizable(false);
			HBox buttonRow=new HBox();
			Button submitButton=new Button("Submit");
			Button cancelButton=new Button("Cancel");
			buttonRow.setAlignment(Pos.CENTER);
			buttonRow.getChildren().addAll(submitButton);
			
			innerLayout.getChildren().addAll(innerRow1, innerRow1_5, innerRow2, buttonRow);
			
			Scene scene=new Scene(innerLayout, 600, 400);
			scene.getStylesheets().add("homePage/FlashScreen.css");
			stage.setScene(scene);
			cancelButton.setOnAction(f-> {
				stage.close();
				HomePage.show(scene.getWidth(), scene.getHeight());
			});
			submitButton.setOnAction(f-> {
				String oldPass=oldPassField.getText();
				String newPass=newPassField.getText();
				String confirmPass=confirmPassField.getText();
				String currentPass=Important.getStringFromTextFile(Paths.get("res/home page/Password.txt"));
				if(oldPass.equals(currentPass) && newPass.equals(confirmPass)) {
					Important.popupShow("Password changed successfully", 3000, 200, 150);
					Important.writeToTextFile(new File("res/home page/Password.txt"), newPass);
					stage.close();
				}
				else if(!oldPass.equals(currentPass)) Important.popupShow("Wrong Password", 1500, 200, 100);
				else Important.popupShow("Password Mismatch", 2000, 200, 100);
			});
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		});
		
		HBox row3 = new HBox();
		CheckBox passwordCheck=new CheckBox("Enable password protection.");
		row3.setPadding(new Insets(0, 0, 0, 60));
		row3.getChildren().add(passwordCheck);
//		passwordCheck.setPadding(new Insets(0, 0, 0, 60));
		String passCheck=Important.getStringFromTextFile(Paths.get("res/home page/password enable check.txt"));
		if(passCheck.equals("true")) passwordCheck.setSelected(true);
		passwordCheck.setOnAction(f-> {
			if(passwordCheck.isSelected()) Important.writeToTextFile(new File("res/home page/password enable check.txt"), "true");
			else Important.writeToTextFile(new File("res/home page/password enable check.txt"), "false");
		});
		
		layout.getChildren().addAll(row1, row2, row3);
		Scene scene=new Scene(layout, sceneWidth, sceneHeight);
		scene.getStylesheets().add("settings/Settings.css");
		back.setOnAction(e-> HomePage.show(scene.getWidth(), scene.getHeight()));
		HomePage.stage.setScene(scene);
	}
	
}
