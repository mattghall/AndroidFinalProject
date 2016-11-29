package com.example.mattghall.finalproject;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String routeDetails = intent.getStringExtra("route");


        RouteDetails_Fragment fragment = new RouteDetails_Fragment();
        Bundle buns = new Bundle();

        buns.putString("route",routeDetails);
        fragment.setArguments(buns);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.detail_frag, fragment);
        transaction.addToBackStack(null);
    }
}
