package com.example.to_do_app;

import com.google.firebase.Timestamp;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AddTaskFragmentTest {

    public Map<String, Object> createTaskMap(String title, String description, Date dueDate) {
        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        if (dueDate != null) {
            task.put("dueDate", new Timestamp(dueDate));
        }
        task.put("timestamp", new Timestamp(new Date()));
        return task;
    }

    @Test
    public void testCreateTaskMap() {
        // TC_ADD_01: Test Save Task with Valid Input

        // Arrange
        String title = "Test Task";
        String description = "Test Description";
        Date dueDate = new Date(2024 - 1900, 8, 17); // September 17, 2024

        // Act
        Map<String, Object> taskMap = createTaskMap(title, description, dueDate);

        // Assert
        assertEquals("Test Task", taskMap.get("title"));
        assertEquals("Test Description", taskMap.get("description"));
        assertTrue(taskMap.get("dueDate") instanceof Timestamp);
        assertTrue(taskMap.get("timestamp") instanceof Timestamp);
    }

    @Test
    public void testCreateTaskMapWithEmptyTitle() {
        // TC_ADD_02: Test Save Task with Empty Title

        // Arrange
        String title = "";  // Empty title
        String description = "Test Description";
        Date dueDate = new Date(2024 - 1900, 8, 17); // September 17, 2024

        // Act
        Map<String, Object> taskMap = createTaskMap(title, description, dueDate);

        // Assert
        assertEquals("", taskMap.get("title"));
        assertEquals("Test Description", taskMap.get("description"));
        assertTrue(taskMap.get("dueDate") instanceof Timestamp);
    }

    @Test
    public void testCreateTaskMapWithoutDueDate() {
        // TC_ADD_03: Test Save Task Without Date

        // Arrange
        String title = "Test Task";
        String description = "Test Description";

        // Act
        Map<String, Object> taskMap = createTaskMap(title, description, null);

        // Assert
        assertEquals("Test Task", taskMap.get("title"));
        assertEquals("Test Description", taskMap.get("description"));
        assertNull(taskMap.get("dueDate"));  // Due date should be null
        assertTrue(taskMap.get("timestamp") instanceof Timestamp);
    }
}
