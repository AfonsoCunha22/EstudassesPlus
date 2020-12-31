package com.example.cmprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cmprojeto.callbacks.SessionCallback;
import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.Session;

import java.util.List;

public class SessionActivity extends AppCompatActivity {
    Button newSession;
    DrawerLayout drawer;
    ImageView openMenu;
    Button session, settings, home, study;
    ListView list;
    DBHelper dbHelper = DBHelper.getInstance();
    String[] arrSessoes;
    ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        arrSessoes = new String[15];
        dbHelper.getSessions(sessions -> {

            if(sessions.size()!=0){
                arrSessoes = new String[sessions.size()];
                for(int i =0;i<sessions.size();i++){
                    arrSessoes[i]=sessions.get(i).getDescription();
                }
            }
        });

        list = (ListView) findViewById(R.id.listView);
        openMenu = (ImageView) findViewById(R.id.openMenu);
        adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrSessoes);
        list.setAdapter(adp);
        newSession = (Button) findViewById(R.id.newSession);
        drawer = (DrawerLayout) findViewById(R.id.drawer);

        drawer.closeDrawer(Gravity.LEFT);

        session = (Button) findViewById(R.id.sessionsMenu);
        settings = (Button) findViewById(R.id.settingsMenu);
        home = (Button) findViewById(R.id.homeMenu);
        study = (Button) findViewById(R.id.studyMenu);

        newSession.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewSessionActivity.class);
            startActivity(intent);
        });

        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));

        session.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
        });
        study.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
            startActivity(intent);
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
