package com.example.cmprojeto;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.cmprojeto.database.*;
import android.app.Fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmprojeto.model.Color;
import com.example.cmprojeto.model.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    //TODO Import the active plan from the database
    private Plan plan = new Plan("Mobile Computing", "Let's Study", 90, Color.RED, true);

    private TextView mTimerText;
    private TextView mTimerPauseText;

    DBHelper dbHelper = DBHelper.getInstance();
    private Button mStartStop;
    private Button mReset;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer mCountDownPauseTimer;

    private boolean mTimerRunning;



    Button sessions, settings, home, study;
    TextView logout;
    ImageView openMenu;
    DrawerLayout drawer;
    LinearLayout plansLinear;

    private long mTimeLeftMillis = plan.getTime() * 60 * 1000;
    private long mTimeLeftPauseMillis = 10 * 60 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        mTimerText = (TextView) findViewById(R.id.timer);
        mTimerPauseText = (TextView) findViewById(R.id.pause);

        mStartStop = (Button) findViewById(R.id.b_start_pause);
        mReset = (Button) findViewById(R.id.b_reset);

        plansLinear = (LinearLayout) findViewById(R.id.plansLinear);

        sessions = (Button) findViewById(R.id.sessionsMenu);
        settings = (Button) findViewById(R.id.settingsMenu);
        home = (Button) findViewById(R.id.homeMenu);
        study = (Button) findViewById(R.id.studyMenu);
        logout = (TextView) findViewById(R.id.logout);



        openMenu = (ImageView) findViewById(R.id.openMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(Gravity.LEFT);

        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));

        sessions.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(getApplicationContext(), SessionActivity.class);
            startActivity(intent);
        });

        study.setOnClickListener(v -> {
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

        logout.setOnClickListener(v -> {
            dbHelper.logout();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        mStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.createPlan(new Plan("Mobile Computing","Let's Study", 90, Color.RED, false));
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                resetTimer();
            }
        });

        updateCountDownText();
        updateCountDownPauseText(mTimeLeftPauseMillis);
        populateActivity();
    }

    private void resetTimer() {
        mTimeLeftMillis = plan.getTime() * 60 * 1000;
        updateCountDownText();
        mReset.setEnabled(false);
        mStartStop.setEnabled(true);
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftMillis = l;
                updateCountDownText();

                int minutes = (int) mTimeLeftMillis / 1000 / 60;
                int seconds = (int) mTimeLeftMillis / 1000 % 60;

                if (minutes % 50 == 0 && seconds == 50){
                    pauseTimer();
                    mStartStop.setEnabled(false);
                    mReset.setEnabled(false);

                    mCountDownPauseTimer = new CountDownTimer(mTimeLeftPauseMillis, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mTimeLeftPauseMillis = millisUntilFinished;
                            updateCountDownPauseText(millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                            mTimeLeftMillis -= 1000;
                            resetPauseTimer();
                            startTimer();
                        }
                    }.start();
                }
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mStartStop.setText(getResources().getString(R.string.start));
                mStartStop.setEnabled(false);
                mReset.setEnabled(true);
            }
        }.start();

        mTimerRunning = true;
        mStartStop.setText(getResources().getString(R.string.pause));
        mReset.setEnabled(false);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mStartStop.setText(getResources().getString(R.string.start));
        mReset.setEnabled(true);
    }

    private void resetPauseTimer(){
        mTimeLeftPauseMillis = 10 * 60 * 1000;
        mReset.setEnabled(true);
        mStartStop.setEnabled(true);
        updateCountDownPauseText(mTimeLeftPauseMillis);
    }

    private void updateCountDownText() {
        int minutes = (int) mTimeLeftMillis / 1000 / 60;
        int seconds = (int) mTimeLeftMillis / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTimerText.setText(timeLeftFormatted);
    }


    private void updateCountDownPauseText(long mTimeLeftMillisPause) {
        int minutes = (int) mTimeLeftMillisPause / 1000 / 60;
        int seconds = (int) mTimeLeftMillisPause / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTimerPauseText.setText(timeLeftFormatted);
    }

    private void populateActivity(){
        if(!DBHelper.USER_PLANS.isPopulated()){
            dbHelper.getUserPlans(plans -> {
                DBHelper.USER_PLANS.populate(plans);

                for (Plan p: plans) {
                    if(!p.isActive()){
                        Fragment fg = PlanFragment.newInstance("10 min", p.getTime()+" min",p.getSubject(), p.getColor().toString());
                        getFragmentManager().beginTransaction().add(plansLinear.getId(),fg, "Ola").commit();
                        View view = fg.getView();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,300);
                        //view.setLayoutParams(params);
                        //view.requestLayout();
                    }
                }
            });

        }
    }
}
