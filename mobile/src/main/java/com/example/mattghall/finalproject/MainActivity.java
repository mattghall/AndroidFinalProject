package com.example.mattghall.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    JSONObject data;

    String FILENAME = "data_file";
    final String defaultData = "{ \"area-0\": { \"area-id\": \"0\", \"area-name\": \"Sweet Climbin\", \"trailhead-name\": \"test-trailhead\", \"trailhead-gps\": \"000200020\", \"routes\": { \"route-0\": { \"route-id\": \"0\", \"route-name\": \"Fun Route\", \"route-area\": \"area-0\", \"route-gps\": \"000000\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"reach for the rock\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"dino to the finish\" } } }, \"route-1\": { \"route-id\": \"1\", \"route-name\": \"Sucky Route\", \"route-area\": \"area-0\", \"route-gps\": \"000000\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-10d\", \"anchor-beta\": \"first clip's a dino\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-10c\", \"anchor-beta\": \"second clip's a dino\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-11a\", \"anchor-beta\": \"wait, this one's also a dino\" }, \"anchor-3\": { \"anchor-num\": \"3\", \"anchor-difficulty\": \"5-10b\", \"anchor-beta\": \"wait, this one's also a dino\" }, \"anchor-4\": { \"anchor-num\": \"4\", \"anchor-difficulty\": \"5-10b\", \"anchor-beta\": \"All the clips are dinos... :/\" }, \"anchor-4\": { \"anchor-num\": \"4\", \"anchor-difficulty\": \"5-12b\", \"anchor-beta\": \"Why am I evn climbing this? MAybe I should start to rethink my activities. Maybe I'll take out a safer sport like... knitting? Knitting is cool right?\" } , \"anchor-5\": { \"anchor-num\": \"5\", \"anchor-difficulty\": \"5-12b\", \"anchor-beta\": \"GOOD NEWS! I have officially learned how to knit and have joined the knitting club!\" } } } } }, \"area-1\": { \"area-id\": \"1\", \"area-name\": \"Vertical World\", \"trailhead-name\": \"Commodore Ave\", \"trailhead-gps\": \"000200020\", \"routes\": { \"route-0\": { \"route-id\": \"0\", \"route-name\": \"The Crack\", \"route-area\": \"area-1\", \"route-gps\": \"000000\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-13\", \"anchor-beta\": \"this is a hard clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-19\", \"anchor-beta\": \"this is not a clip\" } } }, \"route-1\": { \"route-id\": \"1\", \"route-name\": \"Slabin it\", \"route-area\": \"area-1\", \"route-gps\": \"000000\", \"route-difficulty\": \"5-11c\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-6\", \"anchor-beta\": \"this is an easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-7\", \"anchor-beta\": \"this is also an easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-4\", \"anchor-beta\": \"I literally walked to this one\" } } } } } }";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addAreaFab = (FloatingActionButton) findViewById(R.id.addAreaFab);
        addAreaFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewArea();
            }
        });

        FloatingActionButton addRouteFab = (FloatingActionButton) findViewById(R.id.addRouteFab);
        addRouteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewRoute();
            }
        });

        FloatingActionButton resetFab = (FloatingActionButton) findViewById(R.id.resetFab);
        resetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteDefaultDaters();
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void WriteDefaultDaters() {
        FileOutputStream fos = null;
        try {
            fos = getBaseContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.flush();
            fos.write(defaultData.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
        toast.show();
    }

    void CreateNewRoute()
    {
        Intent in = new Intent(this,EditRouteActivity.class);
        in.putExtra("isNew", true);
        startActivity(in);
    }

    void CreateNewArea()
    {
        // Create Object of Dialog class
        final Dialog anchorDialog = new Dialog(this);
        // Set GUI of login screen
        anchorDialog.setContentView(R.layout.dialog_area);

        // Init button of login GUI
        Button saveButton = (Button) anchorDialog.findViewById(R.id.btnSave);
        Button btnCancel = (Button) anchorDialog.findViewById(R.id.btnCancel);
        final EditText areaNameEdit = (EditText)anchorDialog.findViewById(R.id.newAreaName);
        final EditText areaTrailheadEdit = (EditText)anchorDialog.findViewById(R.id.newAreaTrailheadName);
        final EditText areaTrailheadGPS = (EditText)anchorDialog.findViewById(R.id.newAreaTrailheadGPS);

        // Attached listener for login GUI button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add area
                ClimbingAreaClass newArea = new ClimbingAreaClass(areaNameEdit.getText().toString(),areaTrailheadEdit.getText().toString(),areaTrailheadGPS.getText().toString());
                SaveArea(newArea);
                anchorDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anchorDialog.dismiss();
            }
        });

        // Make dialog box visible.
        anchorDialog.show();

    }

    public void SaveArea(ClimbingAreaClass newRouteDetails)
    {
        JSONObject oldData = ReadDaters();
        try {
            newRouteDetails.id = String.valueOf(oldData.length());
            String areaId = "area-" + newRouteDetails.id;

            oldData.put(areaId,newRouteDetails.GetJSON());
            boolean suc = WriteNewDatersFile(oldData.toString());
            if(suc){
                RestartActivity();
                ToastMachine("New Data Successfully Saved");
            }
            else {
                ToastMachine("Something went wrong");
            }

        } catch (JSONException e) {
            ToastMachine("ERROR");
            e.printStackTrace();
        }
    }

    JSONObject ReadDaters()
    {
        FileInputStream fis = null;
        try{
            int n;
            fis = this.openFileInput(FILENAME);
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
            ToastMachine("ERRRRRRROR Could not load saved data file");
            e.printStackTrace();
            return new JSONObject();
        }
    }

    boolean WriteNewDatersFile(String _newData) {
        FileOutputStream fos = null;
        try {
            fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.flush();
            fos.write(_newData.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void RestartActivity() {
        // Restart Activity
        finish();
        startActivity(getIntent());
    }
}
