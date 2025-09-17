package keeka.backend;


import java.util.ArrayList;
import java.util.List;

import keeka.tasks.Task;

/**
 * Handles all user interface operations and message display functionality.
 * Manages a message buffer for GUI communication while also providing
 * console output for debugging and CLI usage.
 */
public class Ui {
    private final List<String> messageBuffer;

    /**
     * Constructs a new Ui instance with an empty message buffer ready for operation.
     */
    public Ui() {
        this.messageBuffer = new ArrayList<>();
    }

    /**
     * Displays the application welcome greeting message to the user.
     */
    public void showGreeting() {
        addMessage("Hello! I'm Keeka\nWhat can I do for you?");
    }

    /**
     * Displays the application goodbye message when the user exits.
     */
    public void showGoodbye() {
        addMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Displays a confirmation message when a task is successfully added.
     * Shows the task details and the current total task count.
     *
     * @param task The task that was successfully added.
     * @param totalTasks The total number of tasks after adding this task.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        addMessage("Task successfully added:\n" + task + "\nTask counter: " + totalTasks);
    }

    /**
     * Displays a confirmation message when a task is successfully marked as done.
     *
     * @param task The task that was marked as completed.
     */
    public void showTaskMarked(Task task) {
        addMessage("Task successfully marked as done:\n" + task);
    }

    /**
     * Displays a confirmation message when a task is successfully unmarked.
     *
     * @param task The task that was marked as not completed.
     */
    public void showTaskUnmarked(Task task) {
        addMessage("Task successfully marked as NOT done:\n" + task);
    }

    /**
     * Displays a confirmation message when a task is successfully deleted.
     * Shows the deleted task details and the remaining task count.
     *
     * @param task The task that was deleted.
     * @param remainingTasks The number of tasks remaining after deletion.
     */
    public void showTaskDeleted(Task task, int remainingTasks) {
        addMessage("Task successfully deleted:\n" + task + "\nTask counter: " + remainingTasks);
    }

    /**
     * Displays a confirmation message when a task is successfully updated.
     *
     * @param task The task that was updated with new information.
     */
    public void showTaskUpdated(Task task) {
        addMessage("Task successfully updated:\n" + task);
    }

    /**
     * Displays the complete list of tasks with sequential numbering.
     * Shows a message if the list is empty rather than a blank display.
     *
     * @param tasks The list of tasks to display to the user.
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            addMessage("List is empty! Add tasks to display");
            return;
        }

        StringBuilder listText = new StringBuilder("Displaying list items\n");
        for (int i = 0; i < tasks.size(); i++) {
            listText.append(i + 1).append(". ").append(tasks.get(i).toString()).append("\n");
        }
        addMessage(listText.toString());
    }

    /**
     * Displays the results of a task search operation with sequential numbering.
     * Shows a message if no tasks match the search criteria.
     *
     * @param tasks The list of tasks that match the search query.
     */
    public void showFoundTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            addMessage("Unable to find any matches for your query!");
            return;
        }

        StringBuilder listText = new StringBuilder("Displaying items that match your query\n");
        for (int i = 0; i < tasks.size(); i++) {
            listText.append(i + 1).append(". ").append(tasks.get(i).toString()).append("\n");
        }
        addMessage(listText.toString());
    }

    /**
     * Displays error messages to the user with appropriate formatting.
     *
     * @param message The error message to display to the user.
     */
    public void showError(String message) {
        addMessage("Error: " + message);
    }

    /**
     * Retrieves the most recently added message from the buffer.
     * Used primarily by GUI components to get the latest response.
     *
     * @return The latest message added to the buffer, or empty string if buffer is empty.
     */
    public String getLatestMessage() {
        return messageBuffer.isEmpty() ? "" : messageBuffer.get(messageBuffer.size() - 1);
    }

    /**
     * Adds a message to the buffer and prints it to the console with formatting.
     * Provides both GUI support through buffering and CLI support through console output.
     *
     * @param message The message to add to the buffer and display.
     */
    private void addMessage(String message) {
        messageBuffer.add(message);
        System.out.println("====================================================================");
        System.out.println(message);
        System.out.println("====================================================================");
    }
}
