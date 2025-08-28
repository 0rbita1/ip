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

    public static void interpretAndLoadDeadlineSave(String taskContent, Boolean isDone, ArrayList<Task> tasks) {
        String[] descriptionDateSplit = taskContent.split(" \\(by: ", 2);
        String description = descriptionDateSplit[0];
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
