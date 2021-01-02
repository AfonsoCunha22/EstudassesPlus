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

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.callbacks.SessionCallback;
import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.Session;

import java.util.List;

public class SessionActivity extends AppCompatActivity implements FragmentClick {
    Button newSession;
    DrawerLayout drawer;
    ImageView openMenu;
    ListView list;
    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        dbHelper.getSessions(sessions -> { });


        list = (ListView) findViewById(R.id.listView);
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
    public void buttonClicked(String planID) {

    }
    @Override
    public void menuClicked() {
        drawer.closeDrawer(Gravity.LEFT);
    }
}
