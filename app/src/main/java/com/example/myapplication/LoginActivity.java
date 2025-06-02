package com.example.myapplication;

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

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button submit;
    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        submit = findViewById(R.id.Login);
        userService = new UserServiceImpl();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        // Get user input
        String Username = username.getText().toString().trim();
        String Password = password.getText().toString();

        // Validate input
        if (Username.isEmpty() || Password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create User object
        LoginDTO login = new LoginDTO();
        login.username = Username;
        login.password = Password;

        // Call the register method from UserService
        userService.login(login, new UserService.UserLoginCallback() {
            @Override
            public void onUserLoggedIn(String userId) {
                Toast.makeText(getApplicationContext(), "User Id: " + userId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}