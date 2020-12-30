package com.example.cmprojeto.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.cmprojeto.HomeActivity;
import com.example.cmprojeto.LoginActivity;
import com.example.cmprojeto.R;
import com.example.cmprojeto.callbacks.BooleanCallback;
import com.example.cmprojeto.callbacks.PlanCallback;
import com.example.cmprojeto.callbacks.SessionCallback;
import com.example.cmprojeto.callbacks.UserCallback;
import com.example.cmprojeto.model.Color;
import com.example.cmprojeto.model.Plan;
import com.example.cmprojeto.model.Session;
import com.example.cmprojeto.model.SubjectList;
import com.example.cmprojeto.model.UserInfo;
import com.example.cmprojeto.model.UserPlans;
import com.example.cmprojeto.model.UserSessions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class DBHelper{
    public static final UserInfo USER = new UserInfo();
    public static final UserPlans USER_PLANS = new UserPlans();
    public static final UserSessions USER_SESSIONS = new UserSessions();
    public static final SubjectList SUBJECT_LIST = new SubjectList();

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

    public void createUser(UserInfo userInfo, Context context, Context appContext){
        mAuth.createUserWithEmailAndPassword(userInfo.getEmail(), userInfo.getPassword()).addOnCompleteListener((Activity) context, task -> {
            if(task.isSuccessful()) {
                FirebaseUser fUser = mAuth.getCurrentUser();
                assert fUser != null;
                userID = fUser.getUid();
                DocumentReference userReference = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("username", userInfo.getUsername());
                user.put("email", userInfo.getEmail());
                user.put("description", userInfo.getDescription());

                try {
                    user.put("password", PasswordUtils.encrypt(userInfo.getPassword()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

    public void createSession(String subject, Date date, int hours,int minute, Double latitude,Double longitude, String description){
        Map<String, Object> session = new HashMap<>();
        session.put("userID", mAuth.getCurrentUser().getUid());
        session.put("subject", subject);
        session.put("date", date);
        session.put("hours", hours);
        session.put("minutes", minute);
        session.put("latitude", latitude);
        session.put("longitude", longitude);
        session.put("description", description);

        fStore.collection("sessions").add(session);
    }

    public void checkUsernameExists(String username, BooleanCallback callback) {
        fStore.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
            if(task.isComplete())
                callback.exists(task.getResult().getDocuments().size() != 0);
        });
    }

    public void getUserInfo(UserCallback callback) {
        fStore.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get().addOnCompleteListener(task -> {
            if(task.isComplete()) {
                callback.manageUserInformation(
                        new UserInfo(Objects.requireNonNull(task.getResult().get("username")).toString(),
                                Objects.requireNonNull(task.getResult().get("password")).toString(),
                                Objects.requireNonNull(task.getResult().get("email")).toString(),
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

    public void getSessions(SessionCallback callback) {
        fStore.collection("session").get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                List<Session> sessions = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    sessions.add(
                            new Session(
                                    doc.getString("subjectName"),
                                    doc.getDate("date"),
                                    new Time(doc.getDouble("hour").intValue(),doc.getDouble("minute").intValue(),0),
                                    new LatLng(doc.getDouble("latitude"),doc.getDouble("latitude")),
                                    Objects.requireNonNull(doc.get("description")).toString()));
                }

                callback.manageUserSession(sessions);
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
                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    Toast.makeText(context, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                else if(task.getException() instanceof FirebaseAuthInvalidUserException)
                    Toast.makeText(context, R.string.not_available_user, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, R.string.log_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkIfLogged() {
        return mAuth.getCurrentUser() != null;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public boolean emailVerified() {
        return !Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified();
    }

    public void resetPassword(String email, Context context) {
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

    public void createPlan(Plan plan){
        Map<String, Object> planMap = new HashMap<>();
        planMap.put("subjectName", plan.getSubject());
        planMap.put("description", plan.getDescription());
        planMap.put("time", plan.getTime());
        planMap.put("userID", mAuth.getCurrentUser().getUid());
        planMap.put("color", plan.getColor().toString());
        planMap.put("active", plan.isActive());

        fStore.collection("plans").add(planMap);
    }

}
