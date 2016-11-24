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
public class Routes extends Fragment {

    public Routes() {
    }

    private String[] climbingAreaNames = { "Route A","Route B", "Route C","Route Error" };
    private String[] climbingAreaIds = { "0","1","2" };
    private ListView routesListView;
    private ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_routes,container,false);

        routesListView = (ListView) fragmentView.findViewById(R.id.routes_listview);

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, climbingAreaNames);
        routesListView.setAdapter(arrayAdapter);

        routesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
