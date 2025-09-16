package keeka.backend;

import java.time.LocalDate;
import java.time.LocalDateTime;

import keeka.tasks.Deadline;
import keeka.tasks.Event;
import keeka.tasks.Task;
import keeka.tasks.ToDo;

/**
 * Factory class responsible for creating different types of task objects.
 * Provides static methods to instantiate ToDo, Deadline, and Event tasks
 * with appropriate parameters while ensuring consistent object creation.
 */
public class TaskFactory {
    
    /**
     * Creates a new ToDo task with the specified description and completion status.
     *
     * @param description The description text for the todo task.
     * @param isDone The completion status of the task (true if completed, false otherwise).
     * @return A new ToDo task instance with the provided parameters.
     */
    public static ToDo createToDo(String description, boolean isDone) {
        return new ToDo(description, isDone);
    }

    /**
     * Creates a new Deadline task with a specific date and time constraint.
     *
     * @param description The description text for the deadline task.
     * @param isDone The completion status of the task (true if completed, false otherwise).
     * @param dateTime The specific date and time when the task is due.
     * @return A new Deadline task instance with LocalDateTime constraint.
     */
    public static Deadline createDeadline(String description, boolean isDone, LocalDateTime dateTime) {
        return new Deadline(description, isDone, dateTime);
    }

    /**
     * Creates a new Deadline task with a date constraint (no specific time).
     *
     * @param description The description text for the deadline task.
     * @param isDone The completion status of the task (true if completed, false otherwise).
     * @param date The date when the task is due (without specific time).
     * @return A new Deadline task instance with LocalDate constraint.
     */
    public static Deadline createDeadline(String description, boolean isDone, LocalDate date) {
        return new Deadline(description, isDone, date);
    }

    /**
     * Creates a new Event task with specific start and end date-time constraints.
     *
     * @param description The description text for the event task.
     * @param isDone The completion status of the task (true if completed, false otherwise).
     * @param start The specific date and time when the event starts.
     * @param end The specific date and time when the event ends.
     * @return A new Event task instance with LocalDateTime constraints.
     */
    public static Event createEvent(String description, boolean isDone, LocalDateTime start, LocalDateTime end) {
        return new Event(description, isDone, start, end);
    }

    /**
     * Creates a new Event task with start and end date constraints (no specific times).
     *
     * @param description The description text for the event task.
     * @param isDone The completion status of the task (true if completed, false otherwise).
     * @param start The date when the event starts (without specific time).
     * @param end The date when the event ends (without specific time).
     * @return A new Event task instance with LocalDate constraints.
     */
    public static Event createEvent(String description, boolean isDone, LocalDate start, LocalDate end) {
        return new Event(description, isDone, start, end);
    }
}
