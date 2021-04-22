package com.example.hospitalwaka.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalwaka.R;
import com.example.hospitalwaka.Util.WakaApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registration_Activity extends AppCompatActivity {
    private static final String TAG = "myTAG";
    TextInputEditText UsernameText;
    TextInputEditText emailText;
    TextInputEditText passwordField;
    MaterialButton RegisterButton;
    TextView goBacktext;
    ProgressBar progressBar;

    TextInputLayout passwordLayout;
    TextInputLayout emailLayout;
    TextInputLayout usernameLayout;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Users = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);
        mAuth = FirebaseAuth.getInstance();


        passwordField = findViewById(R.id.passwordField);
        emailText = findViewById(R.id.emailText);
        UsernameText = findViewById(R.id.UsernameText);
        RegisterButton = findViewById(R.id.RegisterButton);
        goBacktext = findViewById(R.id.goBackLogin);
        progressBar = findViewById(R.id.progressBar);

        passwordLayout = findViewById(R.id.passwordLayout);
        emailLayout = findViewById(R.id.emailLayout);
        usernameLayout = findViewById(R.id.usernameLayout);
        progressBar.setVisibility(View.INVISIBLE);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String username = Objects.requireNonNull(UsernameText.getText()).toString().trim();
                String password = Objects.requireNonNull(passwordField.getText()).toString().trim();
                String email = Objects.requireNonNull(emailText.getText()).toString().trim();


                passwordLayout.setError(null);
                usernameLayout.setError(null);
                emailLayout.setError(null);


                if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {

                    passwordLayout.setError(null);
                    usernameLayout.setError(null);
                    emailLayout.setError(null);
                    SignUp(username, email, password);


                } else {
                    if (username.isEmpty()) {
                        usernameLayout.setError("Error");
                    }
                    if (password.isEmpty()) {
                        passwordLayout.setError("Error");
                    }

                    if (email.isEmpty()) {
                        emailLayout.setError("Error");
                    }
                    Toast.makeText(Registration_Activity.this, "Empty field not required", Toast.LENGTH_SHORT).show();


                }

            }
        });
        goBacktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration_Activity.this, Login_Page.class));
            }
        });

    }


    private void SignUp(String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.VISIBLE);
                            // make it return  to login page the automatically sends it to next activity
                            startActivity(new Intent(Registration_Activity.this, Login_Page.class));
                            Toast.makeText(Registration_Activity.this, "Registration Successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Map<String, String> data = new HashMap<>();
                            data.put("username", username);
                            assert user != null;
                            data.put("userId", user.getUid());
                            Users.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) { /// then get the uploaded data from the server
                                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            final String username = Objects.requireNonNull(task.getResult()).getString("username");
                                            final String userId = task.getResult().getString("userId");

                                            WakaApi api = WakaApi.getInstance();
                                            api.setUsername(username);
                                            api.setUserId(userId);

                                         //   Log.d("myTAG", "onComplete: " + "userName: " + api.getUsername() + ","
                                                //    + "userId :" + api.getUserId());

                                        }

                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(Registration_Activity.this, "Email Verification Failed." ,
                                    Toast.LENGTH_LONG).show();
                          //  Log.d("innerTag", "onComplete: " + task.getException());

                        }

                        // ...
                    }
                });


    }
}