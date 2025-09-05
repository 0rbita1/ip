import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import keeka.Interpreter;
import keeka.Keeka;
import keeka.Storage;
import keeka.Ui;
import tasks.Task;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Keeka keeka;
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));
    private ArrayList<Task> tasks = new ArrayList<>();
    private Ui ui = new Ui();

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        Storage.loadSaveContents(tasks);
        Ui.printGreeting();
        String greeting = Ui.retrieveMessage();
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(greeting, dukeImage)
        );
    }

    public void setKeeka(Keeka d) {
        keeka = d;
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (Objects.equals(input, "bye")) {
            Platform.exit();
        }

        Interpreter.interpretInputs(tasks, input);
        String response = Ui.retrieveMessage();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
    }
}
