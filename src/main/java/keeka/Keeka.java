package keeka;

import tasks.Task;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Keeka {

    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        Storage.loadSaveContents(tasks);

        Ui.printGreeting();

        Interpreter.interpretInputs(tasks);

        Ui.printBye();

    }
}