package keeka;

import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    final static String SAVE_FILE_PATH = "src/main/java/keeka/List.txt";
    static File saveFile;
    public Storage() {
        saveFile = new File(SAVE_FILE_PATH);
    }


    public static void loadSaveContents(ArrayList<Task> tasks) {
        try {
            ArrayList<String> saveContents = retrieveSaveContents();

            for (int i = 0; i < saveContents.size(); i++) {
                String saveContent = saveContents.get(i);

                ParsedSaveContent parsedSaveContent = Parser.parseSaveContent(saveContent);
                char taskCode = parsedSaveContent.getTaskCode();
                boolean isDone = parsedSaveContent.getMarkedStatus();
                String taskContent = parsedSaveContent.getTaskContent();

                switch (taskCode) {
                case 'T' -> loadToDo(taskContent, isDone, tasks);
                case 'D' -> Interpreter.interpretAndLoadDeadlineSave(taskContent, isDone, tasks);
                case 'E' -> Interpreter.interpretAndLoadEventSave(taskContent, isDone, tasks);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found! " + e);
        }
    }


    /**
     * Retrieves the contents of the save file and returns them as a list of strings.
     *
     * This method reads each line from the file located at `SAVE_FILE_PATH` using a `Scanner`.
     * Each line, representing a saved task, is added to an `ArrayList<String>`.
     * The process continues until the end of the file is reached, after which the populated list is returned.
     *
     * @return An `ArrayList` of strings, where each string is a line from the save file.
     * @throws FileNotFoundException If the save file does not exist at the specified path.
     */
    public static ArrayList<String> retrieveSaveContents() throws FileNotFoundException {
        saveFile = new File(SAVE_FILE_PATH);
        Scanner saveFileScanner = new Scanner(saveFile);
        ArrayList<String> saveContents = new ArrayList<>();

        while (saveFileScanner.hasNext()) {
            String currentLine = saveFileScanner.nextLine();
            saveContents.add(currentLine);
        }

        return saveContents;
    }

    /**
     * Loads a ToDo task and adds it to the task list.
     *
     * This method creates a new `ToDo` object with the given description and completion status.
     * It then adds this newly created `ToDo` task to the provided `ArrayList` of tasks.
     *
     * @param description The textual description of the ToDo task.
     * @param isDone      A boolean indicating whether the task is marked as done.
     * @param tasks       The `ArrayList` to which the new ToDo task will be added.
     */
    public static void loadToDo(String description, Boolean isDone, ArrayList<Task> tasks) {
        ToDo newToDo = new ToDo(description, isDone);
        tasks.add(newToDo);
    }

    /**
     * Loads a deadline task with a `LocalDateTime` and adds it to the task list.
     *
     * This method is an overloaded version of `loadDeadline`. It creates a new `Deadline` object
     * using a specific description, completion status, and a `LocalDateTime` object representing the deadline date and
     * time.
     * The created task is then added to the provided `ArrayList`.
     *
     * @param description The textual description of the deadline task.
     * @param isDone      A boolean indicating whether the task is marked as done.
     * @param date        The `LocalDateTime` object representing the deadline.
     * @param tasks       The `ArrayList` to which the new deadline task will be added.
     */
    public static void loadDeadline(String description, Boolean isDone, LocalDateTime date, ArrayList<Task> tasks)  {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
    }

    /**
     * Loads a deadline task with a `LocalDate` and adds it to the task list.
     *
     * This method is an overloaded version of `loadDeadline`. It creates a new `Deadline` object
     * using a specific description, completion status, and a `LocalDate` object representing the deadline date.
     * The created task is then added to the provided `ArrayList`.
     *
     * @param description The textual description of the deadline task.
     * @param isDone      A boolean indicating whether the task is marked as done.
     * @param date        The `LocalDate` object representing the deadline.
     * @param tasks       The `ArrayList` to which the new deadline task will be added.
     */
    public static void loadDeadline(String description, Boolean isDone, LocalDate date, ArrayList<Task> tasks)  {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
    }

    /**
     * Loads an event task with `LocalDateTime` objects and adds it to the task list.
     *
     * This method is an overloaded version of `loadEvent`. It creates a new `Event` object
     * with the given description, completion status, and start and end times as `LocalDateTime` objects.
     * The created event task is then added to the provided `ArrayList`.
     *
     * @param description The textual description of the event.
     * @param isDone      A boolean indicating whether the task is marked as done.
     * @param start       The `LocalDateTime` object representing the event's start time.
     * @param end         The `LocalDateTime` object representing the event's end time.
     * @param tasks       The `ArrayList` to which the new event task will be added.
     */
    public static void loadEvent(String description, Boolean isDone, LocalDateTime start, LocalDateTime end,
                                  ArrayList<Task> tasks) {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
    }

    /**
     * Loads an event task with `LocalDate` objects and adds it to the task list.
     *
     * This method is an overloaded version of `loadEvent`. It creates a new `Event` object
     * with the given description, completion status, and start and end dates as `LocalDate` objects.
     * The created event task is then added to the provided `ArrayList`.
     *
     * @param description The textual description of the event.
     * @param isDone      A boolean indicating whether the task is marked as done.
     * @param start       The `LocalDate` object representing the event's start date.
     * @param end         The `LocalDate` object representing the event's end date.
     * @param tasks       The `ArrayList` to which the new event task will be added.
     */
    public static void loadEvent(String description, Boolean isDone, LocalDate start, LocalDate end,
                                  ArrayList<Task> tasks) {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
    }

    /**
     * Appends a ToDo task to the save file.
     *
     * This method formats the ToDo task into a save-friendly string representation.
     * It uses a `FileWriter` in append mode to write this string to the save file
     * located at `SAVE_FILE_PATH`, ensuring that new content is added to the end.
     *
     * @param task The `ToDo` object to be saved.
     * @param tasks The `ArrayList` of tasks used to determine the correct index for saving.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void addToDoToSave(ToDo task, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + task.toString() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    /**
     * Appends a Deadline task to the save file.
     *
     * This method formats the Deadline task's ISO string representation and appends it to the save file.
     * It uses a `FileWriter` in append mode to write the task's details to `SAVE_FILE_PATH`.
     *
     * @param deadline The `Deadline` object to be saved.
     * @param tasks    The `ArrayList` of tasks used to determine the correct index for saving.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void addDeadlineToSave(Deadline deadline, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + deadline.toString() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    /**
     * Appends an Event task to the save file.
     *
     * This method formats the Event task's ISO string representation and appends it to the save file.
     * It uses a `FileWriter` in append mode to write the task's details to `SAVE_FILE_PATH`.
     *
     * @param event The `Event` object to be saved.
     * @param tasks The `ArrayList` of tasks used to determine the correct index for saving.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void addEventToSave(Event event, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + event.toString() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    /**
     * Rewrites the entire save file with the current contents of the task list.
     *
     * This method is used to synchronize the save file with any changes made to the task list,
     * such as marking a task as done or deleting one. It uses a `FileWriter` in overwrite mode (`false`)
     * to clear the existing file content before writing the updated list of tasks.
     * Each task in the provided `ArrayList` is formatted into a string and written to the file.
     *
     * @param tasks The `ArrayList` of `Task` objects containing the current state of the tasks.
     * @throws IOException If an I/O error occurs during the file writing process.
     */
    public static void updateTaskInSave(ArrayList<Task> tasks) throws IOException {
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, false);

        for (int i = 0; i < tasks.size(); i++) {
            String saveText = (i + 1) + ". " + tasks.get(i).toString() + "\n";
            fw.write(saveText);
        }

        fw.close();
    }



}
