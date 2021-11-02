package important;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Important {
	
	
	public static void vSpacing(VBox layout, int space) {
		for(int i=0;i<space;i++) layout.getChildren().addAll(new Label(""));
	}
	
	public static void hSpacing(HBox layout, int space) {
		String s = "";
		for(int i=0;i<space;i++) s+=" ";
		layout.getChildren().add(new Label(s));
	}
	
	public static Region vSpacer() {
	    Region spacer = new Region();
	    VBox.setVgrow(spacer, Priority.ALWAYS);
	    return spacer;
	}
	
	public static Region hSpacer() {
	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);
	    return spacer;
	}
	public static String getTimeFromSecond(double sec) {
		String time="";
		int hours = (int)sec / 3600;
		int minutes = ((int)sec % 3600) / 60;
		int seconds = (int)sec % 60;
		time+=String.valueOf(hours);
		time+=".";
		time+=String.valueOf(minutes);
		time+=".";
		time+=String.valueOf(seconds);
		return time;
	}
	
	public static double getWindowsTaskbarBottomHeight() {
		double taskBarHeight;
		Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		taskBarHeight = scrnSize.height - winSize.height;
		return taskBarHeight;
	}
	
	public static double getWindowsTaskbarRightHeight() {
		double taskBarWidth;
		Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		taskBarWidth = scrnSize.width - winSize.width;
		return taskBarWidth;
	}
	
	public static String[] getFolderName(String path) {
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return directories;
	}
	
	public static File[] getFolderAndFileName(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
	    return listOfFiles;
	}
	
	public static String getDateAndTime() {
		String hour=Integer.toString(LocalDateTime.now().getHour());
		if(hour.length()==1) hour="0"+hour;
		String min=Integer.toString(LocalDateTime.now().getMinute());
		if(min.length()==1) min="0"+min;
		String second=Integer.toString(LocalDateTime.now().getSecond());
		if(second.length()==1) second="0"+second;
		return LocalDate.now()+" "+hour+"."+min+"."+second;
	}
	
	public static void createFolder(String pathWithName) {
		new File(pathWithName).mkdirs();
	}
	
	public static void createTextFile(String pathWithName) {
		try {
			PrintWriter writer = new PrintWriter(pathWithName);
		} catch (Exception ex) {}
	}
	
	public static String getStringFromTextFile(Path path){
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded);
	}
	
	public static void deleteDirectory(File file) {
		File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDirectory(f);
	        }
	    }
	    file.delete();
	}
	
	public static void disableButton(Button button, int millisecondTime) {
		new Thread() {
		    public void run() {
		        Platform.runLater(new Runnable() {
		            public void run() {
		                button.setDisable(true);
		            }
		        });
		        try {
		            Thread.sleep(millisecondTime);
		        }
		        catch(InterruptedException ex) {
		        }
		        Platform.runLater(new Runnable() {
		            public void run() {
		                button.setDisable(false);
		            }
		        });
		    }
		}.start();
	}
	
	public static void writeToTextFile(File textFile, String text) {
		FileWriter writer=null;
		try {
			writer=new FileWriter(textFile);
		} catch (IOException e1) {
		}
		try {
			writer.write(text);
		} catch (IOException e1) {
		}
		try {
			writer.flush();
		} catch (IOException e1) {
		}
		try {
			writer.close();
		} catch (IOException e1) {
		}
	}
	
	public static void popupShow(String popup, int millisecondTime, double sceneWidth, double sceneHeight) {
		Stage stage=new Stage();
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		StackPane layout=new StackPane();
		Scene scene=new Scene(layout, sceneWidth, sceneHeight);
		scene.getStylesheets().add("important/Popup.css");
		Label label=new Label(popup);
		label.setAlignment(Pos.CENTER);
		label.setMinWidth(sceneWidth);
		label.setMinHeight(sceneHeight);
		layout.getChildren().add(label);
		stage.setScene(scene);
		new Thread() {
		    public void run() {
		        Platform.runLater(new Runnable() {
		            public void run() {
		                stage.show();
		            }
		        });
		        try {
		            Thread.sleep(millisecondTime);
		        }
		        catch(InterruptedException ex) {
		        }
		        Platform.runLater(new Runnable() {
		            public void run() {
		                stage.close();
		            }
		        });
		    }
		}.start();
		
	}
	
    
    public static void dirToZip(String sourceFolderName, String outputFileName){
    	new Thread(()-> {
    		try {
    	        String sourceFile = sourceFolderName;
    	        FileOutputStream fos = new FileOutputStream(outputFileName);
    	        ZipOutputStream zipOut = new ZipOutputStream(fos);
    	        File fileToZip = new File(sourceFile);
    	        zipFile(fileToZip, fileToZip.getName(), zipOut);
    	        zipOut.close();
    	        fos.close();
    		}catch(Exception ex) {}
    	}).start();
    }
    
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
    
    public static void unzipFile(String sourceFile, String outputFolder){
    	new Thread(()-> {
    		try {
    	        String fileZip = sourceFile;
    	        byte[] buffer = new byte[1024];
    	        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
    	        ZipEntry zipEntry = zis.getNextEntry();
    	        while(zipEntry != null){
    	            String fileName = zipEntry.getName();
    	            File newFile = new File(outputFolder + fileName);
    	            FileOutputStream fos = new FileOutputStream(newFile);
    	            int len;
    	            while ((len = zis.read(buffer)) > 0) {
    	                fos.write(buffer, 0, len);
    	            }
    	            fos.close();
    	            zipEntry = zis.getNextEntry();
    	        }
    	        zis.closeEntry();
    	        zis.close();
    		}catch(Exception ex) {}

    	}).start();

    }
    
    public static boolean isNetReachable() {
		try {
			URL u = new URL ( "http://www.google.com/");
			HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection (); 
			huc.setRequestMethod ("GET");
			huc.connect () ;
			return true;
		} catch(Exception e) {return false;}
    }
    
    public static void fadeScene(Stage stage, Scene scene, double fadeDurationMillisecond) {
    	
    }
	
}

