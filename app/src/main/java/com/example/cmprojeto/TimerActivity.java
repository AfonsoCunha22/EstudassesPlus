package com.example.cmprojeto;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorStateListDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.example.cmprojeto.model.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity implements FragmentClick, SensorEventListener {
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

    //Sensors
    private SensorManager sensorManager;
    private Toast warning;
    private boolean isWorking;

    private boolean mTimerRunning;

    SharedPreferences preferences;
    SharedPreferences sensorPreferences;

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
        isWorking=false;
        mStartStop = (Button) findViewById(R.id.b_start_pause);
        mReset = (Button) findViewById(R.id.b_reset);
        mDescription = (Button) findViewById(R.id.b_description);
        mCreatePlan = (Button) findViewById(R.id.createPlanBtn);
        circleView = (View) findViewById(R.id.circleView);

        plansLinear = (LinearLayout) findViewById(R.id.plansLinear);

        preferences = getSharedPreferences("notificationSettings", MODE_PRIVATE);
        //Sensors Initiating
        sensorPreferences = getSharedPreferences("sensorSettings", MODE_PRIVATE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        warning = new Toast(this);

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

        createNotificationChannel();
        populateActivity();
    }

    private void resetTimer() {
        mTimeLeftMillis = currentPlan.getTime() * 60 * 1000;
        updateCountDownText();
        mReset.setEnabled(false);
        mStartStop.setEnabled(true);
    }

    private void startTimer() {
        isWorking=true;
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
                    if(preferences.getBoolean("studyBreak",true)){
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(TimerActivity.this, "Estudasses+")
                                .setSmallIcon(R.drawable.icon)
                                .setContentTitle("Break time!")
                                .setContentText(getText(R.string.break_notif))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(TimerActivity.this);
                        notificationManager.notify(100,builder.build());
                        isWorking=false;
                    }
                }
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mStartStop.setText(getResources().getString(R.string.start));
                mStartStop.setEnabled(false);
                mReset.setEnabled(true);
                if(preferences.getBoolean("studyEnd",true)){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(TimerActivity.this,"estudasses+")
                            .setSmallIcon(R.drawable.icon)
                            .setContentTitle("Plan has ended!")
                            .setContentText(getText(R.string.endStudy_notif))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(TimerActivity.this);
                    notificationManager.notify(100,builder.build());
                    isWorking=false;
                }
            }
        }.start();
        if(preferences.getBoolean("studyStart",true)){
            System.out.println("Entrei aqui rapazes");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(TimerActivity.this, "estudasses+")
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("Work time!")
                    .setContentText(getText(R.string.study_notif) + " " + mSubjectText.getText().toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(100,builder.build());


        }

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
        isWorking=false;
    }

    private void resetPauseTimer(){
        mTimeLeftPauseMillis = 10 * 60 * 1000;
        mReset.setEnabled(true);
        mStartStop.setEnabled(true);
        isWorking=true;
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
        System.out.println(currentPlan.getColor().toString());
        String x = "";
        x = currentPlan.getColor().toString().substring(0, 1) + "FF" + currentPlan.getColor().toString().substring(1, currentPlan.getColor().toString().length());
        System.out.println(x);
        drawable.setStroke(1,Color.parseColor(x));
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

    public void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String name = "plansChannel";
            String description = "Channel for plans notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("estudasses+", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    public void buttonClicked(String planID) {
        if(mCountDownTimer!=null){
            pauseTimer();
        }

        resetTimer();
        if(currentPlan!=null){
            PlanFragment fg = PlanFragment.newInstance(currentPlan.getTime()+" min",currentPlan.getSubject(), currentPlan.getColor().toString(), currentPlan.getId());
            fg.setClickInterface(this);
            getFragmentManager().beginTransaction().add(plansLinear.getId(),fg, currentPlan.getId()).commit();
            dbHelper.updatePlanActive(planID, currentPlan.getId(), callback ->{
                if (callback) {
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(planID)).commit();
                    updateCurrentPlan(planID);
                }
            });
        }else {
            dbHelper.updatePlanActive(planID, null, callback ->{
                if (callback) {
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(planID)).commit();
                    updateCurrentPlan(planID);
                }
            });
        }




    }

    public void updateCurrentPlan(String planID){
        for (Plan plan: DBHelper.USER_PLANS.getPlans()){
            if (plan.getId().equals(planID)){
                currentPlan = plan;
                mTimeLeftMillis = currentPlan.getTime() * 60 * 1000;
                resetTimer();
                updateSubjectName();
                updateCountDownPauseText(mTimeLeftPauseMillis);
                updateCountDownText();
                updateCircleColor();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lighting = event.values[0];
            if ((lighting < 80) && isWorking) {
                showAToast("Room too dark !");
            } else if ((lighting > 25000) && isWorking) {
                showAToast("Room too bright!");
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float movement = event.values[0];
            if ((Math.abs(movement) > 1)&&isWorking) {
                showAToast("Its not break time! Don't touch your phone and keep studying!");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void showAToast (String st){
        try{ warning.getView().isShown();
            warning.setText(st);
        } catch (Exception e) {
            warning = Toast.makeText(this, st, Toast.LENGTH_SHORT);
        }
        warning.show();
    }
    private void loadAmbientLight() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "No Light Sensor !", Toast.LENGTH_LONG).show();
        }
    }
    private void loadAccelerometer() {
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "No accelerometer Sensor !", Toast.LENGTH_LONG).show();
        }
    }
    private void unregisterAll() {
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(sensorPreferences.getBoolean("lightSwitch",false)){
            loadAmbientLight();
        }
        if(sensorPreferences.getBoolean("accelerometerSwitch",false)){
            loadAccelerometer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterAll();
    }
}
