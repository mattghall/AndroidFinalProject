package com.example.mattghall.finalproject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditRoute_Fragment extends Fragment implements View.OnClickListener {
    JSONObject datera = null;
    JSONObject routeDetails = null;
    String FILENAME = "data_file";
    private ListView anchorListView;
    private ArrayAdapter arrayAdapter;
    EditRouteActivity parentActivity;
    List<ClimbingAreaClass> areas;

    RouteDetailsClass RDC;

    EditText titleEdit;
    EditText gpsEdit;
    EditText difficultyEdit;
    EditText anchorDifficultyEdit;
    EditText anchorBetaEdit;
    Spinner areaSpinner;
    TextView areaTextView;

    public EditRoute_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the form and get all the daters and stuff
        parentActivity = (EditRouteActivity) getActivity();
        datera = parentActivity.GetDataTails();
        areas = parentActivity.GetClimbingAreas();
        View view;

        // SEt view and load data if available
        if(parentActivity.isNew) {
            view = inflater.inflate(R.layout.fragment_edit_route, container, false);
        }
        else {
            routeDetails = GetRoute(datera);
            RDC = new RouteDetailsClass(routeDetails);

            // Thanks Android Studio Documentation for leaving me to figure this out completely on my own and not thinking to update your documentation at all
            // Set the binding
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_route, container, false);
            view = binding.getRoot();
            binding.setVariable(BR.RDC, RDC);
            binding.setVariable(BR.DataFile, routeDetails.toString());
        }

        // Set OnClickListeners
        final Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        final Button addAnchorButton = (Button) view.findViewById(R.id.addAnchorButton);
        addAnchorButton.setOnClickListener(this);

        // Reference lovely EditTexts
        titleEdit = (EditText)view.findViewById(R.id.routeTitle);
        gpsEdit = (EditText)view.findViewById(R.id.routeGPS);
        difficultyEdit = (EditText)view.findViewById(R.id.routeDifficulty);
        anchorDifficultyEdit = (EditText)view.findViewById(R.id.newAnchorDifficulty);
        anchorBetaEdit = (EditText)view.findViewById(R.id.newAnchorBeta);
        areaSpinner = (Spinner)view.findViewById(R.id.routeAreaSpinner);
        areaTextView = (TextView)view.findViewById(R.id.routeAreaTextView);

        // Anchors and stuff
        anchorListView = (ListView) view.findViewById(R.id.anchors_listview);


        // Special Bindings
        if(parentActivity.isNew) {
            // SEt title prompt
            titleEdit.setText(R.string.routeTitlePrompt);
            areaTextView.setVisibility(View.GONE);
            // bind properties of spinner to areas
            areaSpinner.setAdapter(new AreaAdapter(this, areas));
        }
        else
        {
            // Load anchors
            anchorListView.setAdapter(new AnchorAdapter(this, RDC.anchors));
            areaSpinner.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.saveButton :
                if(parentActivity.isNew){
                    SaveNewRoute();
                }
                else {
                    UpdateRouteDetails();
                }
                break;
            case R.id.addAnchorButton:
                AddNewAnchor(anchorDifficultyEdit.getText().toString(),anchorBetaEdit.getText().toString());
                break;
            default:
                ToastMachine("ERRRRRROR");
                break;
        }
    }

    private void SaveNewRoute() {
        RDC = new RouteDetailsClass();
        RDC.name = titleEdit.getText().toString();
        RDC.area = "area-" + ((ClimbingAreaClass)areaSpinner.getSelectedItem()).id;
        RDC.gps = gpsEdit.getText().toString();
        RDC.difficulty = difficultyEdit.getText().toString();
        AddNewRoute(RDC);
    }

    private void AddNewRoute(RouteDetailsClass rdc) {
        JSONObject oldData = ReadDaters();
        try {
            // Get route Area and Route ID
            String areaName = rdc.area;

            JSONObject area = oldData.getJSONObject(areaName);
            JSONObject routes = area.getJSONObject("routes");
            rdc.id = String.valueOf(routes.length());

            JSONObject newRouteDetails = rdc.GetJSON();
            routes.put(rdc.getFullId(),newRouteDetails);
            boolean suc = WriteNewDatersFile(oldData.toString());

            if(suc){
                ToastMachine("New Route Successfully Saved");
                parentActivity.finish();
                OpenRoute(RDC.GetJSON());
            }
            else {
                ToastMachine("Something went wrong");
            }

        } catch (JSONException e) {
            ToastMachine("ERROR");
            e.printStackTrace();
        }
    }

    private void UpdateRouteDetails() {
        RDC.name = titleEdit.getText().toString();
        RDC.gps = gpsEdit.getText().toString();
        RDC.difficulty = difficultyEdit.getText().toString();

        TrySaveData();
    }

    private void AddNewAnchor(String _difficulty, String _beta)
    {
        if(RDC == null)
        {
            ToastMachine("Please save route details before adding anchors");
        }
        else {
            RDC.AddAnchor(_difficulty, _beta);
            TrySaveData();
        }
    }

    public void TrySaveData(){
        try {
            JSONObject newRoute = RDC.GetJSON();
            SaveRoute(newRoute);
            RestartActivity();
        } catch (JSONException e) {
            ToastMachine("Could not Save Route");
            e.printStackTrace();
        }
    }

    private void RestartActivity() {
        // Restart Activity
        getActivity().finish();
        startActivity(getActivity().getIntent());
    }

    public void SaveRoute(JSONObject newRouteDetails)
    {
        JSONObject oldData = ReadDaters();
        try {
            // Get route Area and Route ID
            String areaName = newRouteDetails.getString("route-area");
            String routeId = "route-" + newRouteDetails.getString("route-id");

            JSONObject area = oldData.getJSONObject(areaName);
            JSONObject routes = area.getJSONObject("routes");
            routes.remove(routeId);
            routes.put(routeId,newRouteDetails);
            boolean suc = WriteNewDatersFile(oldData.toString());
            if(suc){
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
            fis = getContext().openFileInput(FILENAME);
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

    boolean WriteNewDatersFile(String newData) {
        FileOutputStream fos = null;
        try {
            fos = getContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.flush();
            fos.write(newData.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    JSONObject GetRoute(JSONObject dates)
    {
        try {
            JSONObject a = dates.getJSONObject(parentActivity.areaId);
            JSONObject r = a.getJSONObject("routes");
            a= r.getJSONObject("route-" + parentActivity.routeId);
            return a;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    void OpenRoute(JSONObject _route)
    {
        Intent in = new Intent(parentActivity,DetailsActivity.class);
        String putme = _route.toString();
        in.putExtra("route", putme);
        startActivity(in);
    }

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), msg, duration);
        toast.show();
    }
}
