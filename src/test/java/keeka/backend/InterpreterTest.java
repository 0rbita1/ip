package keeka.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {
    
    private Interpreter interpreter;
    private TaskList taskList;
    private Storage storage;
    private Parser parser;
    private Ui ui;
    private CommandHandler commandHandler;
    private TaskLoader taskLoader;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        storage = new Storage("test_list.txt");
        parser = new Parser();
        ui = new Ui();
        commandHandler = new CommandHandler(taskList, storage, parser, ui);
        taskLoader = new TaskLoader(taskList, storage, parser);
        interpreter = new Interpreter(commandHandler, taskLoader, ui);
        
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testStart() {
        assertDoesNotThrow(() -> interpreter.start());
        String output = outputStream.toString();
        assertTrue(output.contains("Hello! I'm Keeka"));
    }
    
    @Test
    public void testProcessCommand_TodoCommand() {
        assertDoesNotThrow(() -> interpreter.processCommand("todo read book"));
        assertEquals(1, taskList.size());
    }
    
    @Test
    public void testProcessCommand_ListCommand() {
        taskList.addTask(TaskFactory.createToDo("test task", false));
        assertDoesNotThrow(() -> interpreter.processCommand("list"));
        String output = outputStream.toString();
        assertTrue(output.contains("Displaying list items"));
    }
    
    @Test
    public void testProcessCommand_ByeCommand() {
        assertDoesNotThrow(() -> interpreter.processCommand("bye"));
        String output = outputStream.toString();
        assertTrue(output.contains("Bye. Hope to see you again soon!"));
    }
    
    @Test
    public void testProcessCommand_UnknownCommand() {
        assertDoesNotThrow(() -> interpreter.processCommand("unknown"));
        String output = outputStream.toString();
        assertTrue(output.contains("Unknown command"));
    }
}
