package com.example.to_do_app;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import android.widget.DatePicker;

public class AddTaskFragment extends Fragment {
    // Fields
    private EditText editTextTitle;
    private EditText editTextDescription;
    private DatePicker datePickerDueDate; // New DatePicker field
    private Spinner spinnerImportance; // Spinner for importance level
    private Button buttonSaveTask;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        // Initialize user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Initialize UI components
        db = FirebaseFirestore.getInstance();
        editTextTitle = view.findViewById(R.id.edit_task_name);
        editTextDescription = view.findViewById(R.id.edit_task_description);
        datePickerDueDate = view.findViewById(R.id.datepicker_due_date); // Initialize DatePicker
        spinnerImportance = view.findViewById(R.id.spinner_importance); // Initialize Importance Spinner
        buttonSaveTask = view.findViewById(R.id.button_save_task);

        // Set up the click listener for the Save button
        buttonSaveTask.setOnClickListener(v -> saveTask());

        return view;
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty()) {
            editTextTitle.setError("Title cannot be empty");
            editTextTitle.requestFocus();
            return;
        }

        // Get the selected date from DatePicker
        int day = datePickerDueDate.getDayOfMonth();
        int month = datePickerDueDate.getMonth();
        int year = datePickerDueDate.getYear();
        Timestamp dueDate = new Timestamp(new java.util.Date(year - 1900, month, day));

        // Get the selected importance level from the Spinner
        int importance = spinnerImportance.getSelectedItemPosition(); // Low = 0, Medium = 1, High = 2

        // Create task map with due date and importance
        String taskId = db.collection("tasks").document().getId();
        Map<String, Object> task = new HashMap<>();
        task.put("id", taskId);
        task.put("title", title);
        task.put("description", description);
        task.put("dueDate", dueDate);
        task.put("importance", importance); // Save importance level
        task.put("timestamp", Timestamp.now());

        db.collection("users").document(user.getUid()).collection("tasks").document(taskId)
                .set(task)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Task added", Toast.LENGTH_SHORT).show();
                    editTextTitle.setText("");
                    editTextDescription.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error adding task", Toast.LENGTH_SHORT).show();
                });
    }
}
