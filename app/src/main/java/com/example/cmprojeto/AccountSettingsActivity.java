package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.database.PasswordUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class AccountSettingsActivity extends AppCompatActivity {
    ImageView goBack;

    ImageButton btnImage;
    ImageView userImage;

    EditText emailField;
    EditText nameField;
    EditText passField;

    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        goBack = (ImageView) findViewById(R.id.goBack);

        btnImage = (ImageButton) findViewById(R.id.btnImage);
        userImage = (ImageView) findViewById(R.id.userImage);

        nameField = (EditText) findViewById((R.id.nameField));
        passField = (EditText) findViewById((R.id.passField));
        emailField = (EditText) findViewById(R.id.emailField);
        emailField.setEnabled(false);

        populateActivity();

        btnImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                userImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateActivity() {
        if(!DBHelper.USER.isPopulated()) {
            dbHelper.getUserInfo(user -> {
                DBHelper.USER.populateUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getDescription());
                fillControls(user.getEmail(), user.getUsername(), user.getPassword());
            });
        } else {
            fillControls(DBHelper.USER.getEmail(), DBHelper.USER.getUsername(), DBHelper.USER.getPassword());
        }
    }

    private void fillControls(String email, String username, String password) {
        emailField.setText(email);
        nameField.setText(username);

        try {
            passField.setText(PasswordUtils.decrypt(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}