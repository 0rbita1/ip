package keeka.backend;

import org.junit.jupiter.api.Test;
import keeka.tasks.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskFactoryTest {
    
    @Test
    public void testCreateToDo() {
        ToDo todo = TaskFactory.createToDo("read book", false);
        
        assertEquals("read book", todo.getDescription());
        assertFalse(todo.isDone());
        assertEquals("T", todo.getTaskCode());
    }
    
    @Test
    public void testCreateDeadline_LocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
        Deadline deadline = TaskFactory.createDeadline("submit assignment", false, dateTime);
        
        assertEquals("submit assignment", deadline.getDescription());
        assertFalse(deadline.isDone());
        assertEquals("D", deadline.getTaskCode());
        assertEquals(dateTime, deadline.getDateTime());
    }
    
    @Test
    public void testCreateDeadline_LocalDate() {
        LocalDate date = LocalDate.of(2024, 12, 31);
        Deadline deadline = TaskFactory.createDeadline("submit assignment", false, date);
        
        assertEquals("submit assignment", deadline.getDescription());
        assertFalse(deadline.isDone());
        assertEquals("D", deadline.getTaskCode());
        assertEquals(date, deadline.getDate());
    }
    
    @Test
    public void testCreateEvent_LocalDateTime() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 31, 10, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 31, 12, 0, 0);
        Event event = TaskFactory.createEvent("meeting", false, start, end);
        
        assertEquals("meeting", event.getDescription());
        assertFalse(event.isDone());
        assertEquals("E", event.getTaskCode());
        assertEquals(start, event.getStartDateTime());
        assertEquals(end, event.getEndDateTime());
    }
    
    @Test
    public void testCreateEvent_LocalDate() {
        LocalDate start = LocalDate.of(2024, 12, 31);
        LocalDate end = LocalDate.of(2025, 1, 1);
        Event event = TaskFactory.createEvent("meeting", false, start, end);
        
        assertEquals("meeting", event.getDescription());
        assertFalse(event.isDone());
        assertEquals("E", event.getTaskCode());
        assertEquals(start, event.getStartDate());
        assertEquals(end, event.getEndDate());
    }
}
