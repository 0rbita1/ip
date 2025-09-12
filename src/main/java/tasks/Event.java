package tasks;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    @Override
    public String toString() {
        String from;
        String to;
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
