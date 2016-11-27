package com.example.mattghall.finalproject;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mattghall.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetails_Fragment extends Fragment {


    public RouteDetails_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route__details, container, false);
    }

}
