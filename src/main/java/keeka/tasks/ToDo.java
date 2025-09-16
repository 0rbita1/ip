package keeka.tasks;

public class ToDo extends Task {
    public ToDo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String getTaskCode() {
        return "T";
    }

    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        return String.format("[T][%s] %s", status, description);
    }
}
