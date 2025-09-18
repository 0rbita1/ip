package keeka.backend;

import java.util.List;

import keeka.tasks.Deadline;
import keeka.tasks.Event;
import keeka.tasks.Task;
import keeka.tasks.ToDo;

/**
 * Central command handler that processes all user commands and coordinates
 * between parsing, task management, storage, and user interface components.
 * Acts as the main controller for command execution in the application.
 */
public class CommandHandler {
    private final TaskList taskList;
    private final Storage storage;
    private final Parser parser;
    private final Ui ui;

    /**
     * Constructs a CommandHandler with required dependencies for command processing.
     *
     * @param taskList The task list manager for storing and retrieving tasks.
     * @param storage The storage handler for file persistence operations.
     * @param parser The parser for processing user input and saved content.
     * @param ui The user interface handler for displaying messages and responses.
     */
    public CommandHandler(TaskList taskList, Storage storage, Parser parser, Ui ui) {
        this.taskList = taskList;
        this.storage = storage;
        this.parser = parser;
        this.ui = ui;
    }

    /**
     * Processes todo task creation commands by parsing input, creating the task,
     * adding it to the task list, persisting to storage, and showing confirmation.
     *
     * @param input The todo task description provided by the user.
     */
    public void handleTodoCommand(String input) {

        assert input != null : "Input for the todo command should not be null";

        if (input.trim().isEmpty()) {
            ui.showError("Invalid task invocation!");
            return;
        }

        try {
            ToDo todo = TaskFactory.createToDo(input.trim(), false);
            taskList.addTask(todo);
            storage.saveTask(todo, taskList.size());
            ui.showTaskAdded(todo, taskList.size());
        } catch (Exception e) {
            ui.showError("Failed to create todo: " + e.getMessage());
        }
    }

    /**
     * Processes deadline task creation commands by parsing the input for description
     * and due date, creating the deadline task, and managing storage operations.
     *
     * @param input The deadline command string containing description and due date.
     */
    public void handleDeadlineCommand(String input) {

        assert input != null : "Input for the deadline command should not be null";

        if (input.trim().isEmpty() || !input.contains("/by")) {
            ui.showError("Invalid task invocation!");
            return;
        }

        try {
            Parser.DeadlineInput deadlineInput = parser.parseDeadlineInput(input.trim());
            Deadline deadline;

            if (deadlineInput.getDateTime() != null) {
                deadline = TaskFactory.createDeadline(deadlineInput.getDescription(), false,
                        deadlineInput.getDateTime());
            } else {
                deadline = TaskFactory.createDeadline(deadlineInput.getDescription(), false,
                        deadlineInput.getDate());
            }

            taskList.addTask(deadline);
            storage.saveTask(deadline, taskList.size());
            ui.showTaskAdded(deadline, taskList.size());
        } catch (Exception e) {
            ui.showError("Invalid task invocation!");
        }
    }

    /**
     * Processes event task creation commands by parsing the input for description,
     * start time, and end time, then creating and storing the event task.
     *
     * @param input The event command string containing description, start time, and end time.
     */
    public void handleEventCommand(String input) {

        assert input != null : "Input for the event command should not be null";

        if (input.trim().isEmpty() || !input.contains("/from") || !input.contains("/to")) {
            ui.showError("Invalid task invocation!");
            return;
        }

        try {
            Parser.EventInput eventInput = parser.parseEventInput(input.trim());
            Event event;

            if (eventInput.getStartDateTime() != null) {
                event = TaskFactory.createEvent(eventInput.getDescription(), false,
                        eventInput.getStartDateTime(), eventInput.getEndDateTime());
            } else {
                event = TaskFactory.createEvent(eventInput.getDescription(), false,
                        eventInput.getStartDate(), eventInput.getEndDate());
            }

            taskList.addTask(event);
            storage.saveTask(event, taskList.size());
            ui.showTaskAdded(event, taskList.size());
        } catch (Exception e) {
            ui.showError("Invalid task invocation!");
        }
    }

    /**
     * Processes task marking commands to set a task as completed.
     * Updates the task status, persists changes to storage, and shows confirmation.
     *
     * @param input The mark command containing the task index to be marked.
     */
    public void handleMarkCommand(String input) {

        assert input != null : "Input for the mark command should not be null";

        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = taskList.getTask(index);
            if (task != null) {
                task.markAsDone();
                storage.updateAllTasks(taskList.getAllTasks());
                ui.showTaskMarked(task);
            } else {
                ui.showError("Invalid task index");
            }
        } catch (Exception e) {
            ui.showError("Failed to mark task: " + e.getMessage());
        }
    }

    /**
     * Processes task unmarking commands to set a task as not completed.
     * Updates the task status, persists changes to storage, and shows confirmation.
     *
     * @param input The unmark command containing the task index to be unmarked.
     */
    public void handleUnmarkCommand(String input) {

        assert input != null : "Input for the unmark command should not be null";

        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = taskList.getTask(index);
            if (task != null) {
                task.markAsNotDone();
                storage.updateAllTasks(taskList.getAllTasks());
                ui.showTaskUnmarked(task);
            } else {
                ui.showError("Invalid task index");
            }
        } catch (Exception e) {
            ui.showError("Failed to unmark task: " + e.getMessage());
        }
    }

    /**
     * Processes task deletion commands by removing the specified task from the list,
     * updating storage, and showing confirmation with remaining task count.
     *
     * @param input The delete command containing the task index to be removed.
     */
    public void handleDeleteCommand(String input) {

        assert input != null : "Input for the delete command should not be null";

        try {
            int index = Integer.parseInt(input.split(" ")[1]) - 1;
            Task task = taskList.getTask(index);
            if (task != null) {
                taskList.removeTask(index);
                storage.updateAllTasks(taskList.getAllTasks());
                ui.showTaskDeleted(task, taskList.size());
            } else {
                ui.showError("Invalid task index");
            }
        } catch (Exception e) {
            ui.showError("Failed to delete task: " + e.getMessage());
        }
    }

    /**
     * Processes find commands to search for tasks containing the specified keyword.
     * Displays all matching tasks or a message if no matches are found.
     *
     * @param input The find command containing the search keyword.
     */
    public void handleFindCommand(String input) {

        assert input != null : "Input for the find command should not be null";

        try {
            String keyword = input.split(" ", 2)[1];
            List<Task> foundTasks = taskList.findTasks(keyword);
            ui.showFoundTasks(foundTasks);
        } catch (Exception e) {
            ui.showError("Failed to find tasks: " + e.getMessage());
        }
    }

    /**
     * Processes list commands to display all tasks currently in the task list.
     * Shows either the complete task list or a message if the list is empty.
     */
    public void handleListCommand() {
        ui.showTaskList(taskList.getAllTasks());
    }

    /**
     * Processes update commands to modify existing task properties such as
     * description or dates. Creates a new task with updated values and replaces the original.
     *
     * @param input The update command containing task index, field type, and new value.
     */
    public void handleUpdateCommand(String input) {

        assert input != null : "Input for the update command should not be null";

        try {
            Parser.UpdateInput updateInput = parser.parseUpdateInput(input);
            Task currentTask = taskList.getTask(updateInput.getTaskIndex());

            if (currentTask != null) {
                Task updatedTask = createUpdatedTask(currentTask, updateInput);
                taskList.replaceTask(updateInput.getTaskIndex(), updatedTask);
                storage.updateAllTasks(taskList.getAllTasks());
                ui.showTaskUpdated(updatedTask);
            } else {
                ui.showError("Invalid task index");
            }
        } catch (Exception e) {
            ui.showError("Failed to update task: " + e.getMessage());
        }
    }

    /**
     * Creates an updated version of an existing task with new field values.
     * Preserves the completion status and handles different task types appropriately.
     *
     * @param currentTask The existing task to be updated.
     * @param updateInput The parsed update input containing field type and new value.
     * @return A new task instance with the updated values.
     * @throws IllegalArgumentException If the field type is invalid for the task type.
     */
    private Task createUpdatedTask(Task currentTask, Parser.UpdateInput updateInput) {

        assert currentTask != null : "Current task should not be null";
        assert updateInput != null : "Update input should not be null";

        String fieldType = updateInput.getFieldType();
        String newValue = updateInput.getNewValue();
        boolean isDone = currentTask.isDone();

        if (currentTask instanceof ToDo) {
            if ("description".equals(fieldType)) {
                return TaskFactory.createToDo(newValue, isDone);
            }
        } else if (currentTask instanceof Deadline deadline) {
            if ("description".equals(fieldType)) {
                if (deadline.getDateTime() != null) {
                    return TaskFactory.createDeadline(newValue, isDone, deadline.getDateTime());
                } else {
                    return TaskFactory.createDeadline(newValue, isDone, deadline.getDate());
                }
            } else if ("date".equals(fieldType)) {
                if (newValue.contains("T")) {
                    return TaskFactory.createDeadline(deadline.getDescription(), isDone,
                        java.time.LocalDateTime.parse(newValue));
                } else {
                    return TaskFactory.createDeadline(deadline.getDescription(), isDone,
                        java.time.LocalDate.parse(newValue));
                }
            }
        } else if (currentTask instanceof Event event) {
            if ("description".equals(fieldType)) {
                if (event.getStartDateTime() != null) {
                    return TaskFactory.createEvent(newValue, isDone,
                        event.getStartDateTime(), event.getEndDateTime());
                } else {
                    return TaskFactory.createEvent(newValue, isDone,
                        event.getStartDate(), event.getEndDate());
                }
            }
        }

        throw new IllegalArgumentException("Invalid field type for task: " + fieldType);
    }
}
