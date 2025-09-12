package keeka;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;

import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import static keeka.Ui.addMessageToBuffer;

public class Parser {
    static final int TASK_CODE_INDEX = 1;
    static final int MARKED_STATUS_INDEX = 4;
    static final int SUBSTRING_START_INDEX = 7;

    public Parser() {

    }

    /**
     * Parses a single line of a saved task from a file and extracts its key components.
     *
     * This static method is responsible for dissecting a string from the save file,
     * which is expected to be in the format `"[index]. [taskCode][markedStatus] [taskContent]"`.
     * It splits the string to isolate the task code, the completion status, and the
     * task's description/details. The extracted information is then encapsulated
     * within a `ParsedSaveContent` object for easy access.
     *
     * @param saveContent The single-line string content of a saved task.
     * @return A `ParsedSaveContent` object containing the task's code, marked status, and content.
     */
    public static ParsedSaveContent parseSaveContent(String saveContent) {
        String[] indexTaskPair = saveContent.split(". ", 2);
        char taskCode = indexTaskPair[1].charAt(TASK_CODE_INDEX);
        char markedStatus = indexTaskPair[1].charAt(MARKED_STATUS_INDEX);
        String taskContent = indexTaskPair[1].substring(SUBSTRING_START_INDEX);

        return new ParsedSaveContent(taskCode, markedStatus, taskContent);
    }

    /**
     * Parses user input for a new deadline task and adds it to the task list.
     *
     * The method expects a string in the format `"[description] /by [date/time]"`.
     * It splits the input to separate the description from the deadline date/time.
     * It supports both a date-only format (`YYYY-MM-DD`) and a date-with-time format
     * (`YYYY-MM-DDTHH:MM:SS`). The parsed information is then used to create
     * and add a new `Deadline` task to the provided `ArrayList`.
     * Catches `DateTimeParseException` to handle invalid date formats, printing an error message.
     *
     * @param remainingContent The string containing the task description and deadline date.
     * @param tasks            The `ArrayList` to which the new deadline task will be added.
     * @throws DateTimeParseException If the date/time string is not in a valid format.
     */
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
            addMessageToBuffer("Invalid date format for deadline input! " + e + "Deadline date format as follows: "
                    + "YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }

    }

    /**
     * Parses user input for a new event task and adds it to the task list.
     *
     * This method takes a string input and parses it to extract the event's
     * description, start date/time, and end date/time. The expected format is
     * `"[description] /from [start_date] /to [end_date]"`. It can handle
     * events with dates only (`YYYY-MM-DD`) or full date and time
     * (`YYYY-MM-DDTHH:MM:SS`). The method then creates a new `Event` task and adds
     * it to the provided `ArrayList`. It includes error handling for invalid date formats.
     *
     * @param remainingContent The string containing the event description and duration.
     * @param tasks            The `ArrayList` to which the new event task will be added.
     * @throws DateTimeParseException If the date/time strings are not in a valid format.
     */
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
            System.out.println("Invalid date format for event input! " + e + "\n");
            System.out.println("Event date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");

            addMessageToBuffer("Invalid date format for event input! " + e + "\n" + "Event date format as follows: "
                    + "YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }
    }

    public static ParsedSaveContent parseUpdate(String input, ArrayList<Task> tasks) throws Exception {
        try {
            String[] indexRemainingInputSplit = input.split(" ", 2);
            int indexNumber = Integer.parseInt(indexRemainingInputSplit[0]) - 1;
            Task updateTask = tasks.get(indexNumber);
            ParsedSaveContent parsedTask = parseSaveContent("1. " + updateTask.toString());
            char taskCode = parsedTask.getTaskCode();
            boolean isMarked = parsedTask.getMarkedStatus();
            String remainingInput = indexRemainingInputSplit[1];
            String[] fieldTypeNewValueSplit = remainingInput.split(" ", 2);
            String fieldType = fieldTypeNewValueSplit[0];
            String newValue = fieldTypeNewValueSplit[1];

            switch (taskCode) {
            case 'T' -> parseToDoUpdate(indexNumber, fieldType, newValue, isMarked, tasks);
            case 'D' -> parseDeadlineUpdate(indexNumber, fieldType, newValue, isMarked, tasks);
            case 'E' -> parseEventUpdate(indexNumber, fieldType, newValue, isMarked, tasks);
            default -> throw new RuntimeException();
            }

            return parsedTask;

        } catch (RuntimeException e) {
            addMessageToBuffer("Unable to parse update command! " + e);
            throw new RuntimeException(e);
        }
    }

    public static void parseToDoUpdate(int indexNumber, String fieldType, String newValue, boolean isMarked,
                                       ArrayList<Task> tasks) throws Exception {
        try {
            if (Objects.equals(fieldType, "description")) {
                tasks.remove(indexNumber);
                ToDo toDo = new ToDo(newValue, isMarked);
                tasks.add(toDo);
                Storage.updateTaskInSave(tasks);
            } else {
                addMessageToBuffer("Only field which can be modified for ToDo task is \"description\"");
                throw new RuntimeException("Only field which can be modified for ToDo task is \"description\"");
            }
        } catch (RuntimeException e) {
            addMessageToBuffer("Unable to parse update ToDo command! " + e);
            throw new RuntimeException(e);
        }
    }

    public static void parseDeadlineUpdate(int indexNumber, String fieldType, String newValue, boolean isMarked,
                                           ArrayList<Task> tasks) {
        try {
            Deadline currentDeadline = (Deadline) tasks.get(indexNumber);

            if (Objects.equals(fieldType, "description")) {
                if (currentDeadline.getDateTime() == null) {
                    Deadline newDeadline = new Deadline(newValue, isMarked, currentDeadline.getDate());
                    tasks.add(newDeadline);
                    tasks.remove(indexNumber);
                } else if (currentDeadline.getDate() == null) {
                    Deadline newDeadline = new Deadline(newValue, isMarked, currentDeadline.getDateTime());
                    tasks.add(newDeadline);
                    tasks.remove(indexNumber);
                } else {
                    throw new RuntimeException();
                }
            } else if (Objects.equals(fieldType, "date")) {
                if (newValue.contains("T")) {
                    LocalDateTime deadlineDateTime = LocalDateTime.parse(newValue);
                    Deadline newDeadline = new Deadline(currentDeadline.getDescription(), isMarked, deadlineDateTime);
                    tasks.add(newDeadline);
                    tasks.remove(indexNumber);
                } else {
                    LocalDate deadlineDate = LocalDate.parse(newValue);
                    Deadline newDeadline = new Deadline(currentDeadline.getDescription(), isMarked, deadlineDate);
                    tasks.add(newDeadline);
                    tasks.remove(indexNumber);
                }
            }

            Storage.updateTaskInSave(tasks);
        } catch (RuntimeException | IOException e) {
            addMessageToBuffer("Unable to parse update Deadline command! " + e);
            throw new RuntimeException(e);
        }
    }

    public static void parseEventUpdate(int indexNumber, String fieldType, String newValue, boolean isMarked,
                                        ArrayList<Task> tasks) {
        try {
            Event currentEvent = (Event) tasks.get(indexNumber);

            if (Objects.equals(fieldType, "description")) {
                if (currentEvent.getStartDateTime() == null) {
                    Event newEvent = new Event(newValue, isMarked, currentEvent.getStartDate(),
                            currentEvent.getEndDate());
                    tasks.add(newEvent);
                    tasks.remove(indexNumber);
                } else if (currentEvent.getStartDate() == null) {
                    Event newEvent = new Event(newValue, isMarked, currentEvent.getStartDateTime(),
                            currentEvent.getEndDateTime());
                    tasks.add(newEvent);
                    tasks.remove(indexNumber);
                }
            } else if (Objects.equals(fieldType, "startDate")) {
                if (newValue.contains("T")) {
                    LocalDateTime eventDateTime = LocalDateTime.parse(newValue);
                    Event newEvent = new Event(currentEvent.getDescription(), isMarked, eventDateTime,
                            currentEvent.getEndDateTime());
                    tasks.add(newEvent);
                    tasks.remove(indexNumber);
                } else {
                    LocalDate eventDate = LocalDate.parse(newValue);
                    Event newEvent = new Event(currentEvent.getDescription(), isMarked, eventDate,
                            currentEvent.getEndDate());
                    tasks.add(newEvent);
                    tasks.remove(indexNumber);
                }
            } else if (Objects.equals(fieldType, "endDate")) {
                if (newValue.contains("T")) {
                    LocalDateTime eventDateTime = LocalDateTime.parse(newValue);
                    Event newEvent = new Event(currentEvent.getDescription(), isMarked, currentEvent.getStartDateTime(),
                            eventDateTime);
                    tasks.add(newEvent);
                    tasks.remove(indexNumber);
                } else {
                    LocalDate eventDate = LocalDate.parse(newValue);
                    Event newEvent = new Event(currentEvent.getDescription(), isMarked, currentEvent.getStartDate(),
                            eventDate);
                    tasks.add(newEvent);
                    tasks.remove(indexNumber);
                }
            }

        } catch (RuntimeException e) {
            addMessageToBuffer("Unable to parse update Event command! " + e);
            throw new RuntimeException(e);
        }

    }


}
