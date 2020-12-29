package com.example.cmprojeto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class DBHelper{

    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    String userID;

    public final static User USER = new User();
    public final static Map<String, Object> USER_SETTINGS = new HashMap<>();

    public DBHelper() {
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        populateDefaultSettings();
    }

    private static void populateDefaultSettings() {
        USER_SETTINGS.put("language", "EN");
        USER_SETTINGS.put("lightSensor", true);
        USER_SETTINGS.put("tempSensor", true);
        USER_SETTINGS.put("sessionStartNotification", true);
        USER_SETTINGS.put("studyStartNotification", true);
        USER_SETTINGS.put("studyBreakNotification", true);
        USER_SETTINGS.put("studyEndNotification", true);
        USER_SETTINGS.put("allowLocalization", true);
    }

    public void createUser(String username, String email, String password, Context context, Context appContext){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Activity) context, task -> {
            if(task.isSuccessful()) {
                FirebaseUser fUser = mAuth.getCurrentUser();
                assert fUser != null;
                userID = fUser.getUid();
                DocumentReference userReference = fstore.collection("users").document(userID);
                DocumentReference settingsReference = fstore.collection("settings").document(userID);

                Map<String, Object> user = new HashMap<>();
                user.put("username", username);
                user.put("email", email);
                user.put("description", " ");

                userReference.set(user).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess; user Profile is created for "+ userID));
                settingsReference.set(USER_SETTINGS).addOnSuccessListener(l -> Log.d(TAG, "onSuccess; Settings profile is created for " + userID));

                fUser.sendEmailVerification().addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, R.string.verify_email, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(appContext, LoginActivity.class);
                    context.startActivity(intent);
                }).addOnFailureListener(e -> Log.d(TAG, "OnFailure: "+ R.string.verify_email_not_sent + e.toString()));
            } else {
                if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    Toast.makeText(context, R.string.email_already_exists, Toast.LENGTH_SHORT).show();
                else if(task.getException() instanceof FirebaseAuthEmailException)
                    Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, R.string.reg_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkUsernameExists(String username, String email, String password, EditText editText, Context context, Context appContext, boolean login){
        fstore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isComplete()){
                    boolean checkUsernameExists = false;
                    for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                        if (document.contains("username")){
                            if (document.get("username").toString().equals(username)){
                                editText.setError(context.getResources().getString(R.string.username_exists));
                                checkUsernameExists = true;
                                break;
                            }
                        }
                    }

                    if(login){
                        if (!checkUsernameExists){
                            createUser(username, email, password, context, appContext);
                        }
                    }
                }
            }
        });
    }

    public void loginUser(String email, String password, Context context, Context appContext){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener((Activity) context, task -> {
            if(task.isSuccessful()){
                Toast.makeText(context, R.string.log_success, Toast.LENGTH_LONG).show();
                fstore.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isComplete()){
                            USER.setUsername(task.getResult().get("username").toString());
                            USER.setEmail(email);
                            USER.setPassword(password);
                            USER.setDescription(" ");
                            USER.setuID(mAuth.getCurrentUser().getUid());
                            System.out.println(USER.getUserInfo());
                        }
                    }
                });

                Intent intent = new Intent(appContext, HomeActivity.class);
                context.startActivity(intent);
            } else  {
                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(context, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                }else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(context, R.string.not_available_user, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, R.string.log_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkIfLogged(){
        return mAuth.getCurrentUser() != null;
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        USER.clearData();
    }

    public boolean emailVerified(){
        return !Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified();
    }

    public void resetPassword(String email, Context context){
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, R.string.password_link_sent, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, R.string.password_link_not_sent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void resendEmailVerification(Context context, Context appContext) {
        FirebaseUser fUser = mAuth.getCurrentUser();
        assert fUser != null;
        fUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, R.string.verify_email, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(appContext, LoginActivity.class);
                context.startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "OnFailure: "+ R.string.verify_email_not_sent + e.toString());
            }
        });
    }
}
