package com.codebyedu.sample1.ui.simple;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codebyedu.sample1.R;
import com.codebyedu.sample1.ui.compose.GameActivity;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        saveCredits();
        init();
    }

    private void init() {
        TextView helloTitleView = findViewById(R.id.helloTitle);
        helloTitleView.setText(String.format("Hello, %s", getExtraUsername()));

        TextView noTrustedView = findViewById(R.id.notTrustedMessage);
        Button playView = findViewById(R.id.btnPlay);

        checkSecure(playView, noTrustedView);
        playView.setOnClickListener(v -> play());
    }

    private void checkSecure(Button playView, TextView noTrustedView) {
        if (isEmulator() || isRooted() || isNotTrustCRT()) {
            playView.setEnabled(false);

            StringBuilder message = new StringBuilder(getString(R.string.not_trusted_message));
            if (isNotTrustCRT()) {
                message.append("\n");
                message.append(getString(R.string.crt_error));
            }
            if (isEmulator()) {
                message.append("\n");
                message.append(getString(R.string.emulator));
            }
            if (isRooted()) {
                message.append("\n");
                message.append(getString(R.string.root));
            }

            noTrustedView.setText(message);
            noTrustedView.setVisibility(TextView.VISIBLE);
        }
    }

    private void saveCredits() {
        SharedPreferences preferences = getSharedPreferences("credits", MODE_PRIVATE);
        preferences
                .edit()
                .putString("username", getExtraUsername())
                .putString("password", getExtraPassword())
                .apply();
    }

    private String getExtraUsername() {
        return getIntent().getStringExtra(EXTRA_USERNAME);
    }

    private String getExtraPassword() {
        return getIntent().getStringExtra(EXTRA_PASSWORD);
    }

    private boolean isEmulator() {
        String brand = Build.BRAND.toLowerCase(Locale.ROOT);
        return brand.startsWith("generic") || brand.startsWith("android") || brand.startsWith("google");
    }

    private boolean isRooted() {
        File file = new File("/system/xbin/su");
        return file.exists();
    }

    private void play() {
        startActivity(GameActivity.Companion.newIntent(this, getExtraUsername()));
    }

    private boolean isNotTrustCRT() {
        //проверка после версии АПИ 28 включительно
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return !getPackageManager().hasSigningCertificate("com.codeby.p2", hexToByte(CERT), PackageManager.CERT_INPUT_SHA256);
        } else {
            //проверка до АПИ 28
            try {
                PackageManager pm = getPackageManager();
                Signature signature = pm.getPackageInfo("com.codeby.p2", PackageManager.GET_SIGNATURES).signatures[0];
                byte[] pkgSign = MessageDigest.getInstance("SHA-256").digest(signature.toByteArray());
                return !Arrays.equals(hexToByte(CERT), pkgSign);
            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                return true;
            }
        }
    }

    //конвертация hex-строки в байтовый массив
    public static byte[] hexToByte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len -1; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static Intent newIntent(MainActivity mainActivity, String username, String password) {
        Intent intent = new Intent(mainActivity, SecondActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_PASSWORD, password);
        return intent;
    }

    static final String EXTRA_USERNAME = "com.codebyedu.sample1.ui.simple.SecondActivity.username";
    static final String EXTRA_PASSWORD = "com.codebyedu.sample1.ui.simple.SecondActivity.password";
    static final String CERT = "D5:29:13:0B:EE:C9:6B:95:48:B3:CC:19:9C:66:96:49:FF:39:CE:E0:2C:B1:6D:EB:AB:7E:49:7D:B3:9B:95:70";
}
