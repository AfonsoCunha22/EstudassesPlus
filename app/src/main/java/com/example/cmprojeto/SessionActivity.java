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
import com.example.cmprojeto.callbacks.UserCallback;
import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.model.Plan;
import com.example.cmprojeto.model.Session;
import com.example.cmprojeto.model.UserInfo;

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
/*
    private void populateActivity(){
        if(!DBHelper.USER_SESSIONS.isPopulated()){
            dbHelper.getSessions(sessions -> {
                DBHelper.USER_SESSIONS.populate(sessions);

                for (Session s: sessions) {

                    //newInstance(String dateTime, String user, String subject, String location, String sessionID)
                        SessionFragment fg = SessionFragment.newInstance(s.getTime()+,s.getUserID(),s.getSubject(), s.getId());
                        fg.setClickInterface(this);
                        getFragmentManager().beginTransaction().add(plansLinear.getId(),fg, p.getId()).commit();

                }
            });
        }else {
            for (Plan plan:DBHelper.USER_PLANS.getPlans()) {
                if(plan.isActive()){
                    currentPlan = plan;
                    updateSubjectName();
                    updateCountDownText();
                    updateCountDownPauseText(mTimeLeftPauseMillis);
                    updateCircleColor();
                    mTimeLeftMillis = currentPlan.getTime() * 60 * 1000;
                }
            }
            dbHelper.getUserPlans(plans -> {
                for (Plan p: plans) {
                    if (!DBHelper.USER_PLANS.getPlans().contains(p)){
                        if(!p.isActive()){
                            PlanFragment fg = PlanFragment.newInstance(p.getTime()+" min",p.getSubject(), p.getColor().toString(), p.getId());
                            fg.setClickInterface(this);
                            getFragmentManager().beginTransaction().add(plansLinear.getId(),fg, p.getId()).commit();
                        }
                    }
                }
            });
        }
    }

 */

}
