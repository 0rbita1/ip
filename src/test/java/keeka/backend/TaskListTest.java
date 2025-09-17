package keeka.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import keeka.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {
    
    private TaskList taskList;
    private Task testTask;
    
    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        testTask = TaskFactory.createToDo("test task", false);
    }
    
    @Test
    public void testAddTask() {
        taskList.addTask(testTask);
        assertEquals(1, taskList.size());
        assertEquals(testTask, taskList.getTask(0));
    }
    
    @Test
    public void testRemoveTask_ValidIndex() {
        taskList.addTask(testTask);
        taskList.removeTask(0);
        assertEquals(0, taskList.size());
    }
    
    @Test
    public void testRemoveTask_InvalidIndex() {
        taskList.addTask(testTask);
        taskList.removeTask(5);
        assertEquals(1, taskList.size());
    }
    
    @Test
    public void testGetTask_ValidIndex() {
        taskList.addTask(testTask);
        assertEquals(testTask, taskList.getTask(0));
    }
    
    @Test
    public void testGetTask_InvalidIndex() {
        taskList.addTask(testTask);
        assertNull(taskList.getTask(5));
    }
    
    @Test
    public void testReplaceTask() {
        Task newTask = TaskFactory.createToDo("new task", false);
        taskList.addTask(testTask);
        taskList.replaceTask(0, newTask);
        assertEquals(newTask, taskList.getTask(0));
    }
    
    @Test
    public void testFindTasks() {
        Task task1 = TaskFactory.createToDo("read book", false);
        Task task2 = TaskFactory.createToDo("write code", false);
        Task task3 = TaskFactory.createToDo("read newspaper", false);
        
        taskList.addTask(task1);
        taskList.addTask(task2);
        taskList.addTask(task3);
        
        List<Task> foundTasks = taskList.findTasks("read");
        assertEquals(2, foundTasks.size());
        assertTrue(foundTasks.contains(task1));
        assertTrue(foundTasks.contains(task3));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, taskList.size());
        taskList.addTask(testTask);
        assertEquals(1, taskList.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(taskList.isEmpty());
        taskList.addTask(testTask);
        assertFalse(taskList.isEmpty());
    }
    
    @Test
    public void testGetAllTasks() {
        taskList.addTask(testTask);
        List<Task> allTasks = taskList.getAllTasks();
        assertEquals(1, allTasks.size());
        assertEquals(testTask, allTasks.get(0));
    }
}
