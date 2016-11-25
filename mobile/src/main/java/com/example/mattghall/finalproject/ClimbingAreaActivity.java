package com.example.mattghall.finalproject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class ClimbingAreaActivity extends Fragment {

    public ClimbingAreaActivity() {
    }

    private String[] climbingAreaNames = { "Area A","Area B", "Area C","Area Error" };
    private String[] climbingAreaIds = { "0","1","2" };
    private ListView climbingAreaListView;
    private ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_climbing_areas,container,false);

        climbingAreaListView = (ListView) fragmentView.findViewById(R.id.areas_listview);

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, climbingAreaNames);
        climbingAreaListView.setAdapter(arrayAdapter);

        climbingAreaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(climbingAreaIds.length > position && climbingAreaNames.length > position) {
                    String msg = climbingAreaIds[position] + ": " + climbingAreaNames[position];
                    ToastMachine(msg);
                }
                else
                {
                    ToastMachine("Could not load Route");
                }
            }
        });
        return fragmentView;
    }

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), msg, duration);
        toast.show();
    }
}
