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
        boolean isTask = false;
        boolean isMark = false;
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

                isTask = taskInterpreter(userInput, taskList);
                isMark = markingInterpreter(userInput, taskList);

                if (!isTask && !isMark) {
                    throw new Exception("Invalid command!");
                }

            } catch (Exception e) {
                System.out.println("_________________________________________________");
                System.out.println(e.getMessage());
                System.out.println("_________________________________________________");
            }  finally {
                userInput = scanner.nextLine();
            }
        }
    }

    public static boolean taskInterpreter(String userInput, ArrayList<Task> taskList) throws InvalidTaskException {

        try {
            String[] parsedInput = userInput.split(" ", 2);
            String firstElement = parsedInput[0];
            String description = parsedInput[1];

            if (Objects.equals(firstElement, "todo") || (Objects.equals(firstElement, "deadline")) || (Objects.equals(firstElement, "event"))) {

                switch (firstElement) {
                    case "todo" -> {
                        ToDo newToDo = new ToDo(description, false);
                        taskList.add(newToDo);
                    }
                    case "deadline" -> {
                        String[] parsedDescription = description.split(" /by ");
                        String deadlineDescription = parsedDescription[0];
                        String deadlineDate = parsedDescription[1];
                        Deadline newDeadline = new Deadline(deadlineDescription, false, deadlineDate);
                        taskList.add(newDeadline);
                    }
                    case "event" -> {
                        String[] parsedDescription = description.split(" /from ");
                        String eventDescription = parsedDescription[0];
                        String[] parsedDuration = parsedDescription[1].split(" /to ");
                        String eventStart = parsedDuration[0];
                        String eventEnd = parsedDuration[1];
                        Event newEvent = new Event(eventDescription, false, eventStart, eventEnd);
                        taskList.add(newEvent);
                    }
                }
                successfulTaskAddPrinter(taskList);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public static void successfulTaskAddPrinter(ArrayList<Task> taskList) {
        System.out.println("_________________________________________________");
        System.out.println("Task successfully added: ");
        System.out.println(taskList.get(taskList.size() - 1));
        System.out.println("Task counter: " + taskList.size());
        System.out.println("_________________________________________________");
    }

    public static boolean markingInterpreter(String userInput, ArrayList<Task> taskList) throws InvalidMarkingException {

        try {
            String[] markingArray = userInput.split(" ");
            String isMarkString = markingArray[0];
            int indexNumber = Integer.parseInt(markingArray[1]);

            if ((Objects.equals(isMarkString, "mark")) || (Objects.equals(isMarkString, "unmark"))) {
                switch (isMarkString) {
                    case "mark" -> {
                        listMarker(taskList, indexNumber);
                    }
                    case "unmark" -> {
                        listUnmarker(taskList, indexNumber);
                    }
                }
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
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

    // Unmarks items in list, updates status in list
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

