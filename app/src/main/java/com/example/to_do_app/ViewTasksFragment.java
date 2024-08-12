package com.example.to_do_app;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import androidx.annotation.NonNull;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewTasksFragment extends Fragment {
    // Declare fields
    private LinearLayout linearLayoutTasks;
    private FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewTasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewTasksFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_tasks, container, false);

        // Assign to instance of firestore
        db = FirebaseFirestore.getInstance();
        linearLayoutTasks = view.findViewById(R.id.linearLayoutTasks);

        // Load tasks from Firestore
        loadTasks();
        return view;
    }

    private void loadTasks() {
        // Query tasks collection from Firestore orderedby time created
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("users").document(user.getUid()).collection("tasks").orderBy("timestamp").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Task taskObj = document.toObject(Task.class);

                    View taskCardView = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_card, linearLayoutTasks, false);
                    TextView taskTitleTextView = taskCardView.findViewById(R.id.textViewTitle);
                    TextView taskDescriptionTextView = taskCardView.findViewById(R.id.textViewDescription);

                    // Image button for updating and deleting task
                    ImageButton editButton = taskCardView.findViewById(R.id.buttonEditTask);
                    ImageButton deleteButton = taskCardView.findViewById(R.id.buttonDeleteTask);

                    // Create and append TextView for each task on fragment_view_tasks.xml
                    taskTitleTextView.setText(taskObj.getTitle());
                    taskDescriptionTextView.setText(taskObj.getDescription());

                    // Set cilck event on edit button to import updated task details from EditTaskActivity
                    editButton.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                        intent.putExtra("taskId", taskObj.getId());
                        intent.putExtra("taskTitle", taskObj.getTitle());
                        intent.putExtra("taskDescription", taskObj.getDescription());
                        startActivity(intent);
                    });

                    // Set click event on delete button to remove tasks
                    deleteButton.setOnClickListener(v -> {
                        // method here to delete
                    });

                    linearLayoutTasks.addView(taskCardView);
                }
            }
        });
    }
    // This method ensure the UI is updated correctly when fragments are viewed
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadTasks();
    }
    // This method will call loadTask() method to load updated tasks as soon as the user return from the Edit Task activity.
    @Override
    public void onResume() {
        super.onResume();
        linearLayoutTasks.removeAllViews();
        loadTasks();
    }

}
