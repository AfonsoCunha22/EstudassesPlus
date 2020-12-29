package com.example.cmprojeto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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
    public final static List<Plan> plans = new ArrayList<>();


    public DBHelper() {

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

    }

    public void createUser(String username, String email, String password, Context context, Context appContext){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser fuser = mAuth.getCurrentUser();
                    assert fuser != null;
                    userID = fuser.getUid();
                    DocumentReference documentReference = fstore.collection("users").document(userID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("email", email);
                    user.put("description", " ");
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess; user Profile is created for "+ userID);
                        }
                    });

                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
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
                }else  {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(context, R.string.email_already_exists, Toast.LENGTH_SHORT).show();
                    }else if(task.getException() instanceof FirebaseAuthEmailException){
                        Toast.makeText(context, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(context, R.string.reg_error, Toast.LENGTH_SHORT).show();
                }
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
                loadUser();
                Intent intent = new Intent(appContext, HomeActivity.class);
                context.startActivity(intent);
            }else  {
                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(context, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                }else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(context, R.string.not_available_user, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, R.string.log_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadPlans(){
        fstore.collection("plans").get().addOnCompleteListener(task -> {
            if(task.isComplete()){
                for (DocumentSnapshot doc: task.getResult().getDocuments()) {
                    if(USER.getuID().equals(doc.getString("userID"))){
                        Color color;
                        switch (doc.get("color").toString()){
                            case "#FF0000": color = Color.RED;
                            case "#0000FF": color = Color.BLUE;
                            case "#A52A2A": color = Color.BROWN;
                            case "#00FF00": color = Color.GREEN;
                            case "#FFFF00": color = Color.YELLOW;
                            default: color = Color.RED;
                        }

                        plans.add(new Plan(doc.get("subjectName").toString(),doc.get("description").toString(), doc.getLong("time"),color,doc.getBoolean("active")));
                    }
                }
            }
        });
    }

    public boolean checkIfLogged(){
        Log.i("LOG", "Dentro do checkIfLogged");
        return mAuth.getCurrentUser() != null;
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        USER.clearData();
    }

    public void loadUser(){
        fstore.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    USER.setUsername(task.getResult().get("username").toString());
                    USER.setEmail(task.getResult().get("email").toString());
                    USER.setDescription(" ");
                    USER.setuID(mAuth.getCurrentUser().getUid());
                    loadPlans();
                }
            }
        });
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

    public User getUSER(){
        return USER;
    }
}
