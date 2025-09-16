package keeka.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import keeka.backend.*;
import keeka.tasks.Task;

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
    
    // Core components using new architecture
    private TaskList taskList;
    private Storage storage;
    private Parser parser;
    private Ui ui;
    private CommandHandler commandHandler;
    private TaskLoader taskLoader;
    private Interpreter interpreter;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        
        // Initialize the new architecture components
        initializeComponents();
        
        // Load tasks and show greeting
        interpreter.start();
        String greeting = ui.getLatestMessage();
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(greeting, dukeImage)
        );
    }

    private void initializeComponents() {
        taskList = new TaskList();
        storage = new Storage("src/main/java/keeka/backend/List.txt");
        parser = new Parser();
        ui = new Ui();
        commandHandler = new CommandHandler(taskList, storage, parser, ui);
        taskLoader = new TaskLoader(taskList, storage, parser);
        interpreter = new Interpreter(commandHandler, taskLoader, ui);
    }

    public void setKeeka(Keeka keeka) {
        this.keeka = keeka;
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (Objects.equals(input.trim(), "bye")) {
            ui.showGoodbye();
            String response = ui.getLatestMessage();
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getDukeDialog(response, dukeImage)
            );
            userInput.clear();
            Platform.exit();
            return;
        }

        // Process command through interpreter
        interpreter.processCommand(input);
        String response = ui.getLatestMessage();
        
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
    }
}
