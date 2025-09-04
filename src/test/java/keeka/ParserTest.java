package keeka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private ArrayList<Task> tasks;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        tasks = new ArrayList<>();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testParseSaveContent_TodoTask() {
        String saveContent = "1. [T][ ] Buy groceries";

        ParsedSaveContent result = Parser.parseSaveContent(saveContent);

        assertEquals('T', result.getTaskCode());
        assertEquals(false, result.getMarkedStatus());
        assertEquals("Buy groceries", result.getTaskContent());
    }

    @Test
    public void testParseSaveContent_MarkedDeadlineTask() {
        String saveContent = "2. [D][X] Submit assignment (by: 2024-12-31)";

        ParsedSaveContent result = Parser.parseSaveContent(saveContent);

        assertEquals('D', result.getTaskCode());
        assertEquals(true, result.getMarkedStatus());
        assertEquals("Submit assignment (by: 2024-12-31)", result.getTaskContent());
    }

    @Test
    public void testParseSaveContent_UnmarkedEventTask() {
        String saveContent = "3. [E][ ] Team meeting (from: 2024-12-25 to: 2024-12-26)";

        ParsedSaveContent result = Parser.parseSaveContent(saveContent);

        assertEquals('E', result.getTaskCode());
        assertEquals(false, result.getMarkedStatus());
        assertEquals("Team meeting (from: 2024-12-25 to: 2024-12-26)", result.getTaskContent());
    }

    @Test
    public void testParseSaveContent_EmptyDescription() {
        String saveContent = "1. [T][ ] ";

        ParsedSaveContent result = Parser.parseSaveContent(saveContent);

        assertEquals('T', result.getTaskCode());
        assertEquals(false, result.getMarkedStatus());
        assertEquals("", result.getTaskContent());
    }

    @Test
    public void testParseSaveContent_LongDescription() {
        String saveContent = "4. [D][X] Complete the very long and detailed project assignment with multiple requirements";

        ParsedSaveContent result = Parser.parseSaveContent(saveContent);

        assertEquals('D', result.getTaskCode());
        assertEquals(true, result.getMarkedStatus());
        assertEquals("Complete the very long and detailed project assignment with multiple requirements", result.getTaskContent());
    }

    @Test
    public void testParseDeadlineInput_ValidLocalDate() {
        String remainingContent = "Submit assignment /by 2024-12-31";

        assertDoesNotThrow(() -> {
            Parser.parseDeadlineInput(remainingContent, tasks);
        });

        // Verify no error messages were printed
        String output = outputStream.toString();
        assertFalse(output.contains("Invalid date format"));
    }

    @Test
    public void testParseDeadlineInput_ValidLocalDateTime() {
        String remainingContent = "Submit assignment /by 2024-12-31T23:59:59";

        assertDoesNotThrow(() -> {
            Parser.parseDeadlineInput(remainingContent, tasks);
        });

        // Verify no error messages were printed
        String output = outputStream.toString();
        assertFalse(output.contains("Invalid date format"));
    }

    @Test
    public void testParseDeadlineInput_InvalidDateFormat() {
        String remainingContent = "Submit assignment /by invalid-date";

        assertDoesNotThrow(() -> {
            Parser.parseDeadlineInput(remainingContent, tasks);
        });

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid date format for deadline input!"));
        assertTrue(output.contains("Deadline date format as follows: YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS"));
    }

}