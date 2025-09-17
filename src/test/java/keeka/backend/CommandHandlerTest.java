package keeka.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class CommandHandlerTest {
    
    private CommandHandler commandHandler;
    private TaskList taskList;
    private Storage storage;
    private Parser parser;
    private Ui ui;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        storage = new Storage("test_list.txt");
        parser = new Parser();
        ui = new Ui();
        commandHandler = new CommandHandler(taskList, storage, parser, ui);
        
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testHandleTodoCommand_ValidInput() {
        commandHandler.handleTodoCommand("read book");
        assertEquals(1, taskList.size());
        assertEquals("read book", taskList.getTask(0).getDescription());
    }
    
    @Test
    public void testHandleTodoCommand_EmptyInput() {
        commandHandler.handleTodoCommand("");
        assertEquals(0, taskList.size());
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid task invocation!"));
    }
    
    @Test
    public void testHandleDeadlineCommand_ValidInput() {
        commandHandler.handleDeadlineCommand("submit assignment /by 2024-12-31");
        assertEquals(1, taskList.size());
        assertEquals("submit assignment", taskList.getTask(0).getDescription());
    }
    
    @Test
    public void testHandleDeadlineCommand_InvalidInput() {
        commandHandler.handleDeadlineCommand("submit assignment");
        assertEquals(0, taskList.size());
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid task invocation!"));
    }
    
    @Test
    public void testHandleEventCommand_ValidInput() {
        commandHandler.handleEventCommand("meeting /from 2024-12-31 /to 2025-01-01");
        assertEquals(1, taskList.size());
        assertEquals("meeting", taskList.getTask(0).getDescription());
    }
    
    @Test
    public void testHandleEventCommand_InvalidInput() {
        commandHandler.handleEventCommand("meeting /from 2024-12-31");
        assertEquals(0, taskList.size());
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid task invocation!"));
    }
    
    @Test
    public void testHandleMarkCommand() {
        taskList.addTask(TaskFactory.createToDo("test task", false));
        commandHandler.handleMarkCommand("mark 1");
        assertTrue(taskList.getTask(0).isDone());
    }
    
    @Test
    public void testHandleUnmarkCommand() {
        taskList.addTask(TaskFactory.createToDo("test task", true));
        commandHandler.handleUnmarkCommand("unmark 1");
        assertFalse(taskList.getTask(0).isDone());
    }
    
    @Test
    public void testHandleDeleteCommand() {
        taskList.addTask(TaskFactory.createToDo("test task", false));
        commandHandler.handleDeleteCommand("delete 1");
        assertEquals(0, taskList.size());
    }
    
    @Test
    public void testHandleFindCommand() {
        taskList.addTask(TaskFactory.createToDo("read book", false));
        taskList.addTask(TaskFactory.createToDo("write code", false));
        assertDoesNotThrow(() -> commandHandler.handleFindCommand("find book"));
        String output = outputStream.toString();
        assertTrue(output.contains("read book"));
    }
    
    @Test
    public void testHandleListCommand() {
        taskList.addTask(TaskFactory.createToDo("test task", false));
        commandHandler.handleListCommand();
        String output = outputStream.toString();
        assertTrue(output.contains("Displaying list items"));
    }
}
