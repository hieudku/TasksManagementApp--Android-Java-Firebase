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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Gather button UI element by id
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get username and password entered by the user
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                // Validate username and password entered
                if (username.equals("admin") && password.equals("1234")) {
                    // Successful login
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
                else {
                    // Failed login
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
    }



}