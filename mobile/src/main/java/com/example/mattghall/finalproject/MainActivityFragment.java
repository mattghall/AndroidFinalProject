package com.example.mattghall.finalproject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    private String[] monthsArray = { "Hi Sean","Hi Zak" };
    private ListView routesListView;
    private ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_main,container,false);

        routesListView = (ListView) fragmentView.findViewById(R.id.routes_listview);

        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, monthsArray);
        arrayAdapter = new ArrayAdapter(getContext(), R.layout.listview_routes, monthsArray);
        routesListView.setAdapter(arrayAdapter);
        return fragmentView;
    }
}
