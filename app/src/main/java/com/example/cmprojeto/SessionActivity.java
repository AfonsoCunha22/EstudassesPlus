package com.example.cmprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class SessionActivity extends AppCompatActivity {
    Button newSession;
    DrawerLayout drawer;
    ImageView openMenu;
    Button session, settings, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        openMenu = (ImageView) findViewById(R.id.openMenu);
        newSession = (Button) findViewById(R.id.newSession);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(Gravity.LEFT);

        session = (Button) findViewById(R.id.sessionsMenu);
        settings = (Button) findViewById(R.id.settingsMenu);
        home = (Button) findViewById(R.id.homeMenu);

        newSession.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewSessionActivity.class);
            startActivity(intent);
        });

        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));

        session.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
        });

        settings.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });

        home.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });
    }
}
