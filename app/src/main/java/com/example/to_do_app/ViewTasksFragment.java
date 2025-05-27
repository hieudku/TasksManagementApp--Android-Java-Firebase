package com.example.to_do_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewTasksFragment extends Fragment {
    private LinearLayout linearLayoutTasks;
    private FirebaseFirestore db;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ViewTasksFragment() {
        // Required empty public constructor
    }

    public static ViewTasksFragment newInstance(String param1, String param2) {
        ViewTasksFragment fragment = new ViewTasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_tasks, container, false);
        db = FirebaseFirestore.getInstance();
        linearLayoutTasks = view.findViewById(R.id.linearLayoutTasks);
        loadTasks();
        return view;
    }

    private void loadTasks() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(user.getUid()).collection("tasks").orderBy("timestamp").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                linearLayoutTasks.removeAllViews();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Task taskObj = document.toObject(Task.class);

                    View taskCardView = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_card, linearLayoutTasks, false);

                    TextView taskTitleTextView = taskCardView.findViewById(R.id.textViewTitle);
                    TextView taskDescriptionTextView = taskCardView.findViewById(R.id.textViewDescription);
                    TextView taskDueDateTextView = taskCardView.findViewById(R.id.textViewDueDate);
                    TextView taskImportanceTextView = taskCardView.findViewById(R.id.textViewImportance);

                    ImageButton editButton = taskCardView.findViewById(R.id.buttonEditTask);
                    ImageButton deleteButton = taskCardView.findViewById(R.id.buttonDeleteTask);

                    // Set task details
                    taskTitleTextView.setText(taskObj.getTitle());
                    taskDescriptionTextView.setText(taskObj.getDescription());

                    // Format the due date
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    if (taskObj.getDueDate() != null) {
                        taskDueDateTextView.setText("Due: " + sdf.format(taskObj.getDueDate().toDate()));
                    } else {
                        taskDueDateTextView.setText("Due: Not set");
                    }

                    // Apply color-coded importance level
                    if (taskObj.getImportance() != null) {
                        switch (taskObj.getImportance()) {
                            case 0: // Low importance
                                taskImportanceTextView.setText("Importance: Low");
                                taskImportanceTextView.setTextColor(getResources().getColor(R.color.importanceLow)); // Green
                                break;
                            case 1: // Medium importance
                                taskImportanceTextView.setText("Importance: Medium");
                                taskImportanceTextView.setTextColor(getResources().getColor(R.color.importanceMedium)); // Yellow
                                break;
                            case 2: // High importance
                                taskImportanceTextView.setText("Importance: High");
                                taskImportanceTextView.setTextColor(getResources().getColor(R.color.importanceHigh)); // Red
                                break;
                            default:
                                taskImportanceTextView.setText("Importance: Not set");
                                taskImportanceTextView.setTextColor(getResources().getColor(R.color.black)); // Default to black
                        }
                    } else {
                        taskImportanceTextView.setText("Importance: Not set");
                        taskImportanceTextView.setTextColor(getResources().getColor(R.color.black)); // Default to black
                    }

                    // Set edit and delete functionality
                    editButton.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                        intent.putExtra("taskId", taskObj.getId());
                        intent.putExtra("taskTitle", taskObj.getTitle());
                        intent.putExtra("taskDescription", taskObj.getDescription());
                        intent.putExtra("taskDueDate", taskObj.getDueDate());
                        startActivity(intent);
                    });

                    deleteButton.setOnClickListener(v -> confirmDeleteTask(taskObj.getId()));

                    linearLayoutTasks.addView(taskCardView);
                }
            }
        });
    }

    void deleteTask(String taskId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).collection("tasks").document(taskId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
                        loadTasks();
                        onResume();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Error deleting task", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void confirmDeleteTask(String taskId) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", (dialog, which) -> deleteTask(taskId))
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        linearLayoutTasks.removeAllViews();
        loadTasks();
    }
}
