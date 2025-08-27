import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class Keeka {
    final static String NAME = "Keeka";
    final static String SAVE_FILE_PATH = "src/main/java/TaskList.txt";

    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            retrieveSaveContents(tasks);
            printMessage("Hello! I'm " + NAME + "\n" + "What can I do for you?");
            handleInputs(tasks);
            printMessage("Bye. Hope to see you again soon!");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

    }

    private static void retrieveSaveContents(ArrayList<Task> tasks) throws FileNotFoundException {
        File f = new File(SAVE_FILE_PATH);
        Scanner s = new Scanner(f);

        while (s.hasNext()) {
            String currentLine = s.nextLine();
            parseSaveContent(currentLine, tasks);
        }

    }

    private static void parseSaveContent(String content, ArrayList<Task> tasks) {
        int taskCodeIndex = 1;
        int markedStatusIndex = 5;
        int substringStartIndex = 8;

        String[] indexTaskPair = content.split(". ", 2);
        char taskCode = indexTaskPair[1].charAt(taskCodeIndex);
        char markedStatus = indexTaskPair[1].charAt(markedStatusIndex);
        String taskContent = indexTaskPair[1].substring(substringStartIndex);
        interpretSaveContent(taskCode, markedStatus, taskContent, tasks);
    }

    private static void interpretSaveContent(char taskCode, char markedStatus, String taskContent, ArrayList<Task>
            tasks) {
        boolean isMarked = markedStatus == 'X';


        switch (taskCode) {
        case 'T' -> {
            loadToDo(taskContent, isMarked, tasks);
        }
        case 'D' -> {
            interpretDeadlineSave(taskContent, tasks);
        }
        case 'E' -> {
            interpretEventSave(taskContent, tasks);
        }
        }

    }

    private static void interpretDeadlineSave(String taskContent, ArrayList<Task> tasks) {
        String[] descriptionDateSplit = taskContent.split(" \\(by: ", 2);
        String description = descriptionDateSplit[0];
        String dateString = descriptionDateSplit[1].substring(0, descriptionDateSplit[1].length() - 1);

        try {
            if (dateString.contains("T")) {
                LocalDateTime deadlineDateTime = LocalDateTime.parse(dateString);
                loadDeadline(dateString, false, deadlineDateTime, tasks);
            } else {
                LocalDate deadlineDate = LocalDate.parse(dateString);
                loadDeadline(dateString, false, deadlineDate, tasks);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format for deadline input! " + e);
            System.out.println("Deadline date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }
    }

    private static void interpretEventSave(String taskContent, ArrayList<Task> tasks) {
        String[] descriptionDurationSplit = taskContent.split(" \\(from: ", 2);
        String description = descriptionDurationSplit[0];
        String[] durationSplit = descriptionDurationSplit[1].split(" to: ", 2);
        String eventStartString = durationSplit[0];
        String eventEndString = durationSplit[1].substring(0, durationSplit[1].length() - 1);

        try {
            if (eventStartString.contains("T") && eventEndString.contains("T")) {
                LocalDateTime eventStart = LocalDateTime.parse(eventStartString);
                LocalDateTime eventEnd = LocalDateTime.parse(eventEndString);
                loadEvent(description, false, eventStart, eventEnd, tasks);
            } else if (!eventStartString.contains("T") && !eventEndString.contains("T")) {
                LocalDate eventStart = LocalDate.parse(eventStartString);
                LocalDate eventEnd = LocalDate.parse(eventEndString);
                loadEvent(description, false, eventStart, eventEnd, tasks);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format for event input! " + e);
            System.out.println("Event date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }

    }

    private static void handleInputs(ArrayList<Task> tasks) {
        Scanner s = new Scanner(System.in);
        String input = "";
        input = s.nextLine();

        while (!Objects.equals(input, "bye")) {
            if (Objects.equals(input, "list")) {
                printList(tasks);
                input = s.nextLine();
                continue;
            }

            try {
                String[] parsedInput = input.split(" ", 2);
                String firstWord = parsedInput[0];

                if (Objects.equals(firstWord, "todo") || (Objects.equals(firstWord, "deadline")) ||
                        (Objects.equals(firstWord, "event"))) {
                    interpretTask(input, tasks);
                } else if (Objects.equals(firstWord, "mark") || (Objects.equals(firstWord, "unmark"))) {
                    interpretMark(input, tasks);
                } else if (Objects.equals(firstWord, "delete")) {
                    interpretDelete(input, tasks);
                } else {
                    throw new Exception("Invalid command!");
                }

            } catch (Exception e) {
                printMessage(e.getMessage());
            } finally {
                input = s.nextLine();
            }

        }
    }

    private static void interpretTask(String userInput, ArrayList<Task> tasks) throws InvalidTaskException {
        try {
            String[] parsedInput = userInput.split(" ", 2);
            String taskType = parsedInput[0];
            String remainingContent = parsedInput[1];

            switch (taskType) {
            case "todo" -> {
                addToDo(remainingContent, false, tasks);
                printSuccessfulTaskAddition(tasks);
            }
            case "deadline" -> {
                parseDeadlineInput(remainingContent, tasks);
                printSuccessfulTaskAddition(tasks);
            }
            case "event" -> {
                parseEventInput(remainingContent, tasks);
                printSuccessfulTaskAddition(tasks);
            }
            }

        } catch (Exception e) {
            throw new InvalidTaskException("Invalid task invocation!");
        }
    }

    private static void parseDeadlineInput(String remainingContent, ArrayList<Task> tasks) throws
            DateTimeParseException {
        String[] parsedDescription = remainingContent.split(" /by ");
        String deadlineDescription = parsedDescription[0];
        String deadlineDateString = parsedDescription[1];

        try {
            if (deadlineDateString.contains("T")) {
                LocalDateTime deadlineDateTime = LocalDateTime.parse(deadlineDateString);
                addDeadline(deadlineDescription, false, deadlineDateTime, tasks);
            } else {
                LocalDate deadlineDate = LocalDate.parse(deadlineDateString);
                addDeadline(deadlineDescription, false, deadlineDate, tasks);
            }
        } catch (DateTimeParseException | IOException e) {
            System.out.println("Invalid date format for deadline input! " + e);
            System.out.println("Deadline date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }

    }

    private static void parseEventInput(String remainingContent, ArrayList<Task> tasks) throws DateTimeParseException {
        String[] parsedDescription = remainingContent.split(" /from ");
        String eventDescription = parsedDescription[0];
        String[] parsedDuration = parsedDescription[1].split(" /to ");
        String eventStartString = parsedDuration[0];
        String eventEndString = parsedDuration[1];

        try {
            if (eventStartString.contains("T") && eventEndString.contains("T")) {
                LocalDateTime eventStart = LocalDateTime.parse(eventStartString);
                LocalDateTime eventEnd = LocalDateTime.parse(eventEndString);
                addEvent(eventDescription, false, eventStart, eventEnd, tasks);
            } else if (!eventStartString.contains("T") && !eventEndString.contains("T")) {
                LocalDate eventStart = LocalDate.parse(eventStartString);
                LocalDate eventEnd = LocalDate.parse(eventEndString);
                addEvent(eventDescription, false, eventStart, eventEnd, tasks);
            }
        } catch (DateTimeParseException | IOException e) {
            System.out.println("Invalid date format for event input! " + e);
            System.out.println("Event date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS");
        }
    }

    private static void addToDo(String description, Boolean isDone, ArrayList<Task> tasks)
            throws IOException {
        ToDo newToDo = new ToDo(description, isDone);
        tasks.add(newToDo);
        addToDoToSave(newToDo, tasks);
    }

    private static void addDeadline(String description, Boolean isDone, LocalDateTime date, ArrayList<Task> tasks)
            throws IOException {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
        addDeadlineToSave(newDeadline, tasks);
    }

    private static void addDeadline(String description, Boolean isDone, LocalDate date, ArrayList<Task> tasks)
            throws IOException {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
        addDeadlineToSave(newDeadline, tasks);
    }

    private static void addEvent(String description, Boolean isDone, LocalDateTime start, LocalDateTime end,
                                 ArrayList<Task> tasks) throws IOException {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
        addEventToSave(newEvent, tasks);
    }

    private static void addEvent(String description, Boolean isDone, LocalDate start, LocalDate end,
                                 ArrayList<Task> tasks) throws IOException {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
        addEventToSave(newEvent, tasks);
    }

    private static void loadToDo(String description, Boolean isDone, ArrayList<Task> tasks) {
        ToDo newToDo = new ToDo(description, isDone);
        tasks.add(newToDo);
    }

    private static void loadDeadline(String description, Boolean isDone, LocalDateTime date, ArrayList<Task> tasks)  {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
    }

    private static void loadDeadline(String description, Boolean isDone, LocalDate date, ArrayList<Task> tasks)  {
        Deadline newDeadline = new Deadline(description, isDone, date);
        tasks.add(newDeadline);
    }

    private static void loadEvent(String description, Boolean isDone, LocalDateTime start, LocalDateTime end,
                                  ArrayList<Task> tasks) {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
    }

    private static void loadEvent(String description, Boolean isDone, LocalDate start, LocalDate end,
                                  ArrayList<Task> tasks) {
        Event newEvent = new Event(description, isDone, start, end);
        tasks.add(newEvent);
    }

    private static void addToDoToSave(ToDo task, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + task.toString() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    private static void addDeadlineToSave(Deadline deadline, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + deadline.printInISO() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    private static void addEventToSave(Event event, ArrayList<Task> tasks ) throws IOException {
        String saveText = tasks.size() + ". " + event.printISO() + "\n";
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, true);
        fw.write(saveText);
        fw.close();
    }

    private static void updateTaskInSave(ArrayList<Task> tasks) throws IOException {
        FileWriter fw = new FileWriter(SAVE_FILE_PATH, false);

        for (int i = 0; i < tasks.size(); i++) {
            String saveText = (i + 1) + ". " + tasks.get(i).toString() + "\n";
            fw.write(saveText);
        }

        fw.close();
    }

    private static void interpretMark(String input, ArrayList<Task> tasks) throws InvalidMarkingException {
        try {
            String[] markingArray = input.split(" ");
            String isMarkString = markingArray[0];
            int indexNumber = Integer.parseInt(markingArray[1]);

            switch (isMarkString) {
            case "mark" -> markTask(tasks, indexNumber);
            case "unmark" -> unmarkTask(tasks, indexNumber);
            }

        } catch (Exception e) {
            throw new InvalidMarkingException("Invalid marking invocation!");
        }
    }

    private static void markTask(ArrayList<Task> tasks, int index) throws IOException {
        if (index < 1 || index > tasks.size()) {
            printMessage("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        Task desiredTask = tasks.get(index - 1);
        desiredTask.setDone();
        tasks.set(index - 1, desiredTask);
        printMessage("Task successfully marked as done:\n" + desiredTask);
        updateTaskInSave(tasks);
    }

    private static void unmarkTask(ArrayList<Task> tasks, int index) throws IOException {
        if (index < 1 || index > tasks.size()) {
            printMessage("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        Task desiredTask = tasks.get(index - 1);
        desiredTask.setNotDone();
        tasks.set(index - 1, desiredTask);
        printMessage("Task successfully marked as NOT done:\n" + desiredTask);
        updateTaskInSave(tasks);
    }

    private static void interpretDelete(String input, ArrayList<Task> tasks) throws Exception {
        try {
            String[] deletionArray = input.split(" ");
            String deleteString = deletionArray[0];
            int indexNumber = Integer.parseInt(deletionArray[1]);
            deleteTask(tasks, indexNumber);

        } catch (Exception e) {
            throw new Exception("Invalid deletion invocation!");
        }
    }

    private static void deleteTask(ArrayList<Task> tasks, int index) throws IOException {
        if (index < 1 || index > tasks.size()) {
            printMessage("Invalid index number! Use an integer within the range of the size of the list");
            return;
        }

        Task desiredTask = tasks.get(index - 1);
        tasks.remove(index - 1);
        printMessage("Task successfully deleted:\n" + desiredTask + "\n" + "Task counter: " + tasks.size());
        updateTaskInSave(tasks);
    }

    private static void printSuccessfulTaskAddition(ArrayList<Task> taskList) {
        printMessage("Task successfully added:\n" + taskList.get(taskList.size() - 1) + "\n" + "Task counter: " + taskList.size());
    }

    private static void printList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            printMessage("List is empty! Add tasks to display");
            return;
        }

        String listText = "Displaying list items\n";

        for (int i = 0; i < tasks.size(); i++) {
            Task currentTask = tasks.get(i);
            listText = listText.concat((i + 1) + ". " + currentTask.toString() + "\n");
        }

        printMessage(listText);
    }

    private static void printMessage(String message) {
        System.out.println("_________________________________________________");
        System.out.println(message);
        System.out.println("_________________________________________________");
    }

}