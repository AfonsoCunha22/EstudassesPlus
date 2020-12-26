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
    Button buttonResendEmail;
    DBHelper dbHelper;
    Button session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbHelper = new DBHelper();
        session = (Button) findViewById(R.id.sessionsMenu);
        textViewLogout = (TextView) findViewById(R.id.logout);
        textViewEmail = (TextView) findViewById(R.id.emailNotVerified);
        buttonResendEmail = (Button) findViewById(R.id.resendEmail);
        openMenu = (ImageView) findViewById(R.id.openMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(Gravity.LEFT);

        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SessionActivity.class);
                startActivity(intent);
            }
        });


        if(dbHelper.emailVerified()){
            textViewEmail.setVisibility(View.VISIBLE);
            buttonResendEmail.setVisibility(View.VISIBLE);
        }
        buttonResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHelper.emailVerified()){
                    dbHelper.resendEmailVerification(HomeActivity.this, getApplicationContext());
                }else {
                    textViewEmail.setVisibility(View.GONE);
                    buttonResendEmail.setVisibility(View.GONE);
                }
            }
        });


        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.logout();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}