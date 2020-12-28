package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    DrawerLayout drawer;
    TextView userName;
    ImageView openMenu;
    Button btnAccount;
    Button btnNotifications;
    Button btnSensors;
    Button btnLanguage;
    Button btnHelp;
    Button session, settings;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        DBHelper db = new DBHelper();

        openMenu = (ImageView) findViewById(R.id.openMenu);
        userName = (TextView) findViewById(R.id.userName);

        session = (Button) findViewById(R.id.sessionsMenu);
        settings = (Button) findViewById(R.id.settingsMenu);

        btnAccount = (Button) findViewById(R.id.btnAccount);
        btnNotifications = (Button) findViewById(R.id.btnNotification);
        btnSensors = (Button) findViewById(R.id.btnSensors);
        btnLanguage = (Button) findViewById(R.id.btnLanguage);
        btnHelp = (Button) findViewById(R.id.btnHelp);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(Gravity.LEFT);

        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));

        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);
            startActivity(intent);
        });

        btnSensors.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SensorSettingsActivity.class);
            startActivity(intent);
        });

        // Menu Buttons
        session.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(getApplicationContext(), SessionActivity.class);
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
        });
    }
}