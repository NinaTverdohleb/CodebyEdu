package com.codebyedu.sample1.ui.simple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codebyedu.sample1.R;

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
            Toast.makeText(this, getString(R.string.password_or_name_empty_error), Toast.LENGTH_SHORT)
                    .show();
        } else {
            startActivity(SecondActivity.newIntent(this, username, password));
            finish();
        }
    }
}