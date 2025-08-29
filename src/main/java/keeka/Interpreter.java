package keeka;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Interpreter {

    public Interpreter() {

    }

    /**
     * Interprets the save content and calls the appropriate loadDeadline method depending on the format of the dateTime
     * The method parses a string taskContent to extract the description and the deadline date/time.
     * It supports two date formats: YYYY-MM-DD for a date only, and YYYY-MM-DDTHH:MM:SS for date with time
     * If the parsing is successful, it calls Storage.loadDeadline to create and add the task.
     * If the date string is in an invalid format, a DateTimeParseException is caught, and an error message is printed
     * to the console.
     *
     * @param taskContent The string content of the task, expected to be in the format "description (by: date)".
     * @param isDone      A boolean indicating whether the task is marked as done.
     * @param tasks       The ArrayList to which the loaded task will be added.
     */
    public static void interpretAndLoadDeadlineSave(String taskContent, Boolean isDone, ArrayList<Task> tasks) {
        String[] descriptionDateSplit = taskContent.split(" \\(by: ", 2);
        String dateString = descriptionDateSplit[1].substring(0, descriptionDateSplit[1].length() - 1);

        try {
            if (dateString.contains("T")) {
                LocalDateTime deadlineDateTime = LocalDateTime.parse(dateString);
                Storage.loadDeadline(dateString, isDone, deadlineDateTime, tasks);
            } else {
                LocalDate deadlineDate = LocalDate.parse(dateString);
                Storage.loadDeadline(dateString, isDone, deadlineDate, tasks);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format for deadline input! " + e);
            System.out.println("Deadline date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }
    }

    /**
     * Interprets the save content and calls the appropriate loadEvent method depending on the format of the dateTime
     * This method parses the taskContent string to extract the task description and the event's start and end dates or
     * times.
     * It handles two distinct date/time formats: YYYY-MM-DD for date-only events and YYYY-MM-DDTHH:MM:SS for events
     * with specific times.
     * If the parsing is successful, it calls Storage.loadEvent to create and add the task.
     * If the date string is in an invalid format, a DateTimeParseException is caught, and an error message is printed
     * to the console.
     *
     * @param taskContent The string content of the event, expected to be in the format "description (from: start_date
     *                    to: end_date)".
     * @param isDone      A boolean indicating whether the task is marked as done.
     * @param tasks       The ArrayList to which the loaded task will be added.
     */
    public static void interpretAndLoadEventSave(String taskContent, Boolean isDone, ArrayList<Task> tasks) {
        String[] descriptionDurationSplit = taskContent.split(" \\(from: ", 2);
        String description = descriptionDurationSplit[0];
        String[] durationSplit = descriptionDurationSplit[1].split(" to: ", 2);
        String eventStartString = durationSplit[0];
        String eventEndString = durationSplit[1].substring(0, durationSplit[1].length() - 1);

        try {
            if (eventStartString.contains("T") && eventEndString.contains("T")) {
                LocalDateTime eventStart = LocalDateTime.parse(eventStartString);
                LocalDateTime eventEnd = LocalDateTime.parse(eventEndString);
                Storage.loadEvent(description, isDone, eventStart, eventEnd, tasks);
            } else if (!eventStartString.contains("T") && !eventEndString.contains("T")) {
                LocalDate eventStart = LocalDate.parse(eventStartString);
                LocalDate eventEnd = LocalDate.parse(eventEndString);
                Storage.loadEvent(description, isDone, eventStart, eventEnd, tasks);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format for event input! " + e);
            System.out.println("Event date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }

    }

    /**
     * Reads and interprets user commands from the console until the "bye" command is received.
     * The method continuously reads a line of input from the user. It first checks for the "list" command to display
     * the current tasks.
     * For all other commands, it attempts to parse the input to determine the command type
     * (e.g., todo, deadline, event, mark, unmark, delete, find).
     * It then calls the appropriate helper method to process the command. If the command is not recognized, it throws
     * an Exception which is caught and a relevant error message is printed.
     *
     * @param tasks The ArrayList of Task objects that the commands will operate on.
     */
    public static void interpretInputs(ArrayList<Task> tasks) {
        Scanner s = new Scanner(System.in);
        String input = "";
        input = s.nextLine();

        while (!Objects.equals(input, "bye")) {
            if (Objects.equals(input, "list")) {
                Ui.printList(tasks);
                input = s.nextLine();
                continue;
            }

            try {
                String[] parsedInput = input.split(" ", 2);
                String firstWord = parsedInput[0];

                if (Objects.equals(firstWord, "todo") || (Objects.equals(firstWord, "deadline")) ||
                        (Objects.equals(firstWord, "event"))) {
                    interpretTask(input, tasks);
                } else if (Objects.equals(firstWord, "mark") || (Objects.equals(firstWord, "unmark"))) {
                    interpretMark(input, tasks);
                } else if (Objects.equals(firstWord, "delete")) {
                    interpretDelete(input, tasks);
                } else if (Objects.equals(firstWord, "find")) {
                    interpretFind(input, tasks);
                } else {
                    throw new Exception("Invalid command!");
                }

            } catch (Exception e) {
                Ui.printMessage(e.getMessage());
            } finally {
                input = s.nextLine();
            }

        }
    }

    private static void interpretTask(String userInput, ArrayList<Task> tasks) throws InvalidTaskException {
        try {
            String[] parsedInput = userInput.split(" ", 2);
            String taskType = parsedInput[0];
            String remainingContent = parsedInput[1];

            switch (taskType) {
            case "todo" -> {
                TaskList.addToDo(remainingContent, false, tasks);
                Ui.printSuccessfulTaskAddition(tasks);
            }
            case "deadline" -> {
                Parser.parseDeadlineInput(remainingContent, tasks);
                Ui.printSuccessfulTaskAddition(tasks);
            }
            case "event" -> {
                Parser.parseEventInput(remainingContent, tasks);
                Ui.printSuccessfulTaskAddition(tasks);
            }
            }

        } catch (Exception e) {
            throw new InvalidTaskException("Invalid task invocation!");
        }
    }

    private static void interpretMark(String input, ArrayList<Task> tasks) throws InvalidMarkingException {
        try {
            String[] markingArray = input.split(" ");
            String isMarkString = markingArray[0];
            int indexNumber = Integer.parseInt(markingArray[1]);

            switch (isMarkString) {
            case "mark" -> TaskList.markTask(tasks, indexNumber);
            case "unmark" -> TaskList.unmarkTask(tasks, indexNumber);
            }

        } catch (Exception e) {
            throw new InvalidMarkingException("Invalid marking invocation!");
        }
    }

    private static void interpretDelete(String input, ArrayList<Task> tasks) throws Exception {
        try {
            String[] deletionArray = input.split(" ");
            int indexNumber = Integer.parseInt(deletionArray[1]);
            TaskList.deleteTask(tasks, indexNumber);

        } catch (Exception e) {
            throw new Exception("Invalid deletion invocation!");
        }
    }

    private static void interpretFind(String input, ArrayList<Task> tasks) throws Exception {
        try {
            String[] findQuerySplit = input.split(" ");
            String query = findQuerySplit[1];
            ArrayList<Task> queryResult = TaskList.findQuery(query, tasks);
            Ui.printQueryList(queryResult);
        } catch (Exception e) {
            throw new Exception("Invalid find invocation!");
        }
    }
}
