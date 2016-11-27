package com.example.mattghall.finalproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class AreasList_Fragment extends Fragment {

    public AreasList_Fragment() {
    }
    String newData = "{ \"area-0\": { \"area-id\": \"0\", \"area-name\": \"Boring Place Don't Go Here\", \"trailhead-name\": \"test-trailhead\", \"trailhead-gps\": \"000200020\", \"routes\": { \"route-0\": { \"route-id\": \"0\", \"route-name\": \"testrouteOne\", \"route-area\": \"area-0\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } }, \"route-1\": { \"route-id\": \"1\", \"route-name\": \"testroutetwo\", \"route-area\": \"area-0\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } } } }, \"area-1\": { \"area-id\": \"1\", \"area-name\": \"Party Area Fun Time\", \"trailhead-name\": \"test-trailhead\", \"trailhead-gps\": \"000200020\", \"routes\": { \"route-0\": { \"route-id\": \"0\", \"route-name\": \"route1party\", \"route-area\": \"area-0\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } }, \"route-1\": { \"route-id\": \"1\", \"route-name\": \"Yee ha this one is Guuuuuud\", \"route-area\": \"area-0\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } } } } }";


    private String[] climbingAreaNames = new String[0];
    private String[] defaultAreas = {"No Areas Found"};
    private String[] climbingAreaIds = new String[0];
    private String[] defaultIds = {"-1"};
    private ListView climbingAreaListView;
    private ArrayAdapter arrayAdapter;
    Context ctx;
    String FILENAME = "data_file";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx = getContext();
        //WriteNewDatersFile();

        View fragmentView = inflater.inflate(R.layout.fragment_climbing_areas, container, false);

        climbingAreaListView = (ListView) fragmentView.findViewById(R.id.areas_listview);

        final JSONObject data = ReadDaters();

        try {
            climbingAreaNames = GetClimbingAreas(data);
            climbingAreaIds = GetClimbingAreaIds(data);
        } catch (JSONException e) {
            e.printStackTrace();
            climbingAreaNames = defaultAreas;
            climbingAreaIds = defaultIds;
        }

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, climbingAreaNames);
        climbingAreaListView.setAdapter(arrayAdapter);

        climbingAreaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (climbingAreaIds.length > position && climbingAreaNames.length > position) {
                    String msg = climbingAreaIds[position] + ": " + climbingAreaNames[position];
                    String temp = "area-" + climbingAreaIds[position];
                    try {
                        OpenArea(climbingAreaIds[position],data.getJSONObject(temp));
                    } catch (JSONException e) {
                        ToastMachine("Could not load area");
                        e.printStackTrace();
                    }
                } else {
                    ToastMachine("Could not load area");
                }
            }
        });
        return fragmentView;
    }

    JSONObject ReadDaters()
    {
        FileInputStream fis = null;
        try{
            int n;
            fis = ctx.openFileInput(FILENAME);
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
            ToastMachine("ERRRRRRROR");
            e.printStackTrace();
            return new JSONObject();
        }
    }

    JSONObject GetArea(int areaId, JSONObject data) throws JSONException {
        String temp = "";
        JSONObject found;
        temp = "area-" + String.valueOf(areaId);
        found = data.getJSONObject(temp);
        return found;
    }

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), msg, duration);
        toast.show();
    }

    String [] GetClimbingAreas(JSONObject data) throws JSONException {
        String temp = "";
        JSONObject obj = null;
        int l = data.length();

        String[] areaNames = new String[l];

        for(int i = 0; i < l; i++)
        {
            temp = "area-" + String.valueOf(i);
            obj = data.getJSONObject(temp);
            temp = obj.getString("area-name");
            areaNames[i] = temp;
        }
        return areaNames;
    }

    String [] GetClimbingAreaIds(JSONObject data) throws JSONException {
        String temp = "";
        JSONObject obj = null;
        int l = data.length();

        String[] areaIds = new String[l];

        for(int i = 0; i < l; i++)
        {
            temp = "area-" + String.valueOf(i);
            obj = data.getJSONObject(temp);
            temp = obj.getString("area-id");
            areaIds[i] = temp;
        }

        return areaIds;
    }

    void OpenArea(String climbingAreaId, JSONObject area)
    {
        Bundle buns = new Bundle();
        buns.putString("area", area.toString());
        RouteList_Fragment routelist_fragment = new RouteList_Fragment();
        routelist_fragment.setArguments(buns);
        getFragmentManager().beginTransaction().add(R.id.fragment, routelist_fragment).commit();
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
