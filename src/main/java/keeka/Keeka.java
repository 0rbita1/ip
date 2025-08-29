package keeka;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Keeka {

    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            ArrayList<String> saveContents = Storage.retrieveSaveContents();

            for (int i = 0; i < saveContents.size(); i++) {
                String saveContent = saveContents.get(i);
                ParsedSaveContent parsedSaveContent = Parser.parseSaveContent(saveContent);
                char taskCode = parsedSaveContent.getTaskCode();
                boolean isDone = parsedSaveContent.getMarkedStatus();
                String taskContent = parsedSaveContent.getTaskContent();

                switch (taskCode) {
                case 'T' -> {
                    Storage.loadToDo(taskContent, isDone, tasks);
                }
                case 'D' -> {
                    Interpreter.interpretAndLoadDeadlineSave(taskContent, isDone, tasks);
                }
                case 'E' -> {
                    Interpreter.interpretAndLoadEventSave(taskContent, isDone, tasks);
                }
                }
            }

            Ui.printGreeting();

            Interpreter.interpretInputs(tasks);

            Ui.printBye();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}