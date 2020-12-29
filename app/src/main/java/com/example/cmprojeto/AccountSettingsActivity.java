package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cmprojeto.database.DBHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AccountSettingsActivity extends AppCompatActivity {
    ImageView goBack;

    ImageButton btnImage;
    ImageView userImage;

    EditText emailField;
    EditText nameField;

    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        goBack = (ImageView) findViewById(R.id.goBack);

        btnImage = (ImageButton) findViewById(R.id.btnImage);
        userImage = (ImageView) findViewById(R.id.userImage);

        nameField = (EditText) findViewById((R.id.nameField));
        emailField = (EditText) findViewById(R.id.emailField);
        emailField.setEnabled(false);

        dbHelper.getUserInfo(user -> {
            emailField.setText(user.getEmail());
            nameField.setText(user.getUsername());
        });

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
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                userImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}