package keeka;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskList {
    public TaskList() {

    }

    public static void addToDo(String description, Boolean isDone, ArrayList<Task> tasks)
            throws IOException {
        ToDo newToDo = new ToDo(description, isDone);
        tasks.add(newToDo);
        Storage.addToDoToSave(newToDo, tasks);
    }

    public static void addDeadline(String description, Boolean isDone, LocalDateTime date, ArrayList<Task> tasks)
            throws IOException {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
        Storage.addDeadlineToSave(newDeadline, tasks);
    }

    public static void addDeadline(String description, Boolean isDone, LocalDate date, ArrayList<Task> tasks)
            throws IOException {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
        Storage.addDeadlineToSave(newDeadline, tasks);
    }

    public static void addEvent(String description, Boolean isDone, LocalDateTime start, LocalDateTime end,
                                 ArrayList<Task> tasks) throws IOException {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
        Storage.addEventToSave(newEvent, tasks);
    }

    public static void addEvent(String description, Boolean isDone, LocalDate start, LocalDate end,
                                 ArrayList<Task> tasks) throws IOException {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
        Storage.addEventToSave(newEvent, tasks);
    }

    public static void markTask(ArrayList<Task> tasks, int index) throws IOException {
        if (index < 1 || index > tasks.size()) {
            Ui.printMessage("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        Task desiredTask = tasks.get(index - 1);
        desiredTask.setDone();
        tasks.set(index - 1, desiredTask);
        Ui.printMessage("Task successfully marked as done:\n" + desiredTask);
        Storage.updateTaskInSave(tasks);
    }

    public static void unmarkTask(ArrayList<Task> tasks, int index) throws IOException {
        if (index < 1 || index > tasks.size()) {
            Ui.printMessage("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        Task desiredTask = tasks.get(index - 1);
        desiredTask.setNotDone();
        tasks.set(index - 1, desiredTask);
        Ui.printMessage("Task successfully marked as NOT done:\n" + desiredTask);
        Storage.updateTaskInSave(tasks);
    }

    public static void deleteTask(ArrayList<Task> tasks, int index) throws IOException {
        if (index < 1 || index > tasks.size()) {
            Ui.printMessage("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        Task desiredTask = tasks.get(index - 1);
        tasks.remove(index - 1);
        Ui.printMessage("Task successfully deleted:\n" + desiredTask + "\n" + "Task counter: " + tasks.size());
        Storage.updateTaskInSave(tasks);
    }

    public static ArrayList<Task> findQuery(String query, ArrayList<Task> tasks) {
        ArrayList<Task> result = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String description = task.getDescription();

            if (description.contains(query)) {
                result.add(task);
            }
        }

        return result;
    }




}
