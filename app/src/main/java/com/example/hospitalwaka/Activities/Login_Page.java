package com.example.hospitalwaka.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalwaka.Network.AsynchronousResponse;
import com.example.hospitalwaka.Network.NetworkChecker;
import com.example.hospitalwaka.R;
import com.example.hospitalwaka.Sliders.sliderActivity;
import com.example.hospitalwaka.Util.WakaApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class Login_Page extends AppCompatActivity {
    TextInputEditText userEmail;
    TextInputEditText password;
    MaterialButton loginButton;
    TextView registerHereButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private String Username;
    private String userId;
    private Boolean NetworkFlag;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);
        // check for network connectivity
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
        {

            String userId = user.getUid();
            collectionReference.whereEqualTo("userId",userId).
                    addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for(QueryDocumentSnapshot snapshot :value)
                            {
                                String  username = snapshot.getString("username");
                                String  userId  = snapshot.getString("userId");
                                WakaApi.getInstance().setUsername(username);
                                WakaApi.getInstance().setUserId(userId);
                            }
                            startActivity(new Intent(Login_Page.this, MapsActivity.class));

                        }
                    });
        }




        new Thread(new Runnable() {
            @Override
            public void run() {
                new NetworkChecker().hasNetwork(new AsynchronousResponse() {
                    @Override
                    public void processFinished(boolean callBack) {
                        //Log.d("check", "processFinished: " + callBack);
                        NetworkFlag = callBack;
                    }
                });
            }
        }).start();

        // If no network create PopUp dialog
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                {
                    if (NetworkFlag) {


                    } else {
                        CreatePopUpDialog();
                    }
                }
            }
        }, 4000);
        // Check if user already sign in and move user to mapActivity


        userEmail = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.LoginButton);
        progressBar = findViewById(R.id.progressBar2);
        registerHereButton = findViewById(R.id.registerHereButton);
        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = Objects.requireNonNull(userEmail.getText()).toString().trim();
                String passwordText = Objects.requireNonNull(password.getText()).toString().trim();
                if (!Email.isEmpty() && !passwordText.isEmpty()) {
                    signIn(Email, passwordText);
                } else {
                    Toast.makeText(Login_Page.this, "Empty Field Not Required", Toast.LENGTH_SHORT).show();
                }


            }
        });

        registerHereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Page.this, Registration_Activity.class));
            }
        });


    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d("inTAG", "signInWithEmail:success");
                            Toast.makeText(Login_Page.this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String currentUser = user.getUid();
                            Toast.makeText(Login_Page.this, "Successful", Toast.LENGTH_LONG).show();
                            collectionReference.whereEqualTo("userId", currentUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    for (QueryDocumentSnapshot snapshot : value) {
                                        Username = snapshot.getString("username");
                                        userId = snapshot.getString("userId");
                                        WakaApi.getInstance().setUsername(Username);
                                        WakaApi.getInstance().setUserId(userId);
                                    }
                                }


                            });

                            startActivity(new Intent(Login_Page.this, MapsActivity.class));
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // If sign in fails, display a message to the user.
                           Log.w("errInTAG", "Email or password Incorrect", task.getException());
                            Toast.makeText(Login_Page.this, "User does not exist",
                                    Toast.LENGTH_LONG).show();



                        }

                        // ...
                    }
                });

    }

    private void CreatePopUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login_Page.this);
        View view = View.inflate(Login_Page.this, R.layout.network_err_layout, null);
        Button retryButton = view.findViewById(R.id.retryButton);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (NetworkFlag) {
            Toast.makeText(Login_Page.this, "CONNECTED", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            dialog.setCancelable(false);
            retryButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Login_Page.this, sliderActivity.class));
                }
            });


        }
      }
}