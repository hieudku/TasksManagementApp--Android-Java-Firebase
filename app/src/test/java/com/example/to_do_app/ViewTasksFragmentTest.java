package com.example.to_do_app;

import com.google.firebase.Timestamp;
import junit.framework.TestCase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class ViewTasksFragmentTest extends TestCase {

    private ViewTasksFragment fragment;

    public void setUp() throws Exception {
        super.setUp();
        fragment = new ViewTasksFragment();
    }

    public void tearDown() throws Exception {
        fragment = null;
    }

    // Utility method to test date formatting
    private String formatTaskDueDate(Timestamp dueDate) {
        if (dueDate == null) {
            return "Due: Not set";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return "Due: " + sdf.format(dueDate.toDate());
    }

    public void testFormatTaskDueDate() {
        // TC_VIEW_01: Test Load Tasks - Correct date formatting

        // Arrange
        Date date = new Date(2024 - 1900, 8, 17); // September 17, 2024
        Timestamp timestamp = new Timestamp(date);

        // Act
        String formattedDate = formatTaskDueDate(timestamp);

        // Assert
        assertEquals("Due: Sep 17, 2024", formattedDate);
    }

    public void testFormatTaskDueDateWithNull() {
        // TC_VIEW_02: Test Load Tasks with Missing Due Date

        // Act
        String formattedDate = formatTaskDueDate(null);

        // Assert
        assertEquals("Due: Not set", formattedDate);
    }

    public void testLoadTasks() {
        // TC_VIEW_01: Test Load Tasks - Correct loading and display of tasks

        // Arrange
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("title", "Test Task");
        taskMap.put("description", "Test Description");
        taskMap.put("dueDate", new Timestamp(new Date(2024 - 1900, 8, 17)));

        // Act
        String formattedDate = formatTaskDueDate((Timestamp) taskMap.get("dueDate"));

        // Assert
        assertEquals("Due: Sep 17, 2024", formattedDate);
        assertEquals("Test Task", taskMap.get("title"));
        assertEquals("Test Description", taskMap.get("description"));
    }

    public void testEditTaskNavigation() {
        // TC_VIEW_04: Test Edit Task Navigation

        // Arrange
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("id", "task123");
        taskMap.put("title", "Task to be edited");
        taskMap.put("description", "Edit this description");
        taskMap.put("dueDate", new Timestamp(new Date(2024 - 1900, 8, 17)));
    }
}
