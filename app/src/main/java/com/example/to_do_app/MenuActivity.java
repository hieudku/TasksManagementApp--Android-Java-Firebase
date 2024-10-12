package com.example.to_do_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import android.content.SharedPreferences;

public class MenuActivity extends AppCompatActivity {
    // Declare fields
    private Button buttonLogout;
    private FirebaseAuth mAuth;
    private Switch darkModeSwitch;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        // Initialize UI elements
        Button addTaskButton = findViewById(R.id.add_task_button);
        Button viewTaskButton = findViewById(R.id.view_task_button);
        buttonLogout = findViewById(R.id.logout_button);
        darkModeSwitch = findViewById(R.id.dark_mode_switch);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize SharedPreferences for dark mode
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Check current dark mode status and set the switch accordingly
        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        darkModeSwitch.setChecked(isDarkModeOn);

        // Apply the dark mode if it's enabled
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Set the dark mode toggle listener
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable dark mode and save preference
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isDarkModeOn", true);
            } else {
                // Disable dark mode and save preference
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isDarkModeOn", false);
            }
            editor.apply();
        });

        // Logout event listener
        buttonLogout.setOnClickListener(view -> logoutUser());

        // Click event listener for add task button
        addTaskButton.setOnClickListener(view -> loadFragment(new AddTaskFragment()));

        // Click event listener for view task button
        viewTaskButton.setOnClickListener(view -> loadFragment(new ViewTasksFragment()));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to load specified fragment into fragment_container
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Optional: add this transaction to the back stack
        fragmentTransaction.commit();
    }

    // LOG OUT METHOD
    private void logoutUser() {
        mAuth.signOut(); // Sign out from Firebase
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
