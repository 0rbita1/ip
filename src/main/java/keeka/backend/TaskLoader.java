package keeka.backend;

import java.util.List;

import keeka.tasks.Task;

/**
 * Handles loading and reconstruction of tasks from storage into the task list.
 * Responsible for parsing saved task data and recreating appropriate task objects
 * while maintaining data integrity and handling various task types.
 */
public class TaskLoader {
    private final TaskList taskList;
    private final Storage storage;
    private final Parser parser;
    private final TaskFactory taskFactory;

    /**
     * Constructs a TaskLoader with required dependencies for task restoration.
     *
     * @param taskList The task list to populate with loaded tasks.
     * @param storage The storage handler for reading saved task data.
     * @param parser The parser for processing saved task content.
     */
    public TaskLoader(TaskList taskList, Storage storage, Parser parser) {
        this.taskList = taskList;
        this.storage = storage;
        this.parser = parser;
        this.taskFactory = new TaskFactory();
    }

    /**
     * Loads all previously saved tasks from storage and adds them to the task list.
     * Handles parsing errors gracefully and continues loading other tasks if some fail.
     */
    public void loadTasks() {
        try {
            List<String> saveContents = storage.loadSaveContents();
            for (String content : saveContents) {
                Task task = createTaskFromSaveContent(content);
                if (task != null) {
                    taskList.addTask(task);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
        }
    }

    /**
     * Creates a task object from a saved content line by parsing the content
     * and determining the appropriate task type and properties.
     *
     * @param saveContent The complete saved task line from storage.
     * @return A reconstructed Task object, or null if parsing fails.
     */
    private Task createTaskFromSaveContent(String saveContent) {
        try {
            Parser.ParsedSaveContent parsed = parser.parseSaveContent(saveContent);
            char taskCode = parsed.getTaskCode();
            boolean isDone = parsed.isDone();
            String content = parsed.getTaskContent();

            return switch (taskCode) {
            case 'T' -> TaskFactory.createToDo(content, isDone);
            case 'D' -> createDeadlineFromContent(content, isDone);
            case 'E' -> createEventFromContent(content, isDone);
            default -> null;
            };
        } catch (Exception e) {
            System.err.println("Failed to parse task: " + saveContent);
            return null;
        }
    }

    /**
     * Creates a Deadline task from saved content by parsing the description
     * and due date, supporting both LocalDate and LocalDateTime formats.
     *
     * @param content The saved deadline task content including description and date.
     * @param isDone The completion status of the deadline task.
     * @return A reconstructed Deadline task object.
     */
    private Task createDeadlineFromContent(String content, boolean isDone) {
        String[] parts = content.split(" \\(by: ", 2);
        String description = parts[0];
        String dateString = parts[1].substring(0, parts[1].length() - 1);

        if (dateString.contains("T")) {
            return TaskFactory.createDeadline(description, isDone, 
                java.time.LocalDateTime.parse(dateString));
        } else {
            return TaskFactory.createDeadline(description, isDone, 
                java.time.LocalDate.parse(dateString));
        }
    }

    /**
     * Creates an Event task from saved content by parsing the description,
     * start time, and end time, supporting both LocalDate and LocalDateTime formats.
     *
     * @param content The saved event task content including description and time range.
     * @param isDone The completion status of the event task.
     * @return A reconstructed Event task object.
     */
    private Task createEventFromContent(String content, boolean isDone) {
        String[] parts = content.split(" \\(from: ", 2);
        String description = parts[0];
        String[] dateParts = parts[1].split(" to: ", 2);
        String startString = dateParts[0];
        String endString = dateParts[1].substring(0, dateParts[1].length() - 1);

        if (startString.contains("T") && endString.contains("T")) {
            return TaskFactory.createEvent(description, isDone,
                java.time.LocalDateTime.parse(startString),
                java.time.LocalDateTime.parse(endString));
        } else {
            return TaskFactory.createEvent(description, isDone,
                java.time.LocalDate.parse(startString),
                java.time.LocalDate.parse(endString));
        }
    }
}
