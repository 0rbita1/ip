package keeka.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import keeka.backend.Keeka;

/**
 * GUI application for Keeka using FXML.
 */
public class Main extends Application {

    private Keeka keeka = new Keeka();

    @Override
    public void start(Stage stage) {
        try {
            System.out.println("Starting JavaFX application...");
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            System.out.println("FXML Loader created, loading FXML...");
            AnchorPane ap = fxmlLoader.load();
            System.out.println("FXML loaded successfully, creating scene...");
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Keeka Task Manager");
            System.out.println("Setting Keeka instance to controller...");
            fxmlLoader.<MainWindow>getController().setKeeka(keeka);
            System.out.println("Showing stage...");
            stage.show();
            System.out.println("JavaFX application started successfully!");
        } catch (IOException e) {
            System.err.println("FXML Loading failed:");
            e.printStackTrace();
            // Fall back to CLI if GUI fails
            System.out.println("Falling back to CLI mode...");
            keeka.run();
        } catch (Exception e) {
            System.err.println("Unexpected error during JavaFX startup:");
            e.printStackTrace();
            // Fall back to CLI if GUI fails
            System.out.println("Falling back to CLI mode...");
            keeka.run();
        }
    }
}
