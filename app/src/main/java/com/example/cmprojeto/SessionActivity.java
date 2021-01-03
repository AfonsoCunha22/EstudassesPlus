package com.example.cmprojeto;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.callbacks.SessionCallback;
import com.example.cmprojeto.callbacks.UserCallback;
import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.Plan;
import com.example.cmprojeto.model.Session;
import com.example.cmprojeto.model.UserInfo;
import com.google.type.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SessionActivity extends AppCompatActivity implements FragmentClick {
    Button newSession;
    DrawerLayout drawer;
    ImageView openMenu;
    DBHelper dbHelper = DBHelper.getInstance();
    LinearLayout sessionsLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        populateActivity();


        sessionsLinear = (LinearLayout) findViewById(R.id.sessionsLinear);
        openMenu = (ImageView) findViewById(R.id.openMenu);
        newSession = (Button) findViewById(R.id.newSession);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        MenuFragment fg = MenuFragment.newInstance();
        fg.setClickInterface(this);
        getFragmentManager().beginTransaction().add(drawer.getId(),fg, "menu").commit();

        newSession.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewSessionActivity.class);
            startActivity(intent);
        });

        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
    }

    @Override
    public void buttonClicked(String sessionID) {
        Bundle bundle = new Bundle();
        bundle.putString("sessionID",sessionID);
        Intent intent = new Intent(getApplicationContext(), ExpandedSessionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    public void menuClicked() {
        drawer.closeDrawer(Gravity.LEFT);
    }

    private void populateActivity(){
        if(!DBHelper.USER_SESSIONS.isPopulated()){
            dbHelper.getSessions(sessions -> {
                DBHelper.USER_SESSIONS.populate(sessions);

                for (Session s: sessions) {
                    s.getDate().setTime(s.getTime().getTime());
                    //newInstance(String dateTime, String user, String subject, String location, String sessionID)
                        SessionFragment fg = SessionFragment.newInstance(s.getDate().toString(),s.getUserName(),s.getSubject(), getLocationFromLarLong(s.getLocation().latitude,s.getLocation().longitude), s.getId());
                        fg.setClickInterface(this);
                        getFragmentManager().beginTransaction().add(sessionsLinear.getId(),fg, s.getId()).commit();

                }
            });
        }else {
            for (Session s: DBHelper.USER_SESSIONS.getSessions()){
                SessionFragment fg = SessionFragment.newInstance(s.getDate().toString(),s.getUserName(),s.getSubject(), getLocationFromLarLong(s.getLocation().latitude,s.getLocation().longitude), s.getId());
                fg.setClickInterface(this);
                getFragmentManager().beginTransaction().add(sessionsLinear.getId(),fg, s.getId()).commit();
            }
        }
    }
    private String getLocationFromLarLong(Double latitude, Double longitude){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                String _Location;
                return _Location = listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Inválido";
    }


}
