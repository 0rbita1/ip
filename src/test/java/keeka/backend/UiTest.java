package keeka.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import keeka.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UiTest {
    
    private Ui ui;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    public void setUp() {
        ui = new Ui();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testShowGreeting() {
        ui.showGreeting();
        String output = outputStream.toString();
        assertTrue(output.contains("Hello! I'm Keeka"));
        assertTrue(output.contains("What can I do for you?"));
    }
    
    @Test
    public void testShowGoodbye() {
        ui.showGoodbye();
        String output = outputStream.toString();
        assertTrue(output.contains("Bye. Hope to see you again soon!"));
    }
    
    @Test
    public void testShowTaskAdded() {
        Task task = TaskFactory.createToDo("test task", false);
        ui.showTaskAdded(task, 1);
        String output = outputStream.toString();
        assertTrue(output.contains("Task successfully added"));
        assertTrue(output.contains("Task counter: 1"));
    }
    
    @Test
    public void testShowTaskMarked() {
        Task task = TaskFactory.createToDo("test task", false);
        task.markAsDone();
        ui.showTaskMarked(task);
        String output = outputStream.toString();
        assertTrue(output.contains("Task successfully marked as done"));
    }
    
    @Test
    public void testShowTaskUnmarked() {
        Task task = TaskFactory.createToDo("test task", true);
        task.markAsNotDone();
        ui.showTaskUnmarked(task);
        String output = outputStream.toString();
        assertTrue(output.contains("Task successfully marked as NOT done"));
    }
    
    @Test
    public void testShowTaskDeleted() {
        Task task = TaskFactory.createToDo("test task", false);
        ui.showTaskDeleted(task, 0);
        String output = outputStream.toString();
        assertTrue(output.contains("Task successfully deleted"));
        assertTrue(output.contains("Task counter: 0"));
    }
    
    @Test
    public void testShowTaskList_EmptyList() {
        List<Task> tasks = new ArrayList<>();
        ui.showTaskList(tasks);
        String output = outputStream.toString();
        assertTrue(output.contains("List is empty! Add tasks to display"));
    }
    
    @Test
    public void testShowTaskList_WithTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(TaskFactory.createToDo("task 1", false));
        tasks.add(TaskFactory.createToDo("task 2", true));
        
        ui.showTaskList(tasks);
        String output = outputStream.toString();
        assertTrue(output.contains("Displaying list items"));
        assertTrue(output.contains("1. [T][ ] task 1"));
        assertTrue(output.contains("2. [T][X] task 2"));
    }
    
    @Test
    public void testShowFoundTasks_NoResults() {
        List<Task> tasks = new ArrayList<>();
        ui.showFoundTasks(tasks);
        String output = outputStream.toString();
        assertTrue(output.contains("Unable to find any matches for your query!"));
    }
    
    @Test
    public void testShowFoundTasks_WithResults() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(TaskFactory.createToDo("found task", false));
        
        ui.showFoundTasks(tasks);
        String output = outputStream.toString();
        assertTrue(output.contains("Displaying items that match your query"));
        assertTrue(output.contains("found task"));
    }
    
    @Test
    public void testShowError() {
        ui.showError("Test error message");
        String output = outputStream.toString();
        assertTrue(output.contains("Error: Test error message"));
    }
    
    @Test
    public void testGetLatestMessage() {
        ui.showGreeting();
        String latestMessage = ui.getLatestMessage();
        assertTrue(latestMessage.contains("Hello! I'm Keeka"));
    }
}
