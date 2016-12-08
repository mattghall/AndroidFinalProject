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

public class ActivityMain extends AppCompatActivity {
    JSONObject data;

    String FILENAME = "data_file";
    final String defaultData = "{\"area-0\":{\"area-id\":\"0\",\"area-name\":\"Vertical World\",\"trailhead-name\":\"Commodore Ave\",\"trailhead-gps\":\"0\",\"routes\":{\"route-0\":{\"route-id\":\"0\",\"route-name\":\"Kiddy Wall\",\"route-area\":\"area-0\",\"route-gps\":\"\",\"route-difficulty\":\"5-2\",\"route-img\":\"n\\/a\",\"anchors\":{\"anchor-0\":{\"anchor-num\":\"0\",\"anchor-difficulty\":\"5-2\",\"anchor-beta\":\"Clip from ground\"},\"anchor-1\":{\"anchor-num\":\"1\",\"anchor-difficulty\":\"5-3\",\"anchor-beta\":\"Climb the jugs to next hold\"},\"anchor-2\":{\"anchor-num\":\"2\",\"anchor-difficulty\":\"5-1\",\"anchor-beta\":\"Reach to your right and grab the achor\"}}},\"route-1\":{\"route-id\":\"1\",\"route-name\":\"The Cave\",\"route-area\":\"area-0\",\"route-gps\":\"\",\"route-difficulty\":\"5-12b\",\"route-img\":\"n\\/a\",\"anchors\":{\"anchor-0\":{\"anchor-num\":\"0\",\"anchor-difficulty\":\"5-11c\",\"anchor-beta\":\"12 feet of crimps\"},\"anchor-1\":{\"anchor-num\":\"1\",\"anchor-difficulty\":\"5-12a\",\"anchor-beta\":\"Reach for the red sloper with your right leg\"},\"anchor-2\":{\"anchor-num\":\"2\",\"anchor-difficulty\":\"5-14b\",\"anchor-beta\":\"Grow wings and fly up to this next clip\"}}}}},\"area-1\":{\"area-id\":\"1\",\"area-name\":\"The Far Side\",\"trailhead-name\":\"Exit 38\",\"trailhead-gps\":\"0\",\"routes\":{\"route-0\":{\"route-id\":\"0\",\"route-name\":\"Beautiful Rock\",\"route-area\":\"area-1\",\"route-gps\":\"\",\"route-difficulty\":\"5-9\",\"route-img\":\"n\\/a\",\"anchors\":{\"anchor-0\":{\"anchor-num\":\"0\",\"anchor-difficulty\":\"5-8\",\"anchor-beta\":\"Work your way up the jugs to the first bolt\"},\"anchor-1\":{\"anchor-num\":\"1\",\"anchor-difficulty\":\"5-9\",\"anchor-beta\":\"Use the ledge on your left to help prop you up to the next hold\"},\"anchor-2\":{\"anchor-num\":\"2\",\"anchor-difficulty\":\"5-10a\",\"anchor-beta\":\"There's a hole just big enough for two fingers up and to the left. Use that to pull yourself up to the sidebar.\"},\"anchor-3\":{\"anchor-num\":\"3\",\"anchor-difficulty\":\"5-7\",\"anchor-beta\":\"Two easy buckets\"},\"anchor-4\":{\"anchor-num\":\"4\",\"anchor-difficulty\":\"5-8\",\"anchor-beta\":\"There's a permi-biner to tie off so make sure it looks good before you clean\"}}}}}}";

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
        Intent in = new Intent(this,ActivityEdit.class);
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
                AreaClass newArea = new AreaClass(areaNameEdit.getText().toString(),areaTrailheadEdit.getText().toString(),areaTrailheadGPS.getText().toString());
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

    public void SaveArea(AreaClass newRouteDetails)
    {
        JSONObject oldData = ReadDaters();
        try {
            newRouteDetails.id = String.valueOf(oldData.length());
            String areaId = "area-" + newRouteDetails.id;

            oldData.put(areaId,newRouteDetails.GetJSON());
            boolean suc = WriteNewDatersFile(oldData.toString());
            if(suc){
                RestartActivity();
                ToastMachine(getResources().getString(R.string.saveSuccess));
            }
            else {
                ToastMachine(getResources().getString(R.string.genericErrorMessage));
            }

        } catch (JSONException e) {
            ToastMachine(getResources().getString(R.string.genericErrorMessage));
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
            ToastMachine(getResources().getString(R.string.noLoadSavedDataMessage));
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
