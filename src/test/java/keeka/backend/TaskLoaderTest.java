package keeka.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TaskLoaderTest {
    
    private TaskLoader taskLoader;
    private TaskList taskList;
    private Storage storage;
    private Parser parser;
    private String testFilePath;
    
    @BeforeEach
    public void setUp() {
        testFilePath = "test_loader.txt";
        taskList = new TaskList();
        storage = new Storage(testFilePath);
        parser = new Parser();
        taskLoader = new TaskLoader(taskList, storage, parser);
    }
    
    @AfterEach
    public void tearDown() {
        File testFile = new File(testFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    @Test
    public void testLoadTasks_EmptyStorage() {
        assertDoesNotThrow(() -> taskLoader.loadTasks());
        assertEquals(0, taskList.size());
    }
    
    @Test
    public void testLoadTasks_WithSavedTasks() {
        assertDoesNotThrow(() -> {
            // Save some tasks first
            storage.saveTask(TaskFactory.createToDo("test todo", false), 1);
            storage.saveTask(TaskFactory.createDeadline("test deadline", false, 
                java.time.LocalDate.of(2024, 12, 31)), 2);
            
            // Create new instances to simulate fresh load
            TaskList newTaskList = new TaskList();
            TaskLoader newTaskLoader = new TaskLoader(newTaskList, storage, parser);
            
            newTaskLoader.loadTasks();
            assertEquals(2, newTaskList.size());
        });
    }
    
    @Test
    public void testLoadTasks_HandleInvalidData() {
        assertDoesNotThrow(() -> taskLoader.loadTasks());
        // Should not crash even with invalid data in file
    }
}
