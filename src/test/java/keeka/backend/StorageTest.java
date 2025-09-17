package keeka.backend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import keeka.tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    
    private Storage storage;
    private String testFilePath;
    
    @BeforeEach
    public void setUp() {
        testFilePath = "test_storage.txt";
        storage = new Storage(testFilePath);
    }
    
    @AfterEach
    public void tearDown() {
        File testFile = new File(testFilePath);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    
    @Test
    public void testSaveTask() {
        Task task = TaskFactory.createToDo("test task", false);
        assertDoesNotThrow(() -> storage.saveTask(task, 1));
    }
    
    @Test
    public void testUpdateAllTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(TaskFactory.createToDo("task 1", false));
        tasks.add(TaskFactory.createToDo("task 2", true));
        
        assertDoesNotThrow(() -> storage.updateAllTasks(tasks));
    }
    
    @Test
    public void testLoadSaveContents() {
        Task task = TaskFactory.createToDo("test task", false);
        
        assertDoesNotThrow(() -> {
            storage.saveTask(task, 1);
            List<String> contents = storage.loadSaveContents();
            assertFalse(contents.isEmpty());
            assertTrue(contents.get(0).contains("test task"));
        });
    }
    
    @Test
    public void testLoadSaveContents_EmptyFile() {
        assertDoesNotThrow(() -> {
            List<String> contents = storage.loadSaveContents();
            assertTrue(contents.isEmpty());
        });
    }
}
