package com.example.cmprojeto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cmprojeto.database.DBHelper;
import com.example.cmprojeto.database.PasswordUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class AccountSettingsActivity extends AppCompatActivity {
    ImageView goBack;

    ImageButton btnImage;
    ImageView userImage;

    EditText emailField;
    EditText nameField;
    EditText passField;

    Button btnApply;

    DBHelper dbHelper = DBHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        goBack = (ImageView) findViewById(R.id.goBack);

        btnImage = (ImageButton) findViewById(R.id.btnImage);
        userImage = (ImageView) findViewById(R.id.userImage);

        btnApply = (Button) findViewById(R.id.applyButton);

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

        btnApply.setOnClickListener(v -> showConfirmationDialog());

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

                //TODO: Make sure selected image is below 1MB in size

                userImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadAvatar() {
        userImage.setDrawingCacheEnabled(true);
        userImage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) userImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] data = baos.toByteArray();
        dbHelper.uploadUserAvatar(data, getApplicationContext());
    }

    private void populateActivity() {
        if(!DBHelper.USER.isPopulated()) {
            dbHelper.getUserInfo(user -> {
                DBHelper.USER.populateUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getDescription(), user.getAvatar());
                fillControls(user.getEmail(), user.getUsername(), user.getPassword(), user.getAvatar());
            });
        } else {
            fillControls(DBHelper.USER.getEmail(), DBHelper.USER.getUsername(), DBHelper.USER.getPassword(), DBHelper.USER.getAvatar());
        }
    }

    private void fillControls(String email, String username, String password, byte[] avatar) {
        emailField.setText(email);
        nameField.setText(username);

        if(avatar != null)
            userImage.setImageBitmap(BitmapFactory.decodeByteArray(avatar, 0, avatar.length));

        try {
            passField.setText(PasswordUtils.decrypt(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        alert.setMessage("Enter current password");
        alert.setTitle("Confirm password");

        alert.setView(editText);
        alert.setPositiveButton(getResources().getString(R.string.submit), (dialog, whichButton) -> updateUserInformation(editText));
        alert.setNegativeButton(getResources().getString(R.string.cancel), (dialog, whichButton) -> dialog.cancel());
        alert.show();
    }

    private void updateUserInformation(EditText editText) {
        String passInput = editText.getText().toString();

        try {
            if(PasswordUtils.decrypt(DBHelper.USER.getPassword()).equals(passInput)) {
                String username = nameField.getText().toString();
                String password = passField.getText().toString();

                dbHelper.updateUserInformation(username, password, getApplicationContext());
                uploadAvatar();
                DBHelper.USER.clear();

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_conf_different), Toast.LENGTH_SHORT).show();
                populateActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}