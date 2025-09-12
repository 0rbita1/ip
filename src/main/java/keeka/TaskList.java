package keeka;

import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskList {
    public TaskList() {

    }

    /**
     * Creates and adds a new `ToDo` task to the task list and saves it to a file.
     *
     * This method instantiates a new `ToDo` object with the provided description and completion status.
     * It then adds the new task to the `ArrayList` and subsequently calls a method in the `Storage` class
     * to persist the task in the save file.
     *
     * @param description The textual description of the to-do task.
     * @param isDone      A boolean indicating whether the task is initially marked as done.
     * @param tasks       The `ArrayList` of tasks to which the new to-do task will be added.
     * @throws IOException If an I/O error occurs while saving the task to the file.
     */
    public static void addToDo(String description, Boolean isDone, ArrayList<Task> tasks)
            throws IOException {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        assert isDone != null : "isDone parameter cannot be null";
        assert tasks != null : "Task list cannot be null";

        int initialSize = tasks.size();
        ToDo newToDo = new ToDo(description, isDone);
        tasks.add(newToDo);

        assert tasks.size() == initialSize + 1 : "Task should be added to the list";
        assert tasks.get(tasks.size() - 1) == newToDo : "New task should be at the end of the list";

        Storage.addToDoToSave(newToDo, tasks);
    }

    /**
     * Creates and adds a new `Deadline` task with a `LocalDateTime` to the task list and saves it.
     *
     * This is an overloaded method that creates a new `Deadline` object with a description, completion status, and a
     * `LocalDateTime` for the deadline. The new task is added to the `ArrayList` and then saved to the file via a call
     * to `Storage.addDeadlineToSave`.
     *
     * @param description The textual description of the deadline task.
     * @param isDone      A boolean indicating whether the task is initially marked as done.
     * @param date        The `LocalDateTime` object representing the deadline date and time.
     * @param tasks       The `ArrayList` of tasks to which the new deadline task will be added.
     * @throws IOException If an I/O error occurs while saving the task to the file.
     */
    public static void addDeadline(String description, Boolean isDone, LocalDateTime date, ArrayList<Task> tasks)
            throws IOException {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        assert isDone != null : "isDone parameter cannot be null";
        assert date != null : "Deadline date cannot be null";
        assert tasks != null : "Task list cannot be null";

        int initialSize = tasks.size();
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);

        assert tasks.size() == initialSize + 1 : "Task should be added to the list";
        assert tasks.get(tasks.size() - 1) == newDeadline : "New task should be at the end of the list";

        Storage.addDeadlineToSave(newDeadline, tasks);
    }

    /**
     * Creates and adds a new `Deadline` task with a `LocalDate` to the task list and saves it.
     *
     * This is an overloaded method that creates a new `Deadline` object with a description, completion status, and a
     * `LocalDate` for the deadline. The new task is added to the `ArrayList` and then saved to the file via a call to
     * `Storage.addDeadlineToSave`.
     *
     * @param description The textual description of the deadline task.
     * @param isDone      A boolean indicating whether the task is initially marked as done.
     * @param date        The `LocalDate` object representing the deadline date.
     * @param tasks       The `ArrayList` of tasks to which the new deadline task will be added.
     * @throws IOException If an I/O error occurs while saving the task to the file.
     */
    public static void addDeadline(String description, Boolean isDone, LocalDate date, ArrayList<Task> tasks)
            throws IOException {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        assert isDone != null : "isDone parameter cannot be null";
        assert date != null : "Deadline date cannot be null";
        assert tasks != null : "Task list cannot be null";

        int initialSize = tasks.size();
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);

        assert tasks.size() == initialSize + 1 : "Task should be added to the list";
        assert tasks.get(tasks.size() - 1) == newDeadline : "New task should be at the end of the list";

        Storage.addDeadlineToSave(newDeadline, tasks);
    }

    /**
     * Creates and adds a new `Event` task with `LocalDateTime` objects to the task list and saves it.
     *
     * This overloaded method creates an `Event` object with a description, completion status, and start and end times
     * as `LocalDateTime` objects. The task is then added to the provided `ArrayList` and saved to the file using
     * `Storage.addEventToSave`.
     *
     * @param description The textual description of the event.
     * @param isDone      A boolean indicating whether the task is initially marked as done.
     * @param start       The `LocalDateTime` object for the event's start time.
     * @param end         The `LocalDateTime` object for the event's end time.
     * @param tasks       The `ArrayList` of tasks to which the new event task will be added.
     * @throws IOException If an I/O error occurs while saving the task to the file.
     */
    public static void addEvent(String description, Boolean isDone, LocalDateTime start, LocalDateTime end,
                                ArrayList<Task> tasks) throws IOException {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        assert isDone != null : "isDone parameter cannot be null";
        assert start != null : "Event start time cannot be null";
        assert end != null : "Event end time cannot be null";
        assert start.isBefore(end) || start.isEqual(end) : "Event start time must be before or equal to end time";
        assert tasks != null : "Task list cannot be null";

        int initialSize = tasks.size();
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);

        assert tasks.size() == initialSize + 1 : "Task should be added to the list";
        assert tasks.get(tasks.size() - 1) == newEvent : "New task should be at the end of the list";

        Storage.addEventToSave(newEvent, tasks);
    }

    /**
     * Creates and adds a new `Event` task with `LocalDate` objects to the task list and saves it.
     *
     * This overloaded method creates an `Event` object with a description, completion status, and start and end dates
     * as `LocalDate` objects. The task is then added to the provided `ArrayList` and saved to the file using
     * `Storage.addEventToSave`.
     *
     * @param description The textual description of the event.
     * @param isDone      A boolean indicating whether the task is initially marked as done.
     * @param start       The `LocalDate` object for the event's start date.
     * @param end         The `LocalDate` object for the event's end date.
     * @param tasks       The `ArrayList` of tasks to which the new event task will be added.
     * @throws IOException If an I/O error occurs while saving the task to the file.
     */
    public static void addEvent(String description, Boolean isDone, LocalDate start, LocalDate end,
                                ArrayList<Task> tasks) throws IOException {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        assert isDone != null : "isDone parameter cannot be null";
        assert start != null : "Event start date cannot be null";
        assert end != null : "Event end date cannot be null";
        assert start.isBefore(end) || start.isEqual(end) : "Event start date must be before or equal to end date";
        assert tasks != null : "Task list cannot be null";

        int initialSize = tasks.size();
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);

        assert tasks.size() == initialSize + 1 : "Task should be added to the list";
        assert tasks.get(tasks.size() - 1) == newEvent : "New task should be at the end of the list";

        Storage.addEventToSave(newEvent, tasks);
    }

    /**
     * Marks a specific task in the task list as done and updates the save file.
     *
     * This method validates the provided index to ensure it is within the bounds of the task list.
     * If the index is valid, it retrieves the corresponding task, marks it as done using `setDone()`,
     * updates the task in the list, and prints a confirmation message. Finally, it calls a `Storage` method to rewrite
     * the entire save file to reflect the change.
     *
     * @param tasks The `ArrayList` of tasks to be modified.
     * @param index The 1-based index of the task to be marked.
     * @throws IOException If an I/O error occurs while updating the save file.
     */
    public static void markTask(ArrayList<Task> tasks, int index) throws IOException {
        assert tasks != null : "Task list cannot be null";

        if (index < 1 || index > tasks.size()) {
            Ui.addMessageToBuffer("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        Task desiredTask = tasks.get(index - 1);
        assert desiredTask != null : "Task at valid index should not be null";

        desiredTask.setDone();
        tasks.set(index - 1, desiredTask);

        assert tasks.get(index - 1) == desiredTask : "Updated task should be in the correct position";

        Ui.addMessageToBuffer("Task successfully marked as done:\n" + desiredTask);
        Storage.updateTaskInSave(tasks);
    }

    /**
     * Marks a specific task in the task list as not done and updates the save file.
     *
     * This method validates the provided index to ensure it is within the bounds of the task list.
     * If the index is valid, it retrieves the corresponding task, marks it as not done using `setNotDone()`,
     * updates the task in the list, and prints a confirmation message. Finally, it calls a `Storage` method to rewrite
     * the entire save file to reflect the change.
     *
     * @param tasks The `ArrayList` of tasks to be modified.
     * @param index The 1-based index of the task to be unmarked.
     * @throws IOException If an I/O error occurs while updating the save file.
     */
    public static void unmarkTask(ArrayList<Task> tasks, int index) throws IOException {
        assert tasks != null : "Task list cannot be null";

        if (index < 1 || index > tasks.size()) {
            Ui.addMessageToBuffer("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        Task desiredTask = tasks.get(index - 1);
        assert desiredTask != null : "Task at valid index should not be null";

        desiredTask.setNotDone();
        tasks.set(index - 1, desiredTask);

        assert tasks.get(index - 1) == desiredTask : "Updated task should be in the correct position";

        Ui.addMessageToBuffer("Task successfully marked as NOT done:\n" + desiredTask);
        Storage.updateTaskInSave(tasks);
    }

    /**
     * Deletes a specific task from the task list and updates the save file.
     *
     * This method validates the provided index to ensure it is within the bounds of the task list.
     * If the index is valid, it removes the task from the `ArrayList`, prints a confirmation message,
     * and then calls a `Storage` method to rewrite the entire save file to reflect the deletion.
     *
     * @param tasks The `ArrayList` of tasks from which a task will be deleted.
     * @param index The 1-based index of the task to be deleted.
     * @throws IOException If an I/O error occurs while updating the save file.
     */
    public static void deleteTask(ArrayList<Task> tasks, int index) throws IOException {
        assert tasks != null : "Task list cannot be null";

        if (index < 1 || index > tasks.size()) {
            Ui.addMessageToBuffer("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        int initialSize = tasks.size();
        Task desiredTask = tasks.get(index - 1);
        assert desiredTask != null : "Task at valid index should not be null";

        tasks.remove(index - 1);

        assert tasks.size() == initialSize - 1 : "Task list size should decrease by 1 after deletion";
        assert !tasks.contains(desiredTask) : "Deleted task should no longer be in the list";

        Ui.addMessageToBuffer("Task successfully deleted:\n" + desiredTask + "\n" + "Task counter: " + tasks.size());
        Storage.updateTaskInSave(tasks);
    }

    /**
     * Searches for tasks containing a specific keyword in their description.
     *
     * This method iterates through the provided `ArrayList` of tasks. For each task, it checks if its description
     * contains the specified `query` string. All matching tasks are added to a new `ArrayList`, which is then returned.
     * The search is case-sensitive.
     *
     * @param query The keyword or phrase to search for within task descriptions.
     * @param tasks The `ArrayList` of tasks to be searched.
     * @return An `ArrayList` of `Task` objects that contain the search query.
     */
    public static ArrayList<Task> findQuery(String query, ArrayList<Task> tasks) {
        assert query != null : "Search query cannot be null";
        assert tasks != null : "Task list cannot be null";

        ArrayList<Task> result = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            assert task != null : "Task in list should not be null";

            String description = task.getDescription();
            assert description != null : "Task description should not be null";

            if (description.contains(query)) {
                result.add(task);
            }
        }

        assert result != null : "Result list should not be null";
        // Verify that all tasks in result actually contain the query
        for (Task task : result) {
            assert task.getDescription().contains(query) : "All result tasks should contain the search query";
        }

        return result;
    }

}