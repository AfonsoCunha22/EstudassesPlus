package com.example.cmprojeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.model.Plan;
import com.example.cmprojeto.model.Session;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import com.example.cmprojeto.database.DBHelper;

public class HomeActivity extends AppCompatActivity implements FragmentClick{


    TextView textViewEmail,textViewAccess,plansShortcutTitle, suggestedSessionTitle;
    ImageView openMenu;
    DrawerLayout drawer;
    Button buttonResendEmail,plansShortCut;
    View sessionView;
    RelativeLayout planLayout, sessionsLayout;

    SharedPreferences preferences;
    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textViewEmail = (TextView) findViewById(R.id.emailNotVerified);
        textViewAccess = (TextView) findViewById(R.id.toAccessEmail);
        plansShortcutTitle = (TextView) findViewById(R.id.noPlanTitle);
        suggestedSessionTitle = (TextView) findViewById(R.id.noSugestedTitle);
        sessionView = (View) findViewById(R.id.sessionView);
        buttonResendEmail = (Button) findViewById(R.id.resendEmail);
        planLayout = (RelativeLayout) findViewById(R.id.plansLayout);
        sessionsLayout = (RelativeLayout) findViewById(R.id.sessionLayout);
        plansShortCut = (Button) findViewById(R.id.noPlanShortcut);
        openMenu = (ImageView) findViewById(R.id.openMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        MenuFragment fg = MenuFragment.newInstance();
        fg.setClickInterface(this);
        getFragmentManager().beginTransaction().add(drawer.getId(),fg, "menu").commit();
        //drawer.closeDrawer(Gravity.LEFT);
        populatePlans();

        setupSharedPreferences();


        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
        plansShortCut.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
            startActivity(intent);
        });


        if(dbHelper.emailNotVerified()){
            textViewEmail.setVisibility(View.VISIBLE);
            textViewAccess.setVisibility(View.VISIBLE);
            sessionView.setVisibility(View.VISIBLE);
            buttonResendEmail.setVisibility(View.VISIBLE);
        }else {
            textViewEmail.setVisibility(View.GONE);
            textViewAccess.setVisibility(View.GONE);
            sessionView.setVisibility(View.GONE);
            buttonResendEmail.setVisibility(View.GONE);
            if(DBHelper.USER_PLANS.isPopulated())
                dbHelper.getFilteredSessions("subject", DBHelper.USER_PLANS.getPlans().get(0).getSubject(), sessions -> {
                    if(sessions.isEmpty()){
                        suggestedSessionTitle.setVisibility(View.VISIBLE);
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        dbHelper.getUserUsername(sessions.get(0).getUserID(), username -> {
                            SessionFragment sessionFragment = SessionFragment.newInstance(sdf.format(sessions.get(0).getDateTime()),
                                    username,
                                    sessions.get(0).getSubject(),
                                    getLocationFromLarLong(sessions.get(0).getLocation().latitude, sessions.get(0).getLocation().longitude),
                                    sessions.get(0).getSessionID());
                            fg.setClickInterface(this);
                            getFragmentManager().beginTransaction().add(sessionsLayout.getId(),sessionFragment, sessions.get(0).getSessionID()).commit();
                        });

                    }
                });
        }
        if(!DBHelper.USER_PLANS.isPopulated()){
            plansShortcutTitle.setVisibility(View.VISIBLE);
            plansShortCut.setVisibility(View.VISIBLE);
        }else{
            Plan plan = DBHelper.USER_PLANS.getPlans().get(0);
            PlanFragment frag = PlanFragment.newInstance(plan.getTime()+" min",plan.getSubject(), plan.getColor().toString(), plan.getId());
            fg.setClickInterface(this);
            getFragmentManager().beginTransaction().add(planLayout.getId(),frag, plan.getId()).commit();
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
    }

    @Override
    public void menuClicked() {
        drawer.closeDrawer(Gravity.LEFT);
    }

    private void populatePlans(){
        if(!DBHelper.USER_PLANS.isPopulated()){
            dbHelper.getUserPlans(DBHelper.USER_PLANS::populate);
        }
    }
    private String getLocationFromLarLong(Double latitude, Double longitude){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                return listAddresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Inválido";
    }
}