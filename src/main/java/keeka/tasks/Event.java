package keeka.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event extends Task {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDate startDate;
    private LocalDate endDate;

    public Event(String description, boolean isDone, LocalDateTime start, LocalDateTime end) {
        super(description, isDone);
        this.startDateTime = start;
        this.endDateTime = end;
    }

    public Event(String description, boolean isDone, LocalDate start, LocalDate end) {
        super(description, isDone);
        this.startDate = start;
        this.endDate = end;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String getTaskCode() {
        return "E";
    }

    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        if (startDateTime != null) {
            return String.format("[E][%s] %s (from: %s to: %s)", 
                status, description, startDateTime, endDateTime);
        } else {
            return String.format("[E][%s] %s (from: %s to: %s)", 
                status, description, startDate, endDate);
        }
    }
}
