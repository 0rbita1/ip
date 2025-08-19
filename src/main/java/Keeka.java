import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;

public class Keeka {

    public static void main(String[] args) {

        String name = "Keeka";

        // Greeting
        System.out.println("_________________________________________________");
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
        System.out.println("_________________________________________________");

        inputHandler();

        // Exit
        System.out.println("_________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("_________________________________________________");

    }

    public static void inputHandler() {

        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        ArrayList<Task> taskList = new ArrayList<>();
        userInput = scanner.nextLine();

        while (!Objects.equals(userInput, "bye")) {

            if (Objects.equals(userInput, "list")) {
                listPrinter(taskList);
                userInput = scanner.nextLine();
                continue;
            }

            try {
                String[] markingArray = userInput.split(" ");
                String isMarkString = markingArray[0];
                int indexNumber = Integer.parseInt(markingArray[1]);

                if (Objects.equals(isMarkString, "mark")) {
                    listMarker(taskList, indexNumber);
                }

                if (Objects.equals(isMarkString, "unmark")) {
                    listUnmarker(taskList, indexNumber);
                }

            } catch (Exception e){
                Task newTask = new Task(userInput, false);
                taskList.add(newTask);
                System.out.println("_________________________________________________");
                System.out.println("Task successfully added: " + newTask.getDescription());
                System.out.println("_________________________________________________");
            } finally {
                userInput = scanner.nextLine();
            }

        }

    }

    // Text list printer
    private static void listPrinter(ArrayList<Task> taskList) {

        if (taskList.isEmpty()) {
            System.out.println("_________________________________________________");
            System.out.println("List is empty! Add texts to display");
            System.out.println("_________________________________________________");
            return;
        }

        System.out.println("_________________________________________________");
        System.out.println("Displaying list items");

        for (int i = 0; i < taskList.size(); i++) {
            Task currentText = taskList.get(i);
            System.out.println((i + 1) + ". " + currentText.toString());
        }

        System.out.println("_________________________________________________");

    }

    // Marks items in list, updates status in list
    private static void listMarker(ArrayList<Task> taskList, int index) {

        if (index < 1 || index > taskList.size()) {
            System.out.println("_________________________________________________");
            System.out.println("Invalid index number!");
            System.out.println("_________________________________________________");
            return;
        }

        Task desiredTask = taskList.get(index - 1);
        desiredTask.setDone();
        taskList.set(index - 1, desiredTask);

        System.out.println("_________________________________________________");
        System.out.println("Task successfully marked as done:");
        System.out.println(desiredTask);
        System.out.println("_________________________________________________");

    }

    private static void listUnmarker(ArrayList<Task> taskList, int index) {

        if (index < 1 || index > taskList.size()) {
            System.out.println("_________________________________________________");
            System.out.println("Invalid index number!");
            System.out.println("_________________________________________________");
            return;
        }

        Task desiredTask = taskList.get(index - 1);
        desiredTask.setNotDone();
        taskList.set(index - 1, desiredTask);

        System.out.println("_________________________________________________");
        System.out.println("Task successfully marked as NOT done:");
        System.out.println(desiredTask);
        System.out.println("_________________________________________________");

    }

}

