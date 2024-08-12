package com.example.to_do_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditTaskActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonUpdateTask;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String taskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_task);

        // Find title, edit button and desc ID
        editTextTitle = findViewById(R.id.edit_task_name);
        editTextDescription = findViewById(R.id.edit_task_description);
        buttonUpdateTask = findViewById(R.id.button_update_task);

        // Initialize Firebase database and user
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Get task details from intent to the editing text view
        Intent intent = getIntent();
        if (intent != null) {
            taskId = intent.getStringExtra("taskId");
            editTextTitle.setText(intent.getStringExtra("taskTitle"));
            editTextDescription.setText(intent.getStringExtra("taskDescription"));
        }
        // set event for update button to call update task method
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
        }

        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);

        // Get db path from firestore and update according to parameters.
        // Display text whether or not task is updated
        db.collection("users").document(user.getUid()).collection("tasks").document(taskId)
                .update(task)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditTaskActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
                    finish(); // If updated successful, take user back to previous activity
                })
        .addOnFailureListener(e -> {
            Toast.makeText(EditTaskActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
        });
    }
}