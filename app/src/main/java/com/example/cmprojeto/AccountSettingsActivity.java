package com.example.cmprojeto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AccountSettingsActivity extends AppCompatActivity {
    DrawerLayout drawer;
    ImageView openMenu;

    ImageButton btnImage;
    ImageView userImage;

    EditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        openMenu = (ImageView) findViewById(R.id.openMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(Gravity.LEFT);

        btnImage = (ImageButton) findViewById(R.id.btnImage);
        userImage = (ImageView) findViewById(R.id.userImage);

        emailField = (EditText) findViewById(R.id.emailField);
        emailField.setEnabled(false);

        btnImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        openMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
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