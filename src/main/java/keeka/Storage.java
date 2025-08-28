package keeka;

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

    public static void loadToDo(String description, Boolean isDone, ArrayList<Task> tasks) {
        ToDo newToDo = new ToDo(description, isDone);
        tasks.add(newToDo);
    }

    public static void loadDeadline(String description, Boolean isDone, LocalDateTime date, ArrayList<Task> tasks)  {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
    }

    public static void loadDeadline(String description, Boolean isDone, LocalDate date, ArrayList<Task> tasks)  {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
    }

    public static void loadEvent(String description, Boolean isDone, LocalDateTime start, LocalDateTime end,
                                  ArrayList<Task> tasks) {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
    }

    public static void loadEvent(String description, Boolean isDone, LocalDate start, LocalDate end,
                                  ArrayList<Task> tasks) {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
    }

    public static void addToDoToSave(ToDo task, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + task.toString() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    public static void addDeadlineToSave(Deadline deadline, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + deadline.printInISO() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    public static void addEventToSave(Event event, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + event.printISO() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    public static void updateTaskInSave(ArrayList<Task> tasks) throws IOException {
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, false);

        for (int i = 0; i < tasks.size(); i++) {
            String saveText = (i + 1) + ". " + tasks.get(i).toString() + "\n";
            fw.write(saveText);
        }

        fw.close();
    }



}
