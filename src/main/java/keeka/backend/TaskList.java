package keeka.backend;

import java.util.ArrayList;
import java.util.List;

import keeka.tasks.Task;

/**
 * Manages a collection of tasks with comprehensive operations for adding, removing,
 * searching, and modifying tasks. Provides a centralized interface for all
 * task list operations while maintaining data integrity and validation.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList ready to store and manage tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new task to the end of the task list.
     *
     * @param task The task to be added to the list.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list at the specified index position.
     * Only removes the task if the index is valid (within bounds).
     *
     * @param index The zero-based index of the task to be removed.
     */
    public void removeTask(int index) {
        if (isValidIndex(index)) {
            tasks.remove(index);
        }
    }

    /**
     * Retrieves the task at the specified index position in the list.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The task at the specified index, or null if the index is invalid.
     */
    public Task getTask(int index) {
        return isValidIndex(index) ? tasks.get(index) : null;
    }

    /**
     * Replaces an existing task at the specified index with a new task.
     * Only performs the replacement if the index is valid (within bounds).
     *
     * @param index The zero-based index of the task to be replaced.
     * @param newTask The new task that will replace the existing task.
     */
    public void replaceTask(int index, Task newTask) {
        if (isValidIndex(index)) {
            tasks.set(index, newTask);
        }
    }

    /**
     * Searches for tasks that contain the specified keyword in their description.
     * Returns a filtered list of all matching tasks.
     *
     * @param keyword The search term to look for in task descriptions.
     * @return A list of tasks whose descriptions contain the keyword.
     */
    public List<Task> findTasks(String keyword) {
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .toList();
    }

    /**
     * Returns the current number of tasks in the list.
     *
     * @return The total count of tasks currently stored in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether the task list is currently empty.
     *
     * @return True if the list contains no tasks, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns a copy of all tasks in the list to prevent external modification.
     * This ensures the internal list remains protected from direct manipulation.
     *
     * @return A new ArrayList containing all tasks in the current list.
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Validates whether the provided index is within the valid range of the task list.
     *
     * @param index The index to validate.
     * @return True if the index is valid (0 <= index < size), false otherwise.
     */
    private boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }
}
