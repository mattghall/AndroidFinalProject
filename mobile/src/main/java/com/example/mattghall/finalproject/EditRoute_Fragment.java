package com.example.mattghall.finalproject;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

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

    RouteDetailsClass RDC;

    EditText titleEdit;
    EditText gpsEdit;
    EditText difficultyEdit;
    EditText anchorDifficultyEdit;
    EditText anchorBetaEdit;
    EditText areaEdit;
    TextView areaTextView;

    public EditRoute_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the form and get all the daters and stuff
        parentActivity = (EditRouteActivity) getActivity();
        datera = parentActivity.GetDataTails();

        View eternalAnger;

        // SEt view and load data if available
        if(parentActivity.isNew) {
            eternalAnger = inflater.inflate(R.layout.fragment_edit_route, container, false);
        }
        else {
            routeDetails = GetRoute(datera);
            RDC = new RouteDetailsClass(routeDetails);

            // Thanks Android Studio Documentation for leaving me to figure this out completely on my own and not thinking to update your documentation at all
            // Set the binding
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_route, container, false);
            eternalAnger = binding.getRoot();
            binding.setVariable(BR.RDC, RDC);
            binding.setVariable(BR.DataFile, routeDetails.toString());
        }

        // Set OnClickListeners
        final Button saveButton = (Button) eternalAnger.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        final Button addAnchorButton = (Button) eternalAnger.findViewById(R.id.addAnchorButton);
        addAnchorButton.setOnClickListener(this);

        // Reference lovely EditTexts
        titleEdit = (EditText)eternalAnger.findViewById(R.id.routeTitle);
        gpsEdit = (EditText)eternalAnger.findViewById(R.id.routeGPS);
        difficultyEdit = (EditText)eternalAnger.findViewById(R.id.routeDifficulty);
        anchorDifficultyEdit = (EditText)eternalAnger.findViewById(R.id.newAnchorDifficulty);
        anchorBetaEdit = (EditText)eternalAnger.findViewById(R.id.newAnchorBeta);
        areaEdit = (EditText)eternalAnger.findViewById(R.id.routeAreaEditText);
        areaTextView = (TextView)eternalAnger.findViewById(R.id.routeAreaTextView);

        // Anchors and stuff
        anchorListView = (ListView) eternalAnger.findViewById(R.id.anchors_listview);


        // Special Bindings
        if(parentActivity.isNew) {
            // SEt title prompt
            titleEdit.setText(R.string.routeTitlePrompt);
            areaTextView.setVisibility(View.GONE);
        }
        else
        {
            // Load anchors
            anchorListView.setAdapter(new AnchorAdapter(this, RDC.anchors));
            areaEdit.setVisibility(View.GONE);
        }

        return eternalAnger;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.saveButton :
                UpdateRouteDetails();
                break;
            case R.id.addAnchorButton:
                AddNewAnchor(anchorDifficultyEdit.getText().toString(),anchorBetaEdit.getText().toString());
                break;
            default:
                ToastMachine("ERRRRRROR");
                break;
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

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), msg, duration);
        toast.show();
    }
}
