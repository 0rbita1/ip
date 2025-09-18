package keeka.backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import keeka.tasks.Task;

/**
 * Handles all file I/O operations for task persistence and data management.
 * Provides methods for saving individual tasks, updating entire task lists,
 * and loading previously saved tasks from storage files.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a Storage instance with the specified file path for data persistence.
     * Automatically creates the storage file and necessary directories if they don't exist.
     *
     * @param filePath The file path where tasks will be saved and loaded from.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists();
    }

    /**
     * Appends a single task to the storage file with its assigned task number.
     * Used when adding new tasks to maintain sequential numbering.
     *
     * @param task The task to be saved to the storage file.
     * @param taskNumber The sequential number assigned to this task in the list.
     * @throws IOException If an error occurs during file writing operations.
     */
    public void saveTask(Task task, int taskNumber) throws IOException {

        assert task != null : "Task to save must not be null";
        assert taskNumber > 0 : "Task number must be positive";

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(taskNumber + ". " + task.toString() + "\n");
        }
    }

    /**
     * Completely rewrites the storage file with the current state of all tasks.
     * Used when tasks are modified, deleted, or reordered to maintain consistency.
     *
     * @param tasks The complete list of tasks to be written to storage.
     * @throws IOException If an error occurs during file writing operations.
     */
    public void updateAllTasks(List<Task> tasks) throws IOException {

        assert tasks != null : "Task list must not be null";

        try (FileWriter writer = new FileWriter(filePath, false)) {
            for (int i = 0; i < tasks.size(); i++) {
                writer.write((i + 1) + ". " + tasks.get(i).toString() + "\n");
            }
        }
    }

    /**
     * Reads and returns all saved task content lines from the storage file.
     * Each line represents a complete task with its formatting and details.
     *
     * @return A list of strings, each representing a saved task line.
     * @throws IOException If an error occurs during file reading operations.
     */
    public List<String> loadSaveContents() throws IOException {

        File file = new File(filePath);
        assert file.exists() : "Storage file should exist before loading contents";

        List<String> contents = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                contents.add(scanner.nextLine());
            }
        }

        return contents;
    }

    /**
     * Creates the storage file and its parent directories if they don't already exist.
     * Ensures the application has a valid location to save task data.
     */
    private void createFileIfNotExists() {
        File file = new File(filePath);
        try {
            // Only create parent directories if they exist (i.e., the file has a parent directory)
            File parentDir = file.getParentFile();
            if (parentDir != null) {
                parentDir.mkdirs();
            }
            file.createNewFile();
            assert file.exists() : "Storage file should exist after creation";
        } catch (IOException e) {
            System.err.println("Failed to create storage file: " + e.getMessage());
        }
    }
}
