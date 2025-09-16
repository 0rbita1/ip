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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Keeka Task Manager");
            fxmlLoader.<MainWindow>getController().setKeeka(keeka);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
