package keeka;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;



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
        } else {
            by = dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
        return "[D] " + super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns a string representation of the Deadline task in ISO format.
     * This method constructs a string containing the object's superclass string representation,
     *
     * @return A string in the format "[D] [superclass string] (by: [date/datetime string])".
     */
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
