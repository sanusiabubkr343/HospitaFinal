package com.example.hospitalwaka.Sliders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cuberto.liquid_swipe.LiquidPager;
import com.example.hospitalwaka.Activities.Login_Page;
import com.example.hospitalwaka.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class sliderActivity extends AppCompatActivity {
   LiquidPager pager;
    viewPagerAdapter viewPager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    CollectionReference collectionReference = db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        pager = findViewById(R.id.pager);
        viewPager = new viewPagerAdapter(getSupportFragmentManager(), 1);
        pager.setAdapter(viewPager);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(sliderActivity.this, Login_Page.class));
                finish();
            }
        }, 10000);

        // check for network connectivity



    }
}