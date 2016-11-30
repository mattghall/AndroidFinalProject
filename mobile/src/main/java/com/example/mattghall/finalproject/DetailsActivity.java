package com.example.mattghall.finalproject;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends FragmentActivity {
    JSONObject datatails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        try {
            datatails = new JSONObject(intent.getStringExtra("route"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_details);
    }

    public JSONObject GetDataTails()
    {
        return datatails;
    }
}
