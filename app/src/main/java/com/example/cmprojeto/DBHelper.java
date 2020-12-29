package com.example.cmprojeto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class DBHelper{
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;

    private static DBHelper instance;

    public static DBHelper getInstance() {
        if(instance == null)
            instance = new DBHelper();

        return instance;
    }

    private DBHelper() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public void createUser(String username, String email, String password, Context context, Context appContext){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Activity) context, task -> {
            if(task.isSuccessful()) {
                FirebaseUser fUser = mAuth.getCurrentUser();
                assert fUser != null;
                userID = fUser.getUid();
                DocumentReference userReference = fStore.collection("users").document(userID);

                Map<String, Object> user = new HashMap<>();
                user.put("username", username);
                user.put("email", email);
                user.put("description", " ");

                userReference.set(user).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess; user Profile is created for "+ userID));

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
        fStore.collection("users").get().addOnCompleteListener(task -> {
            if(task.isComplete()){
                boolean checkUsernameExists = false;
                for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
                    if (document.contains("username")){
                        if (Objects.requireNonNull(document.get("username")).toString().equals(username)){
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
        });
    }

    public void getUserInfo(UserCallback callback) {
        fStore.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get().addOnCompleteListener(task -> {
            if(task.isComplete()) {
                callback.manageUserInformation(
                        new UserInfo(Objects.requireNonNull(task.getResult().get("username")).toString(),
                        "password", Objects.requireNonNull(task.getResult().get("email")).toString(),
                        "description"));
            }
        });
    }

    public void getUserPlans(PlanCallback callback) {
        fStore.collection("plans").whereEqualTo("userID", Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                List<Plan> userPlans = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Color color = Color.toColor(Objects.requireNonNull(doc.get("color")).toString());
                    userPlans.add(
                            new Plan(
                                    Objects.requireNonNull(
                                    doc.get("subjectName")).toString(),
                                    Objects.requireNonNull(doc.get("description")).toString(),
                                    doc.getLong("time"),
                                    color,
                                    doc.getBoolean("active")));
                }

                callback.manageUserPlans(userPlans);
            }
        });
    }

    public void loginUser(String email, String password, Context context, Context appContext){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener((Activity) context, task -> {
            if(task.isSuccessful()){
                Toast.makeText(context, R.string.log_success, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(appContext, HomeActivity.class);
                context.startActivity(intent);
            } else {
                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(context, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                }else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(context, R.string.not_available_user, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(context, R.string.log_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkIfLogged() {
        return mAuth.getCurrentUser() != null;
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public boolean emailVerified(){
        return !Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified();
    }

    public void resetPassword(String email, Context context){
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.password_link_sent, Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, R.string.password_link_not_sent, Toast.LENGTH_SHORT).show();
        });
    }

    public void resendEmailVerification(Context context, Context appContext) {
        FirebaseUser fUser = mAuth.getCurrentUser();
        assert fUser != null;

        fUser.sendEmailVerification().addOnCompleteListener(task -> {
            Toast.makeText(context, R.string.verify_email, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(appContext, LoginActivity.class);
            context.startActivity(intent);
        }).addOnFailureListener(e -> Log.d(TAG, "OnFailure: "+ R.string.verify_email_not_sent + e.toString()));
    }
}
