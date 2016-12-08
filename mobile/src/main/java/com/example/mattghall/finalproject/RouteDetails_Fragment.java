package com.example.mattghall.finalproject;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.LayoutRes;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mattghall.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetails_Fragment extends Fragment implements View.OnClickListener {
    JSONObject routeDetails = null;
    String FILENAME = "data_file";
    private ListView anchorListView;
    private ArrayAdapter arrayAdapter;
    RouteDetailsClass RDC;
    private boolean paused = false;
    DetailsActivity parentActivity;

    public RouteDetails_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the form and get all the daters and stuff
        parentActivity = (DetailsActivity) getActivity();
        routeDetails = parentActivity.GetDataTails();
        RDC = new RouteDetailsClass(routeDetails);

        // Thanks Android Studio Documentation for leaving me to figure this out completely on my own and not thinking to update your documentation at all
        // Set the binding
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_route__details,container,false);
        View eternalAnger = binding.getRoot();
        binding.setVariable(BR.RDC,RDC);
        binding.setVariable(BR.DataFile,routeDetails.toString());

        // Anchors and stuff
        anchorListView = (ListView) eternalAnger.findViewById(R.id.anchors_listview);
        anchorListView.setAdapter(new AnchorAdapter(this,RDC.anchors));

        // Set OnClickListeners
        final Button editButton = (Button) eternalAnger.findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
        final Button saveButton = (Button) eternalAnger.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        return eternalAnger;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.editButton :
                EditRoute();
                break;
            default:
                ToastMachine("ERRRRRROR");
                break;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(paused) {
            // Reload data in case RDC was changed
            paused = false;

            routeDetails = parentActivity.GetRoute(RDC.id, RDC.area);

            RDC = new RouteDetailsClass(routeDetails);

            RestartActivity();
        }
    }

    public void RestartActivity() {
        // Restart Activity
        getActivity().finish();
        Intent starter = getActivity().getIntent();
        starter.removeExtra("route");
        starter.putExtra("route", RDC.toString());
        startActivity(starter);
    }


    void EditRoute()
    {
        paused = true;
        Intent in = new Intent(getActivity(),EditRouteActivity.class);
        in.putExtra("isNew", false);
        in.putExtra("area-id",RDC.area);
        in.putExtra("route-id",RDC.id);
        startActivity(in);
    }

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), msg, duration);
        toast.show();
    }
}
