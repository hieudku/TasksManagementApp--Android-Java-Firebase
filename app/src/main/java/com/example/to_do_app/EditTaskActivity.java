package com.example.to_do_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditTaskActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private DatePicker datePickerDueDate;
    private Button buttonUpdateTask;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String taskId;
    private Timestamp dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Find title, edit button, description, and date picker
        editTextTitle = findViewById(R.id.edit_task_name);
        editTextDescription = findViewById(R.id.edit_task_description);
        datePickerDueDate = findViewById(R.id.datepicker_due_date);
        buttonUpdateTask = findViewById(R.id.button_update_task);

        // Initialize Firebase database and user
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Get task details from intent
        Intent intent = getIntent();
        if (intent != null) {
            taskId = intent.getStringExtra("taskId");
            editTextTitle.setText(intent.getStringExtra("taskTitle"));
            editTextDescription.setText(intent.getStringExtra("taskDescription"));

            // Retrieve due date as long and convert it back to Timestamp
            long dueDateMillis = intent.getLongExtra("taskDueDate", -1);
            if (dueDateMillis != -1) {
                dueDate = new Timestamp(new Date(dueDateMillis));

                // Set the due date in the DatePicker
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dueDate.toDate());
                datePickerDueDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
        }

        // Set event for update button to call updateTask method
        buttonUpdateTask.setOnClickListener(v -> updateTask());
    }

    private void updateTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty()) {
            editTextTitle.setError("Title cannot be empty");
            editTextTitle.requestFocus();
            return;
        }

        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the selected date from DatePicker
        int day = datePickerDueDate.getDayOfMonth();
        int month = datePickerDueDate.getMonth();
        int year = datePickerDueDate.getYear();
        dueDate = new Timestamp(new Date(year - 1900, month, day));

        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        task.put("dueDate", dueDate);

        db.collection("users").document(user.getUid()).collection("tasks").document(taskId)
                .update(task)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditTaskActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditTaskActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
                });
    }

    public Map<String, Object> createUpdatedTaskMap(String title, String description, Timestamp dueDate) {
        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        task.put("dueDate", dueDate);
        task.put("timestamp", new Timestamp(new Date())); // current time
        return task;
    }
}
