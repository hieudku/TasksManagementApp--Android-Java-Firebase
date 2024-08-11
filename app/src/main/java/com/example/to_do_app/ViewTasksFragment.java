package com.example.to_do_app;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.Task;
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
    private RecyclerView recyclerViewTasks;
    private List<Task> taskList;
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

        // Initialize recycler view by ID
        recyclerViewTasks = view.findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskList = new ArrayList<>();

        // Load tasks from Firestore
        loadTasks();
        return view;
    }

    private void loadTasks() {
        // Query tasks collection from Firestore orderedby time created
        db.collection("tasks").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                if (error != null) { // If error occurs just return
                    return;
                }

                taskList.clear();

                for (QueryDocumentSnapshot doc : value) {
                    Task task = doc.toObject(Task.class);
                    taskList.add(task);
                }

            }
        });
    }
}