package keeka;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Parser {
    final static int TASK_CODE_INDEX = 1;
    final static int MARKED_STATUS_INDEX = 5;
    final static int SUBSTRING_START_INDEX = 8;

    public Parser() {

    }

    public static ParsedSaveContent parseSaveContent(String saveContent) {

        String[] indexTaskPair = saveContent.split(". ", 2);
        char taskCode = indexTaskPair[1].charAt(TASK_CODE_INDEX);
        char markedStatus = indexTaskPair[1].charAt(MARKED_STATUS_INDEX);
        String taskContent = indexTaskPair[1].substring(SUBSTRING_START_INDEX);

        return new ParsedSaveContent(taskCode, markedStatus, taskContent);
    }

    public static void parseDeadlineInput(String remainingContent, ArrayList<Task> tasks) throws
            DateTimeParseException {
        String[] parsedDescription = remainingContent.split(" /by ");
        String deadlineDescription = parsedDescription[0];
        String deadlineDateString = parsedDescription[1];

        try {
            if (deadlineDateString.contains("T")) {
                LocalDateTime deadlineDateTime = LocalDateTime.parse(deadlineDateString);
                TaskList.addDeadline(deadlineDescription, false, deadlineDateTime, tasks);
            } else {
                LocalDate deadlineDate = LocalDate.parse(deadlineDateString);
                TaskList.addDeadline(deadlineDescription, false, deadlineDate, tasks);
            }
        } catch (DateTimeParseException | IOException e) {
            System.out.println("Invalid date format for deadline input! " + e);
            System.out.println("Deadline date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }

    }

    public static void parseEventInput(String remainingContent, ArrayList<Task> tasks) throws DateTimeParseException {
        String[] parsedDescription = remainingContent.split(" /from ");
        String eventDescription = parsedDescription[0];
        String[] parsedDuration = parsedDescription[1].split(" /to ");
        String eventStartString = parsedDuration[0];
        String eventEndString = parsedDuration[1];

        try {
            if (eventStartString.contains("T") && eventEndString.contains("T")) {
                LocalDateTime eventStart = LocalDateTime.parse(eventStartString);
                LocalDateTime eventEnd = LocalDateTime.parse(eventEndString);
                TaskList.addEvent(eventDescription, false, eventStart, eventEnd, tasks);
            } else if (!eventStartString.contains("T") && !eventEndString.contains("T")) {
                LocalDate eventStart = LocalDate.parse(eventStartString);
                LocalDate eventEnd = LocalDate.parse(eventEndString);
                TaskList.addEvent(eventDescription, false, eventStart, eventEnd, tasks);
            }
        } catch (DateTimeParseException | IOException e) {
            System.out.println("Invalid date format for event input! " + e);
            System.out.println("Event date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }
    }

}
