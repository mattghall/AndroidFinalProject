package com.example.mattghall.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    Context ctx;
    String FILENAME = "data_file";
    String newData = "{ \"area-0\": { \"area-id\": \"0\", \"area-name\": \"Boring Place Don't Go Here\", \"trailhead-name\": \"test-trailhead\", \"trailhead-gps\": \"000200020\", \"routes\": { \"route-0\": { \"route-id\": \"0\", \"route-name\": \"testrouteOne\", \"route-area\": \"area-0\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } }, \"route-1\": { \"route-id\": \"1\", \"route-name\": \"testroutetwo\", \"route-area\": \"area-0\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } } } }, \"area-1\": { \"area-id\": \"1\", \"area-name\": \"Party Area Fun Time\", \"trailhead-name\": \"test-trailhead\", \"trailhead-gps\": \"000200020\", \"routes\": { \"route-0\": { \"route-id\": \"0\", \"route-name\": \"route1party\", \"route-area\": \"area-0\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } }, \"route-1\": { \"route-id\": \"1\", \"route-name\": \"Yee ha this one is Guuuuuud\", \"route-area\": \"area-0\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } } } } }";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Data file updated", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                WriteNewDatersFile();
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

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
        toast.show();
    }

    void WriteNewDatersFile() {
        FileOutputStream fos = null;
        try {
            fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.flush();
            fos.write(newData.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
