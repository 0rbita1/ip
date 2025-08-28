package keeka;

import java.util.ArrayList;

public class Ui {

    public Ui() {
    }

    public static void printSuccessfulTaskAddition(ArrayList<Task> taskList) {
        printMessage("Task successfully added:\n" + taskList.get(taskList.size() - 1) + "\n" + "Task counter: " + taskList.size());
    }

    public static void printList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            printMessage("List is empty! Add tasks to display");
            return;
        }

        String listText = "Displaying list items\n";

        for (int i = 0; i < tasks.size(); i++) {
            Task currentTask = tasks.get(i);
            listText = listText.concat((i + 1) + ". " + currentTask.toString() + "\n");
        }

        printMessage(listText);
    }

    public static void printQueryList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            printMessage("Unable to find any matches for your query!");
            return;
        }

        String listText = "Displaying items that match your query\n";

        for (int i = 0; i < tasks.size(); i++) {
            Task currentTask = tasks.get(i);
            listText = listText.concat((i + 1) + ". " + currentTask.toString() + "\n");
        }

        printMessage(listText);
    }



    public static void printGreeting() {
        printMessage("Hello! I'm Keeka\nWhat can I do for you?");
    }

    public static void printBye() {
        printMessage("Bye. Hope to see you again soon!");
    }

    public static void printMessage(String message) {
        System.out.println("_________________________________________________");
        System.out.println(message);
        System.out.println("_________________________________________________");
    }
}
