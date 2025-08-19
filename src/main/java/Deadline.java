public class Deadline extends Task {
    String date;

    public Deadline(String description, boolean isDone, String date) {
        super(description, isDone);
        this.date = date;
    }

    @Override
    public String toString() {
        return "[D] " +  super.toString() + " (by: " + date + ")";
    }

}
