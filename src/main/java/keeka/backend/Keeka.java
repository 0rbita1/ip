package keeka.backend;

import java.util.Scanner;

/**
 * Main application class for the Keeka task manager.
 * Serves as the entry point and coordinator for all application components,
 * providing both CLI and programmatic interfaces for task management.
 */
public class Keeka {
    private TaskList taskList;
    private Storage storage;
    private Parser parser;
    private Ui ui;
    private CommandHandler commandHandler;
    private TaskLoader taskLoader;
    private Interpreter interpreter;

    /**
     * Constructs a new Keeka application instance and initializes all required components.
     */
    public Keeka() {
        initializeComponents();
    }

    /**
     * Initializes all application components and establishes their dependencies.
     * Creates the dependency injection structure for proper component interaction.
     */
    private void initializeComponents() {
        taskList = new TaskList();
        storage = new Storage("src/main/java/keeka/backend/List.txt");
        parser = new Parser();
        ui = new Ui();
        commandHandler = new CommandHandler(taskList, storage, parser, ui);
        taskLoader = new TaskLoader(taskList, storage, parser);
        interpreter = new Interpreter(commandHandler, taskLoader, ui);
    }

    /**
     * Runs the CLI version of Keeka with interactive command processing.
     * Continuously processes user input until the user enters the exit command.
     */
    public void run() {
        interpreter.start();
        
        Scanner scanner = new Scanner(System.in);
        String input;
        
        while (!(input = scanner.nextLine()).equals("bye")) {
            interpreter.processCommand(input);
        }
        
        interpreter.processCommand("bye");
        scanner.close();
    }

    /**
     * Main entry point for the application when run as a standalone CLI program.
     *
     * @param args Command line arguments (currently unused).
     */
    public static void main(String[] args) {
        new Keeka().run();
    }
}
