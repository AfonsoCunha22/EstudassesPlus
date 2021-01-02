package com.example.cmprojeto;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorStateListDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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

import com.example.cmprojeto.callbacks.FragmentClick;
import com.example.cmprojeto.database.*;
import android.app.Fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmprojeto.model.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity implements FragmentClick {
    //TODO Import the active plan from the database
    private Plan currentPlan = null;

    private TextView mTimerText;
    private TextView mTimerPauseText;
    private TextView mSubjectText;

    DBHelper dbHelper = DBHelper.getInstance();
    private Button mStartStop;
    private Button mReset;
    private Button mDescription;
    private Button mCreatePlan;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer mCountDownPauseTimer;

    private boolean mTimerRunning;



    View circleView;
    Button sessions, settings, home, study;
    TextView logout;
    ImageView openMenu;
    DrawerLayout drawer;
    LinearLayout plansLinear;

    private long mTimeLeftMillis = 0;
    private long mTimeLeftPauseMillis = 10 * 60 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        mTimerText = (TextView) findViewById(R.id.timer);
        mTimerPauseText = (TextView) findViewById(R.id.pause);
        mSubjectText = (TextView) findViewById(R.id.subject);

        mStartStop = (Button) findViewById(R.id.b_start_pause);
        mReset = (Button) findViewById(R.id.b_reset);
        mDescription = (Button) findViewById(R.id.b_description);
        mCreatePlan = (Button) findViewById(R.id.createPlanBtn);
        circleView = (View) findViewById(R.id.circleView);

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

        mCreatePlan.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.LEFT);
            Intent intent = new Intent(getApplicationContext(), NewPlan.class);
            startActivity(intent);
        });

        mStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                resetTimer();
            }
        });

        mDescription.setOnClickListener(v->{
            AlertDialog.Builder descDialog = new AlertDialog.Builder(v.getContext());
            descDialog.setTitle(getResources().getString(R.string.description));
            if(currentPlan == null) descDialog.setMessage(getResources().getString(R.string.no_plan_selected));
            else  descDialog.setMessage(currentPlan.getDescription());
            descDialog.setPositiveButton("Ok", (dialog, which) -> { });
            descDialog.setIcon(R.drawable.ic_description);
            descDialog.create().show();
        });

        populateActivity();
    }

    private void resetTimer() {
        mTimeLeftMillis = currentPlan.getTime() * 60 * 1000;
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
        mStartStop.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_pause), null, null ,null);
        mStartStop.setOnClickListener(l ->{pauseTimer();});
        mReset.setEnabled(false);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mStartStop.setText(getResources().getString(R.string.start));
        mStartStop.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_start), null, null ,null);
        mStartStop.setOnClickListener(l ->{startTimer();});
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

    private void updateCircleColor(){
        GradientDrawable drawable = (GradientDrawable) circleView.getBackground();
        Color color = null;

        System.out.println(currentPlan.getColor().toString());
        //drawable.setStroke(1,Color.YELLOW);
    }


    private void updateCountDownPauseText(long mTimeLeftMillisPause) {
        int minutes = (int) mTimeLeftMillisPause / 1000 / 60;
        int seconds = (int) mTimeLeftMillisPause / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTimerPauseText.setText(timeLeftFormatted);
    }

    private void updateSubjectName(){
        if(currentPlan == null) mSubjectText.setText(getResources().getString(R.string.no_plan_selected));
        else mSubjectText.setText(currentPlan.getSubject());
    }

    private void populateActivity(){
        if(!DBHelper.USER_PLANS.isPopulated()){
            dbHelper.getUserPlans(plans -> {
                DBHelper.USER_PLANS.populate(plans);

                for (Plan p: plans) {
                    if(!p.isActive()){
                        PlanFragment fg = PlanFragment.newInstance(p.getTime()+" min",p.getSubject(), p.getColor().toString(), p.getId());
                        fg.setClickInterface(this);
                        getFragmentManager().beginTransaction().add(plansLinear.getId(),fg, p.getId()).commit();

                    }else {
                        currentPlan = p;
                        updateSubjectName();
                        updateCountDownText();
                        updateCountDownPauseText(mTimeLeftPauseMillis);
                        updateCircleColor();
                        mTimeLeftMillis = currentPlan.getTime() * 60 * 1000;
                    }
                    System.out.println(p.getPlanInfo());
                }
            });
        }else {
            dbHelper.getUserPlans(plans -> {
                for (Plan p: plans) {
                    if (!DBHelper.USER_PLANS.getPlans().contains(p)){
                        if(!p.isActive()){
                            PlanFragment fg = PlanFragment.newInstance(p.getTime()+" min",p.getSubject(), p.getColor().toString(), p.getId());
                            fg.setClickInterface(this);
                            getFragmentManager().beginTransaction().add(plansLinear.getId(),fg, p.getId()).commit();
                        }else {
                            currentPlan = p;
                            updateSubjectName();
                            updateCountDownText();
                            updateCountDownPauseText(mTimeLeftPauseMillis);
                            updateCircleColor();
                            mTimeLeftMillis = currentPlan.getTime() * 60 * 1000;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void buttonClicked(String planID) {
        PlanFragment fg = PlanFragment.newInstance(currentPlan.getTime()+" min",currentPlan.getSubject(), currentPlan.getColor().toString(), currentPlan.getId());
        fg.setClickInterface(this);
        getFragmentManager().beginTransaction().add(plansLinear.getId(),fg, currentPlan.getId()).commit();
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(planID)).commit();
        dbHelper.updatePlanActive(planID, currentPlan.getId());
        for (Plan plan: DBHelper.USER_PLANS.getPlans()){
            if (plan.getId().equals(planID)){
                currentPlan = plan;
            }
        }
        mTimeLeftMillis = currentPlan.getTime() * 60 * 1000;
        resetTimer();
        updateSubjectName();
        updateCountDownPauseText(mTimeLeftPauseMillis);
        updateCountDownText();
        updateCircleColor();

    }
}
