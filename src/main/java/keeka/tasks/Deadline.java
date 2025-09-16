package keeka.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Deadline extends Task {
    private LocalDateTime dateTime;
    private LocalDate date;

    public Deadline(String description, boolean isDone, LocalDateTime dateTime) {
        super(description, isDone);
        this.dateTime = dateTime;
    }

    public Deadline(String description, boolean isDone, LocalDate date) {
        super(description, isDone);
        this.date = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getTaskCode() {
        return "D";
    }

    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        String dateStr = dateTime != null ? dateTime.toString() : date.toString();
        return String.format("[D][%s] %s (by: %s)", status, description, dateStr);
    }
}
