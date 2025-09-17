package keeka.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    
    private Parser parser;
    
    @BeforeEach
    public void setUp() {
        parser = new Parser();
    }
    
    @Test
    public void testParseSaveContent() {
        String saveContent = "1. [T][X] read book";
        Parser.ParsedSaveContent parsed = parser.parseSaveContent(saveContent);
        
        assertEquals('T', parsed.getTaskCode());
        assertTrue(parsed.isDone());
        assertEquals("read book", parsed.getTaskContent());
    }
    
    @Test
    public void testParseDeadlineInput_LocalDate() {
        String input = "submit assignment /by 2024-12-31";
        Parser.DeadlineInput deadlineInput = parser.parseDeadlineInput(input);
        
        assertEquals("submit assignment", deadlineInput.getDescription());
        assertEquals(LocalDate.of(2024, 12, 31), deadlineInput.getDate());
        assertNull(deadlineInput.getDateTime());
    }
    
    @Test
    public void testParseDeadlineInput_LocalDateTime() {
        String input = "submit assignment /by 2024-12-31T23:59:59";
        Parser.DeadlineInput deadlineInput = parser.parseDeadlineInput(input);
        
        assertEquals("submit assignment", deadlineInput.getDescription());
        assertEquals(LocalDateTime.of(2024, 12, 31, 23, 59, 59), deadlineInput.getDateTime());
        assertNull(deadlineInput.getDate());
    }
    
    @Test
    public void testParseEventInput_LocalDate() {
        String input = "meeting /from 2024-12-31 /to 2025-01-01";
        Parser.EventInput eventInput = parser.parseEventInput(input);
        
        assertEquals("meeting", eventInput.getDescription());
        assertEquals(LocalDate.of(2024, 12, 31), eventInput.getStartDate());
        assertEquals(LocalDate.of(2025, 1, 1), eventInput.getEndDate());
        assertNull(eventInput.getStartDateTime());
        assertNull(eventInput.getEndDateTime());
    }
    
    @Test
    public void testParseEventInput_LocalDateTime() {
        String input = "meeting /from 2024-12-31T10:00:00 /to 2024-12-31T12:00:00";
        Parser.EventInput eventInput = parser.parseEventInput(input);
        
        assertEquals("meeting", eventInput.getDescription());
        assertEquals(LocalDateTime.of(2024, 12, 31, 10, 0, 0), eventInput.getStartDateTime());
        assertEquals(LocalDateTime.of(2024, 12, 31, 12, 0, 0), eventInput.getEndDateTime());
        assertNull(eventInput.getStartDate());
        assertNull(eventInput.getEndDate());
    }
    
    @Test
    public void testParseUpdateInput() {
        String input = "1 description new task description";
        Parser.UpdateInput updateInput = parser.parseUpdateInput(input);
        
        assertEquals(0, updateInput.getTaskIndex());
        assertEquals("description", updateInput.getFieldType());
        assertEquals("new task description", updateInput.getNewValue());
    }
}
