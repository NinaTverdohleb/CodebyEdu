package com.codebyedu.sample1.ui.simple;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codebyedu.sample1.R;
import com.codebyedu.sample1.ui.compose.GameActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView helloTitleView = findViewById(R.id.helloTitle);
        helloTitleView.setText(String.format("Hello, %s", getExtraUsername()));
        Button playView = findViewById(R.id.btnPlay);
        playView.setOnClickListener(v -> play());
    }

    private String getExtraUsername() {
        return getIntent().getStringExtra(EXTRA_USERNAME);
    }

    private String getExtraPassword() {
        return getIntent().getStringExtra(EXTRA_PASSWORD);
    }

    private void play() {
        startActivity(GameActivity.Companion.newIntent(this, getExtraUsername()));
    }

    public static Intent newIntent(MainActivity mainActivity, String username, String password) {
        Intent intent = new Intent(mainActivity, SecondActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, password);
        return intent;
    }

    static final String EXTRA_USERNAME = "com.codebyedu.sample1.ui.simple.SecondActivity.username";
    static final String EXTRA_PASSWORD = "com.codebyedu.sample1.ui.simple.SecondActivity.password";
}
