package com.example.mattghall.finalproject;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class EditRouteActivity extends FragmentActivity {
    String FILENAME = "data_file";
    private JSONObject datatails = null;
    public String areaId;
    public String routeId;
    public boolean isNew;

    // TODO Use Dialog box for Anchors?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        areaId = intent.getExtras().getString("area-id");
        routeId = intent.getExtras().getString("route-id");
        isNew = intent.getExtras().getBoolean("isNew");
        datatails = ReadDaters();

        setContentView(R.layout.activity_edit_route);
    }

    private JSONObject ReadDaters()
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
            return data;

        }
        catch (Exception e)
        {
            ToastMachine("Could Not Load Saved Data. Go Ahead! Add some!");
            e.printStackTrace();
            return new JSONObject();
        }
    }

    List<ClimbingAreaClass> GetClimbingAreas() {
        List<ClimbingAreaClass> list = new ArrayList<>();
        String temp = "";
        JSONObject obj = null;
        int l = datatails.length();
        String[] areaNames = new String[l];

        try{
            for(int i = 0; i < l; i++)
            {
                temp = "area-" + String.valueOf(i);
                obj = datatails.getJSONObject(temp);
                temp = obj.getString("area-name");
                list.add(new ClimbingAreaClass(obj));
            }
        } catch (Exception e) {
            ToastMachine("Could not load climbing areas");
        }
        return list;
        }

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, msg, duration);
        toast.show();
    }

    public JSONObject GetDataTails()
    {
        return datatails;
    }
}
