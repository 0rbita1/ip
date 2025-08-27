import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Deadline extends Task {
    LocalDate date;
    LocalDateTime dateTime;

    public Deadline(String description, boolean isDone, LocalDate date) {
        super(description, isDone);
        this.date = date;
    }

    public Deadline(String description, boolean isDone, LocalDateTime dateTime) {
        super(description, isDone);
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        String by;
        if (date != null) {
            by = date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
            //by = date.toString();
        } else {

            by = dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
        return "[D] " + super.toString() + " (by: " + by + ")";
    }

    public String printInISO() {
        String by;
        if (date != null) {
            by = date.toString();
        } else {
            by = dateTime.toString();
        }
        return "[D] " + super.toString() + " (by: " + by + ")";
    }

}
