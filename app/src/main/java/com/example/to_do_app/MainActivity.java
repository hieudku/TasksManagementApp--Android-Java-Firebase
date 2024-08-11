package com.example.to_do_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // Declare fields
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Start Firebase
        FirebaseApp.initializeApp(this);

        // Test Firebase integration
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("sampleCollection")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });

        // Initialize Firebase auth
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login_button);
        buttonRegister = findViewById(R.id.register_button);

        // Gather button UI element by id
        Button buttonLogin = findViewById(R.id.login_button);

        // LOG IN LOGIC -- when login button is clicked
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get username and password entered by the user
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Prompt when either email or passwrod is empty
                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required to log in");
                    editTextEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required to log in");
                    editTextPassword.requestFocus();
                    return;
                }


                // Call method with email and password entered as parameters to authenticate user's log in
                loginUser(email, password);
            }
        });


        // REGISTER LOGIC -- when click register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check for required fields/format
                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                }
                // Call method with email and password entered as parameters to authenticate user's registration
                registerUser(email, password);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // LOG IN AUTH-- email and password as parameters to authenticate
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) { // If login is successful
                FirebaseUser user = mAuth.getCurrentUser();
                // Then let user know
                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                // Then take user to Menu/Dashboard activity
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
            else { // Sign in unsuccessful, then exception
                Toast.makeText(MainActivity.this, "Login failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // REGISTER AUTH -- email and password as parameters to register
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) { // If registration is successful
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener(emailTask -> {
                       if (emailTask.isSuccessful()) {
                           Toast.makeText(MainActivity.this, "Registration successful! Please check your email for verification.", Toast.LENGTH_LONG).show();
                       }
                       else {
                           Toast.makeText(MainActivity.this, "Failed to send email.", Toast.LENGTH_SHORT).show();
                       }
                    });
                }

                // Then take user to Menu/Dashboard activity
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Registration failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Skip thje sign in screen if user already signed in
    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }*/

}