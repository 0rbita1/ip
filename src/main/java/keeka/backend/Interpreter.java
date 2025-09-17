package keeka.backend;

/**
 * Main interpreter that coordinates command processing and application flow.
 * Acts as the central controller that routes user commands to appropriate
 * handlers and manages the overall application lifecycle.
 */
public class Interpreter {
    private final CommandHandler commandHandler;
    private final TaskLoader taskLoader;
    private final Ui ui;

    /**
     * Constructs an Interpreter with required components for command processing.
     *
     * @param commandHandler The handler responsible for processing user commands.
     * @param taskLoader The loader responsible for restoring tasks from storage.
     * @param ui The user interface handler for displaying messages and responses.
     */
    public Interpreter(CommandHandler commandHandler, TaskLoader taskLoader, Ui ui) {
        this.commandHandler = commandHandler;
        this.taskLoader = taskLoader;
        this.ui = ui;
    }

    /**
     * Starts the application by displaying the greeting message and loading
     * any previously saved tasks from storage into the task list.
     */
    public void start() {
        ui.showGreeting();
        taskLoader.loadTasks();
    }

    /**
     * Processes a user command by parsing the input and routing it to the
     * appropriate command handler based on the command type.
     *
     * @param input The complete user input string containing command and arguments.
     */
    public void processCommand(String input) {
        String[] parts = input.trim().split(" ", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        switch (command) {
        case "todo" -> commandHandler.handleTodoCommand(args);
        case "deadline" -> commandHandler.handleDeadlineCommand(args);
        case "event" -> commandHandler.handleEventCommand(args);
        case "mark" -> commandHandler.handleMarkCommand(input);
        case "unmark" -> commandHandler.handleUnmarkCommand(input);
        case "delete" -> commandHandler.handleDeleteCommand(input);
        case "find" -> commandHandler.handleFindCommand(input);
        case "list" -> commandHandler.handleListCommand();
        case "update" -> commandHandler.handleUpdateCommand(args);
        case "bye" -> ui.showGoodbye();
        default -> ui.showError("Unknown command: " + command);
        }
    }
}
