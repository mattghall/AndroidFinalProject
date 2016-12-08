package com.example.mattghall.finalproject;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

public class ActivityDetails extends FragmentActivity {
    JSONObject datatails = null;
    String FILENAME = "data_file";

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

    private void ReadDaters()
    {
        FileInputStream fis = null;
        try{
            int n;
            fis = openFileInput(FILENAME);
            StringBuffer fileContent = new StringBuffer("");
            byte[] buffer = new byte[1024];

            while ((n = fis.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }

            JSONObject data = new JSONObject(String.valueOf(fileContent));
            datatails = data;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    JSONObject GetRoute(String routeId, String areaId)
    {
        try {
            ReadDaters();
            JSONObject a = datatails.getJSONObject(areaId);
            JSONObject r = a.getJSONObject("routes");
            a= r.getJSONObject("route-" + routeId);
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public JSONObject GetDataTails()
    {
        return datatails;
    }
}
