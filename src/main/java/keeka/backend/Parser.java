package keeka.backend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import keeka.tasks.Task;

/**
 * Handles parsing of user input commands and saved task content.
 * Provides methods to extract and validate different types of input data
 * including task descriptions, dates, times, and save file content.
 */
public class Parser {
    private static final int TASK_CODE_INDEX = 1;
    private static final int MARKED_STATUS_INDEX = 4;
    private static final int SUBSTRING_START_INDEX = 7;

    /**
     * Parses a saved task content line to extract task code, completion status, and content.
     * Processes the standardized save format to recreate task information.
     *
     * @param saveContent The complete saved task line from storage file.
     * @return A ParsedSaveContent object containing extracted task information.
     */
    public ParsedSaveContent parseSaveContent(String saveContent) {
        String[] indexTaskPair = saveContent.split("\\. ", 2);
        char taskCode = indexTaskPair[1].charAt(TASK_CODE_INDEX);
        char markedStatus = indexTaskPair[1].charAt(MARKED_STATUS_INDEX);
        String taskContent = indexTaskPair[1].substring(SUBSTRING_START_INDEX);

        return new ParsedSaveContent(taskCode, markedStatus == 'X', taskContent);
    }

    /**
     * Parses deadline command input to extract description and due date information.
     * Supports both LocalDate and LocalDateTime formats for flexible date entry.
     *
     * @param input The deadline command string containing description and date.
     * @return A DeadlineInput object containing parsed description and date information.
     * @throws DateTimeParseException If the date format is invalid or unparseable.
     */
    public DeadlineInput parseDeadlineInput(String input) throws DateTimeParseException {
        String[] parts = input.split(" /by ");
        String description = parts[0];
        String dateString = parts[1];

        if (dateString.contains("T")) {
            return new DeadlineInput(description, LocalDateTime.parse(dateString));
        } else {
            return new DeadlineInput(description, LocalDate.parse(dateString));
        }
    }

    /**
     * Parses event command input to extract description, start time, and end time.
     * Supports both LocalDate and LocalDateTime formats for flexible date entry.
     *
     * @param input The event command string containing description and time range.
     * @return An EventInput object containing parsed description and time information.
     * @throws DateTimeParseException If any date format is invalid or unparseable.
     */
    public EventInput parseEventInput(String input) throws DateTimeParseException {
        String[] parts = input.split(" /from ");
        String description = parts[0];
        String[] dateParts = parts[1].split(" /to ");
        String startString = dateParts[0];
        String endString = dateParts[1];

        if (startString.contains("T") && endString.contains("T")) {
            return new EventInput(description, LocalDateTime.parse(startString), LocalDateTime.parse(endString));
        } else {
            return new EventInput(description, LocalDate.parse(startString), LocalDate.parse(endString));
        }
    }

    /**
     * Parses update command input to extract task index, field type, and new value.
     * Processes the three-part update command format for task modification.
     *
     * @param input The update command string containing index, field, and value.
     * @return An UpdateInput object containing parsed update information.
     */
    public UpdateInput parseUpdateInput(String input) {
        String[] parts = input.split(" ", 3);
        int taskIndex = Integer.parseInt(parts[0]) - 1;
        String fieldType = parts[1];
        String newValue = parts[2];
        return new UpdateInput(taskIndex, fieldType, newValue);
    }

    /**
     * Data transfer object containing parsed save content information.
     * Holds extracted task code, completion status, and content from saved data.
     */
    public static class ParsedSaveContent {
        private final char taskCode;
        private final boolean isDone;
        private final String taskContent;

        /**
         * Constructs a ParsedSaveContent with extracted save file information.
         *
         * @param taskCode The single character code identifying the task type.
         * @param isDone The completion status of the task.
         * @param taskContent The full content string of the task.
         */
        public ParsedSaveContent(char taskCode, boolean isDone, String taskContent) {
            this.taskCode = taskCode;
            this.isDone = isDone;
            this.taskContent = taskContent;
        }

        /**
         * Returns the task type code (T, D, or E).
         *
         * @return The character representing the task type.
         */
        public char getTaskCode() { return taskCode; }

        /**
         * Returns the completion status of the task.
         *
         * @return True if the task is completed, false otherwise.
         */
        public boolean isDone() { return isDone; }

        /**
         * Returns the complete content string of the task.
         *
         * @return The task content including description and additional details.
         */
        public String getTaskContent() { return taskContent; }
    }

    /**
     * Data transfer object containing parsed deadline input information.
     * Holds description and either LocalDate or LocalDateTime depending on input format.
     */
    public static class DeadlineInput {
        private final String description;
        private final LocalDateTime dateTime;
        private final LocalDate date;

        /**
         * Constructs a DeadlineInput with description and specific date-time.
         *
         * @param description The task description text.
         * @param dateTime The specific date and time when the task is due.
         */
        public DeadlineInput(String description, LocalDateTime dateTime) {
            this.description = description;
            this.dateTime = dateTime;
            this.date = null;
        }

        /**
         * Constructs a DeadlineInput with description and date only.
         *
         * @param description The task description text.
         * @param date The date when the task is due (without specific time).
         */
        public DeadlineInput(String description, LocalDate date) {
            this.description = description;
            this.date = date;
            this.dateTime = null;
        }

        /**
         * Returns the task description.
         *
         * @return The description text of the deadline task.
         */
        public String getDescription() { return description; }

        /**
         * Returns the specific date-time if provided.
         *
         * @return The LocalDateTime due date, or null if only date was provided.
         */
        public LocalDateTime getDateTime() { return dateTime; }

        /**
         * Returns the date if provided without specific time.
         *
         * @return The LocalDate due date, or null if specific date-time was provided.
         */
        public LocalDate getDate() { return date; }
    }

    /**
     * Data transfer object containing parsed event input information.
     * Holds description and either LocalDate or LocalDateTime pairs for start/end times.
     */
    public static class EventInput {
        private final String description;
        private final LocalDateTime startDateTime;
        private final LocalDateTime endDateTime;
        private final LocalDate startDate;
        private final LocalDate endDate;

        /**
         * Constructs an EventInput with description and specific start/end date-times.
         *
         * @param description The event description text.
         * @param start The specific date and time when the event starts.
         * @param end The specific date and time when the event ends.
         */
        public EventInput(String description, LocalDateTime start, LocalDateTime end) {
            this.description = description;
            this.startDateTime = start;
            this.endDateTime = end;
            this.startDate = null;
            this.endDate = null;
        }

        /**
         * Constructs an EventInput with description and start/end dates only.
         *
         * @param description The event description text.
         * @param start The date when the event starts (without specific time).
         * @param end The date when the event ends (without specific time).
         */
        public EventInput(String description, LocalDate start, LocalDate end) {
            this.description = description;
            this.startDate = start;
            this.endDate = end;
            this.startDateTime = null;
            this.endDateTime = null;
        }

        /**
         * Returns the event description.
         *
         * @return The description text of the event.
         */
        public String getDescription() { return description; }

        /**
         * Returns the specific start date-time if provided.
         *
         * @return The LocalDateTime start time, or null if only date was provided.
         */
        public LocalDateTime getStartDateTime() { return startDateTime; }

        /**
         * Returns the specific end date-time if provided.
         *
         * @return The LocalDateTime end time, or null if only date was provided.
         */
        public LocalDateTime getEndDateTime() { return endDateTime; }

        /**
         * Returns the start date if provided without specific time.
         *
         * @return The LocalDate start date, or null if specific date-time was provided.
         */
        public LocalDate getStartDate() { return startDate; }

        /**
         * Returns the end date if provided without specific time.
         *
         * @return The LocalDate end date, or null if specific date-time was provided.
         */
        public LocalDate getEndDate() { return endDate; }
    }

    /**
     * Data transfer object containing parsed update command information.
     * Holds the task index, field type to update, and the new value.
     */
    public static class UpdateInput {
        private final int taskIndex;
        private final String fieldType;
        private final String newValue;

        /**
         * Constructs an UpdateInput with task index, field type, and new value.
         *
         * @param taskIndex The zero-based index of the task to update.
         * @param fieldType The type of field to update (e.g., "description", "date").
         * @param newValue The new value to assign to the specified field.
         */
        public UpdateInput(int taskIndex, String fieldType, String newValue) {
            this.taskIndex = taskIndex;
            this.fieldType = fieldType;
            this.newValue = newValue;
        }

        /**
         * Returns the zero-based index of the task to update.
         *
         * @return The task index for the update operation.
         */
        public int getTaskIndex() { return taskIndex; }

        /**
         * Returns the field type to be updated.
         *
         * @return The field type string identifier.
         */
        public String getFieldType() { return fieldType; }

        /**
         * Returns the new value for the update operation.
         *
         * @return The new value to be assigned to the field.
         */
        public String getNewValue() { return newValue; }
    }
}
