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

    // Attempts to parse first word to call the appropriate method
    private static void inputHandler() {

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

                String[] parsedInput = userInput.split(" ", 2);
                String firstWord = parsedInput[0];

                if (Objects.equals(firstWord, "todo") || (Objects.equals(firstWord, "deadline")) || (Objects.equals(firstWord, "event"))) {
                    taskInterpreter(userInput, taskList);
                } else if (Objects.equals(firstWord, "mark") || (Objects.equals(firstWord, "unmark"))) {
                    markingInterpreter(userInput, taskList);
                } else if (Objects.equals(firstWord, "delete")) {
                    deleteInterpreter(userInput, taskList);
                } else {
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

    // Creates the appropriate task and adds to list
    private static void taskInterpreter(String userInput, ArrayList<Task> taskList) throws InvalidTaskException {

        try {
            String[] parsedInput = userInput.split(" ", 2);
            String firstElement = parsedInput[0];
            String description = parsedInput[1];

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

        } catch (Exception e) {
            throw new InvalidTaskException("Invalid task invocation!");
        }
    }


    private static void successfulTaskAddPrinter(ArrayList<Task> taskList) {
        System.out.println("_________________________________________________");
        System.out.println("Task successfully added: ");
        System.out.println(taskList.get(taskList.size() - 1));
        System.out.println("Task counter: " + taskList.size());
        System.out.println("_________________________________________________");
    }

    // Parses first word to determine which marking method to call
    private static void markingInterpreter(String userInput, ArrayList<Task> taskList) throws InvalidMarkingException {

        try {
            String[] markingArray = userInput.split(" ");
            String isMarkString = markingArray[0];
            int indexNumber = Integer.parseInt(markingArray[1]);

            switch (isMarkString) {
                case "mark" -> listMarker(taskList, indexNumber);
                case "unmark" -> listUnmarker(taskList, indexNumber);
            }

        } catch (Exception e) {
            throw new InvalidMarkingException("Invalid marking invocation!");
        }
    }

    // Parses input to determine index at which item is to be deleted
    private static void deleteInterpreter(String userInput, ArrayList<Task> taskList) throws Exception {

        try {
            String[] deletionArray = userInput.split(" ");
            String deleteString = deletionArray[0];
            int indexNumber = Integer.parseInt(deletionArray[1]);
            listDeleter(taskList, indexNumber);

        } catch (Exception e) {
            throw new Exception("Invalid deletion invocation!");
        }
    }

    // Deletes item at specified index (1-indexed)
    private static void listDeleter(ArrayList<Task> taskList, int index) {
        if (index < 1 || index > taskList.size()) {
            System.out.println("_________________________________________________");
            System.out.println("Invalid index number! Use an integer within the range of the size of the list");
            System.out.println("_________________________________________________");
            return;
        }

        Task desiredTask = taskList.get(index - 1);
        taskList.remove(index);

        System.out.println("_________________________________________________");
        System.out.println("Task successfully deleted:");
        System.out.println(desiredTask);
        System.out.println("Task counter: " + taskList.size());
        System.out.println("_________________________________________________");
    }

    // Text list printer
    private static void listPrinter(ArrayList<Task> taskList) {

        if (taskList.isEmpty()) {
            System.out.println("_________________________________________________");
            System.out.println("List is empty! Add tasks to display");
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
            System.out.println("Invalid index number! Use an integer within the range of the size of the list");
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
            System.out.println("Invalid index number! Use an integer within the range of the size of the list");
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

