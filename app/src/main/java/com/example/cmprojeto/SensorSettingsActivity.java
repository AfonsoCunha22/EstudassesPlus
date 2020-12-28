package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;

public class SensorSettingsActivity extends AppCompatActivity {
    DrawerLayout drawer;
    ImageView openMenu;

    SwitchCompat tempSwitch, lightSwitch;

    Button session, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);

        openMenu = (ImageView) findViewById(R.id.openMenu);
        session = (Button) findViewById(R.id.sessionsMenu);
        settings = (Button) findViewById(R.id.settingsMenu);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(Gravity.LEFT);

        tempSwitch = (SwitchCompat) findViewById(R.id.tempSwitch);
        lightSwitch = (SwitchCompat) findViewById(R.id.lightSwitch);

        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));

        // Menu Buttons
        session.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(getApplicationContext(), SessionActivity.class);
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }
}