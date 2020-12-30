package com.example.cmprojeto;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmprojeto.model.Color;
import com.example.cmprojeto.model.Plan;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    //TODO Import the active plan from the database
    private Plan plan = new Plan(new Subject("CM"), "Let's Study", 90, Color.RED, true);

    private TextView mTimerText;
    private TextView mTimerPauseText;

    private Button mStartStop;
    private Button mReset;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer mCountDownPauseTimer;

    private boolean mTimerRunning;

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

        mStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper();
                dbHelper.createPlan(new Subject("CM"), 90, Color.RED, "Let's Study");
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
                mStartStop.setText("Start");
                mStartStop.setEnabled(false);
                mReset.setEnabled(true);
            }
        }.start();

        mTimerRunning = true;
        mStartStop.setText("Pause");
        mReset.setEnabled(false);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mStartStop.setText("Start");
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
}
