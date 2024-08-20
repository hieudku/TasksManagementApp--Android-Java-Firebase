package com.example.to_do_app;

import android.content.Intent;
import android.os.Bundle;
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

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassConfirm;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_popup);

        mAuth = FirebaseAuth.getInstance();
        buttonRegister = findViewById(R.id.btnRegister);
        editTextEmail = findViewById(R.id.emailRegistration);
        editTextPassword = findViewById(R.id.passwordRegistration);
        editTextPassConfirm = findViewById(R.id.confirmPassRegistration);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPass = editTextPassConfirm.getText().toString().trim();
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
                if (!confirmPass.equals(password)) {
                    editTextPassConfirm.setError("Passwords do not match");
                    return;
                }
                // Call method with email and password entered as parameters to authenticate user's registration
                registerUser(email, password);
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
                            Toast.makeText(RegistrationActivity.this, "Registration successful! Please check your email for verification.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(RegistrationActivity.this, "Failed to send email.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // Then take user to Login activity
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegistrationActivity.this, "Registration failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}