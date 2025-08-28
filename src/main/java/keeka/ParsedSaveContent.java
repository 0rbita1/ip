package keeka;

public class ParsedSaveContent {
    private char taskCode;
    private boolean isDone;
    private String taskContent;

    public ParsedSaveContent(char taskCode, char markedStatus, String taskContent) {
        this.taskCode = taskCode;
        this.isDone = markedStatus == 'X';
        this.taskContent = taskContent;
    }

    public char getTaskCode() {
        return this.taskCode;
    }

    public boolean getMarkedStatus() {
        return this.isDone;
    }

    public String getTaskContent() {
        return this.taskContent;
    }
}
