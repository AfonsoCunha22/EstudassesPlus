package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmprojeto.database.DBHelper;

public class SettingsActivity extends AppCompatActivity {

    TextView userName;
    ImageView goBack;
    Button btnAccount;
    Button btnNotifications, btnSensors, btnLanguage, btnHelp;

    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        goBack = (ImageView) findViewById(R.id.goBack);
        userName = (TextView) findViewById(R.id.userName);

        btnAccount = (Button) findViewById(R.id.btnAccount);
        btnNotifications = (Button) findViewById(R.id.btnNotification);
        btnSensors = (Button) findViewById(R.id.btnSensors);
        btnLanguage = (Button) findViewById(R.id.btnLanguage);
        btnHelp = (Button) findViewById(R.id.btnHelp);

        dbHelper.getUserInfo(user -> {
            userName.setText(user.getUsername());
        });

        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);
            startActivity(intent);
        });

        btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NotificationSettingsActivity.class);
            startActivity(intent);
        });

        btnSensors.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SensorSettingsActivity.class);
            startActivity(intent);
        });

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });
    }
}