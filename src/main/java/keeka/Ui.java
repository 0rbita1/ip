package keeka;

import tasks.Task;

import java.util.ArrayList;

public class Ui {
    private static ArrayList<String> stringBuffer;

    public Ui() {
        stringBuffer = new ArrayList<>();
    }

    public static void printSuccessfulTaskAddition(ArrayList<Task> taskList) {
        addMessageToBuffer("Task successfully added:\n" + taskList.get(taskList.size() - 1) + "\n" + "Task counter: " +
                taskList.size());
    }

    public static void printList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            addMessageToBuffer("List is empty! Add tasks to display");
            return;
        }

        String listText = "Displaying list items\n";

        for (int i = 0; i < tasks.size(); i++) {
            Task currentTask = tasks.get(i);
            listText = listText.concat((i + 1) + ". " + currentTask.toString() + "\n");
        }

        addMessageToBuffer(listText);
    }

    public static void printQueryList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            addMessageToBuffer("Unable to find any matches for your query!");
            return;
        }

        String listText = "Displaying items that match your query\n";

        for (int i = 0; i < tasks.size(); i++) {
            Task currentTask = tasks.get(i);
            listText = listText.concat((i + 1) + ". " + currentTask.toString() + "\n");
        }

        addMessageToBuffer(listText);
    }

    public static void printGreeting() {
        addMessageToBuffer("Hello! I'm Keeka\nWhat can I do for you?");
    }

    public static void printBye() {
        addMessageToBuffer("Bye. Hope to see you again soon!");
    }

    public static void addMessageToBuffer(String message) {
        stringBuffer.add(message);
        System.out.println("====================================================================");
        System.out.println(message);
        System.out.println("====================================================================");
    }

    public static String retrieveMessage() {
        return stringBuffer.get(stringBuffer.size() - 1);
    }
}
