import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application
{

	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws IOException 
	{
		// Create the FXMLLoader 
		FXMLLoader loader = new FXMLLoader();
		// Path to the FXML File
		String fxmlDocPath = "/home/targum/Documents/Carcassonne/Tile Editor/src/main/java/view/Tile Editor Layout.fxml";
		FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
		
		// Create the Pane and all Details
		VBox root =  loader.load(fxmlStream);
		
		// Create the Scene
		Scene scene = new Scene(root);
		// Set the Scene to the Stage
		stage.setScene(scene);
		// Set the Title to the Stage
		stage.setTitle("Carcassonne Tile Editor");
		// Display the Stage
		stage.show();
	}
}
