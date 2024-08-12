package com.example.to_do_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    // Declare fields
    private Button buttonLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        // Get UI elements IDs
        Button addTaskButton = findViewById(R.id.add_task_button);
        Button viewTaskButton = findViewById(R.id.view_task_button);


        // Start firebase auth
        mAuth = FirebaseAuth.getInstance();
        buttonLogout = findViewById(R.id.logout_button);

        // LOG OUT event
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        // Click event listener for add task button
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load AddTaskFragment when this button is clicked
                loadFragment(new AddTaskFragment());
            }
        });

        // Click event listener for view task button
        viewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Load AddTaskFragment when this button is clicked
                loadFragment(new ViewTasksFragment());
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to load specified fragment into fragment_container
    private void loadFragment(Fragment fragment) {
        // Load the specified fragment into the fragment_container
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Optional: add this transaction to the back stack
        fragmentTransaction.commit();
    }

    // LOG OUT METHOD
    private void logoutUser() {
        // Take user to Main Activity (log in) screen if logged out
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // EDIT TASK CARD

}