package com.example.cmprojeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmprojeto.callbacks.FragmentClick;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import com.example.cmprojeto.database.DBHelper;

public class HomeActivity extends AppCompatActivity implements FragmentClick{


    TextView textViewEmail;
    ImageView openMenu;
    DrawerLayout drawer;
    Button buttonResendEmail;

    SharedPreferences preferences;
    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textViewEmail = (TextView) findViewById(R.id.emailNotVerified);
        buttonResendEmail = (Button) findViewById(R.id.resendEmail);
        openMenu = (ImageView) findViewById(R.id.openMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        MenuFragment fg = MenuFragment.newInstance();
        fg.setClickInterface(this);
        getFragmentManager().beginTransaction().add(drawer.getId(),fg, "menu").commit();
        //drawer.closeDrawer(Gravity.LEFT);

        setupSharedPreferences();


        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));


        if(dbHelper.emailNotVerified()){
            textViewEmail.setVisibility(View.VISIBLE);
            buttonResendEmail.setVisibility(View.VISIBLE);
        }

        buttonResendEmail.setOnClickListener(v -> {
            if(dbHelper.emailNotVerified()){
                dbHelper.resendEmailVerification(HomeActivity.this, getApplicationContext());
            }else {
                textViewEmail.setVisibility(View.GONE);
                buttonResendEmail.setVisibility(View.GONE);
            }
        });
    }

    private void setupSharedPreferences() {
        preferences = getSharedPreferences("notificationSettings", MODE_PRIVATE);

        if(preferences.getBoolean("isFirstRun", true)) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("isFirstRun", false);
            edit.putBoolean("sessionStart", true);
            edit.putBoolean("studyStart", true);
            edit.putBoolean("studyBreak", true);
            edit.putBoolean("studyEnd", true);

            edit.apply();
        }

        preferences = getSharedPreferences("sensorSettings", MODE_PRIVATE);

        if(preferences.getBoolean("isFirstRun", true)) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("isFirstRun", false);
            edit.putBoolean("accelerometerSwitch", true);
            edit.putBoolean("lightSwitch", true);

            edit.apply();
        }
    }

    @Override
    public void buttonClicked(String planID) {
        drawer.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void menuClicked() {
        drawer.closeDrawer(Gravity.LEFT);
    }
}