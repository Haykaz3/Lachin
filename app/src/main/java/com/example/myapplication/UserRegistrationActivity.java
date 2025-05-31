package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText Username, FullName, Email, Password;
    private Button Register;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        // Initialize views
        Username = findViewById(R.id.Username);
        FullName = findViewById(R.id.FullName);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        Register = findViewById(R.id.Register);

        // Initialize UserService
        userService = new UserServiceImpl();

        // Set up the register button click listener
        Register.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        // Get user input
        String username = Username.getText().toString().trim();
        String fullName = FullName.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create User object
        User user = new User(username, fullName, email, password);

        // Call the register method from UserService
        userService.registerUser(user, new UserService.UserServiceCallback() {
            @Override
            public void onUserRegistered() {
                // Show success message and navigate or update UI
                Toast.makeText(UserRegistrationActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                finish();  // Close activity after successful registration
            }

            @Override
            public void onFailure(String error) {
                // Show failure message
                Toast.makeText(UserRegistrationActivity.this, "Registration Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}