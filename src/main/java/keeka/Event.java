package keeka;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    LocalDate startDate;
    LocalDate endDate;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;



    public Event(String description, boolean isDone, LocalDate startDate, LocalDate endDate) {
        super(description, isDone);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Event(String description, boolean isDone, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(description, isDone);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        String from, to;
        if (startDate != null && endDate != null) {
            from = startDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
            to = endDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        } else {
            from = startDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
            to = endDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
        return "[E] " + super.toString() + " (from: " + from + " to: " + to + ")";
    }


    /**
     * Returns a string representation of the Event task in ISO format.
     * This method constructs a string containing the object's superclass string representation,
     *
     * @return A string in the format "[E] [superclass string] (from: [date/datetime string] to:
     * [date/datetime string])".
     */
    public String printISO() {
        String from, to;
        if (startDate != null && endDate != null) {
            from = startDate.toString();
            to = endDate.toString();
        } else {
            from = startDateTime.toString();
            to = endDateTime.toString();
        }
        return "[E] " + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
