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

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {


    TextView textViewLogout, textViewEmail;
    ImageView openMenu;
    DrawerLayout drawer;
    Button buttonResendEmail, session, settings, home;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbHelper = new DBHelper();
        session = (Button) findViewById(R.id.sessionsMenu);
        settings = (Button) findViewById(R.id.settingsMenu);
        home = (Button) findViewById(R.id.homeMenu);
        textViewLogout = (TextView) findViewById(R.id.logout);
        textViewEmail = (TextView) findViewById(R.id.emailNotVerified);
        buttonResendEmail = (Button) findViewById(R.id.resendEmail);
        openMenu = (ImageView) findViewById(R.id.openMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(Gravity.LEFT);

        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));

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
        home.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
        });

        if(dbHelper.emailVerified()){
            textViewEmail.setVisibility(View.VISIBLE);
            buttonResendEmail.setVisibility(View.VISIBLE);
        }

        buttonResendEmail.setOnClickListener(v -> {
            if(dbHelper.emailVerified()){
                dbHelper.resendEmailVerification(HomeActivity.this, getApplicationContext());
            }else {
                textViewEmail.setVisibility(View.GONE);
                buttonResendEmail.setVisibility(View.GONE);
            }
        });

        textViewLogout.setOnClickListener(v -> {
            dbHelper.logout();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}