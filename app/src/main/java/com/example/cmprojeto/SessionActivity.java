package com.example.cmprojeto;

import android.app.Fragment;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.Session;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SessionActivity extends AppCompatActivity implements FragmentClick {
    DrawerLayout drawer;
    ImageView openMenu, newSession;
    DBHelper dbHelper = DBHelper.getInstance();

    LinearLayout sessionsLinear;

    ImageButton searchBtn;
    EditText searchBar;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        sessionsLinear = (LinearLayout) findViewById(R.id.sessionsLinear);
        openMenu = (ImageView) findViewById(R.id.openMenu);
        newSession = (ImageView) findViewById(R.id.newSession);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        searchBar = (EditText) findViewById(R.id.searchBar);

        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        MenuFragment fg = MenuFragment.newInstance();
        fg.setClickInterface(this);
        getFragmentManager().beginTransaction().add(drawer.getId(),fg, "menu").commit();

        populateActivity();

        newSession.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewSessionActivity.class);
            startActivity(intent);
        });

        searchBtn.setOnClickListener(v -> {
            if(searchBar.getText().toString().isEmpty())
                return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                populateSearchResult();
            }
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

    private void populateActivity() {
        if(!DBHelper.USER_SESSIONS.isPopulated()) {
            dbHelper.getUserEnrolledSessions(sessions -> {
                DBHelper.USER_SESSIONS.populate(sessions);

                for (Session s: sessions) {
                    dbHelper.getUserUsername(s.getUserID(), username -> {
                        SessionFragment fg = SessionFragment.newInstance(sdf.format(s.getDateTime()),
                                username,
                                s.getSubject(),
                                getLocationFromLarLong(s.getLocation().latitude, s.getLocation().longitude),
                                s.getSessionID());

                        fg.setClickInterface(this);
                        getFragmentManager().beginTransaction().add(sessionsLinear.getId(),fg, s.getSessionID()).commit();
                        System.out.println(fg.getId());
                    });
                }
            });
        } else {
            for (Session s: DBHelper.USER_SESSIONS.getSessions()){
                dbHelper.getUserUsername(s.getUserID(), username -> {
                    SessionFragment fg = SessionFragment.newInstance(sdf.format(s.getDateTime()),
                            username,
                            s.getSubject(),
                            getLocationFromLarLong(s.getLocation().latitude, s.getLocation().longitude),
                            s.getSessionID());

                    fg.setClickInterface(this);
                    getFragmentManager().beginTransaction().add(sessionsLinear.getId(),fg, s.getSessionID()).commit();
                    System.out.println(fg.getId());
                });
            }
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

    private void populateSearchResult() {
        dbHelper.getFilteredSessions("subject", searchBar.getText().toString(), sessions -> {
            if(sessions.isEmpty()){
                AlertDialog.Builder colorError = new AlertDialog.Builder(this);
                colorError.setMessage(getResources().getString(R.string.no_sessions_found));
                colorError.setPositiveButton("Ok", (dialog, which) -> {});
                colorError.create().show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    for(Fragment fragment: getFragmentManager().getFragments()){
                        getFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                }

                for (Session s: sessions){
                    dbHelper.getUserUsername(s.getUserID(), username -> {
                        SessionFragment fg = SessionFragment.newInstance(sdf.format(s.getDateTime()),
                                username,
                                s.getSubject(),
                                getLocationFromLarLong(s.getLocation().latitude, s.getLocation().longitude),
                                s.getSessionID());

                        fg.setClickInterface(this);
                        getFragmentManager().beginTransaction().add(sessionsLinear.getId(),fg, s.getSessionID()).commit();
                    });
                }
            }
        });
    }
}
