package com.example.cmprojeto;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private Plan plan = new Plan("Artificial Intelligence", "Let's Study", 90, Color.RED, true);

    private TextView mTimerText;
    private TextView mTimerPauseText;

    private Button mStartStop;
    private Button mReset;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer mCountDownPauseTimer;

    private boolean mTimerRunning;

    private long mTimeLeftMillis = plan.getTime() * 60 * 1000;
    private long mTimeLeftPauseMillis = 15 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        mTimerText = findViewById(R.id.timer);
        mTimerPauseText = findViewById(R.id.pause);

        mStartStop = findViewById(R.id.b_start_pause);
        mReset = findViewById(R.id.b_reset);

        mStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning){
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                resetTimer();
            }
        });

        updateCountDownText();
        updateCountDownPauseText(15 * 1000);
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
                String time = minutes + ":" + seconds;
                Log.d("Cenas", "" + time);

                if (time.equals("89:50")){
                    pauseTimer();

                    mCountDownPauseTimer = new CountDownTimer(mTimeLeftPauseMillis, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mTimeLeftPauseMillis = millisUntilFinished;
                            updateCountDownPauseText(millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                            mTimeLeftMillis -= 1000;
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
