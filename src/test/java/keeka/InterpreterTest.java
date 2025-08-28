package keeka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterpreterTest {

    private ArrayList<Task> tasks;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        tasks = new ArrayList<>();
        // Capture System.out for testing print statements
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    public void testInterpretAndLoadDeadlineSave_WithLocalDate() {
        ArrayList<Task> tasks = new ArrayList<>();
        String taskContent = "Submit assignment (by: 2024-12-31)";
        Boolean isDone = false;

        assertDoesNotThrow(() -> {
            Interpreter.interpretAndLoadDeadlineSave(taskContent, isDone, tasks);
        });
    }

    @Test
    public void testInterpretAndLoadDeadlineSave_WithLocalDateTime() {
        ArrayList<Task> tasks = new ArrayList<>();
        String taskContent = "Submit assignment (by: 2024-12-31T23:59:59)";
        Boolean isDone = true;

        assertDoesNotThrow(() -> {
            Interpreter.interpretAndLoadDeadlineSave(taskContent, isDone, tasks);
        });
    }

    @Test
    public void testInterpretAndLoadDeadlineSave_InvalidDateFormat() {
        // Test with invalid date format
        String taskContent = "Submit assignment (by: invalid-date)";
        Boolean isDone = false;

        Interpreter.interpretAndLoadDeadlineSave(taskContent, isDone, tasks);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid date format for deadline input!"));
        assertTrue(output.contains("Deadline date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS"));
    }

    @Test
    public void testInterpretAndLoadEventSave_WithLocalDate() {
        ArrayList<Task> tasks = new ArrayList<>();
        String taskContent = "Team meeting (from: 2024-12-25 to: 2024-12-26)";
        Boolean isDone = false;

        assertDoesNotThrow(() -> {
            Interpreter.interpretAndLoadEventSave(taskContent, isDone, tasks);
        });
    }

    @Test
    public void testInterpretAndLoadEventSave_WithLocalDateTime() {
        ArrayList<Task> tasks = new ArrayList<>();
        String taskContent = "Conference (from: 2024-12-25T09:00:00 to: 2024-12-25T17:00:00)";
        Boolean isDone = true;

        assertDoesNotThrow(() -> {
            Interpreter.interpretAndLoadEventSave(taskContent, isDone, tasks);
        });
    }

    @Test
    public void testInterpretAndLoadEventSave_InvalidDateFormat() {
        String taskContent = "Invalid event (from: bad-date to: another-bad-date)";
        Boolean isDone = false;

        Interpreter.interpretAndLoadEventSave(taskContent, isDone, tasks);

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid date format for event input!"));
        assertTrue(output.contains("Event date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS"));
    }
}
