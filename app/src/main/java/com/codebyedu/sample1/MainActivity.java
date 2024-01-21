package com.codebyedu.sample1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText nameView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameView = findViewById(R.id.edUsername);
        passwordView = findViewById(R.id.edPassword);
        Button loginView = findViewById(R.id.btnLogin);
        loginView.setOnClickListener(v -> login());
    }

    private void login() {
        String username = nameView.getText().toString();
        String password = passwordView.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT)
                    .show();
        } else {
            startActivity(SecondActivity.newIntent(this, username, password));
            finish();
        }
    }
}