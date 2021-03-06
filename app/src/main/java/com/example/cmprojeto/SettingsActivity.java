package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.database.PasswordUtils;

public class SettingsActivity extends AppCompatActivity {
    TextView userName;
    ImageView goBack, userImage;
    Button btnAccount;
    Button btnNotifications, btnSensors, btnLanguage, btnHelp;

    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        goBack = (ImageView) findViewById(R.id.goBack);
        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);

        btnAccount = (Button) findViewById(R.id.btnAccount);
        btnNotifications = (Button) findViewById(R.id.btnNotification);
        btnSensors = (Button) findViewById(R.id.btnSensors);
        btnLanguage = (Button) findViewById(R.id.btnLanguage);
        btnHelp = (Button) findViewById(R.id.btnHelp);

        userImage.setImageResource(R.drawable.avatar_default);
        populateActivity();

        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);
            startActivity(intent);
        });

        btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NotificationSettingsActivity.class);
            startActivity(intent);
        });

        btnSensors.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SensorSettingsActivity.class);
            startActivity(intent);
        });

        btnLanguage.setOnClickListener( v -> {
            Intent intent = new Intent(getApplicationContext(), LanguageSettingsActivity.class);
            startActivity(intent);
        });

        btnHelp.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HelpSettingsActivity.class);
            startActivity(intent);
        });

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });
    }

    private void populateActivity() {
        if(!DBHelper.USER.isPopulated()) {
            dbHelper.getUserInfo(user -> {
                DBHelper.USER.populateUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getDescription(), user.getAvatar());
                fillControls(user.getUsername(), user.getAvatar());
            });
        } else {
            fillControls(DBHelper.USER.getUsername(), DBHelper.USER.getAvatar());
        }
    }

    private void fillControls(String username, byte[] avatar) {
        userName.setText(username);

        if(avatar != null)
            userImage.setImageBitmap(BitmapFactory.decodeByteArray(avatar, 0, avatar.length));
    }
}