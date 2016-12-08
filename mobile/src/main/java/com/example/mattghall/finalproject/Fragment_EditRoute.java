package com.example.mattghall.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class Fragment_EditRoute extends Fragment implements View.OnClickListener {
    JSONObject datera = null;
    JSONObject routeDetails = null;
    String FILENAME = "data_file";
    private ListView anchorListView;
    private ArrayAdapter arrayAdapter;
    EditRouteActivity parentActivity;
    List<AreaClass> areas;

    RouteDetailsClass RDC;

    EditText titleEdit;
    EditText gpsEdit;
    EditText difficultyEdit;
    EditText anchorBetaEdit;
    Spinner areaSpinner;
    TextView areaTextView;

    public Fragment_EditRoute() {
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
            view = inflater.inflate(R.layout.fragment_edit, container, false);
        }
        else {
            routeDetails = GetRoute(datera);
            RDC = new RouteDetailsClass(routeDetails);

            // Thanks Android Studio Documentation for leaving me to figure this out completely on my own and not thinking to update your documentation at all
            // Set the binding
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false);
            view = binding.getRoot();
            binding.setVariable(BR.RDC, RDC);
            binding.setVariable(BR.DataFile, routeDetails.toString());
        }

        // Set OnClickListeners
        final Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        final Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
        final Button addAnchorButton = (Button) view.findViewById(R.id.addAnchorButton);
        addAnchorButton.setOnClickListener(this);

        // Reference lovely EditTexts
        titleEdit = (EditText)view.findViewById(R.id.routeTitle);
        gpsEdit = (EditText)view.findViewById(R.id.routeGPS);
        difficultyEdit = (EditText)view.findViewById(R.id.routeDifficulty);
        areaSpinner = (Spinner)view.findViewById(R.id.routeAreaSpinner);
        areaTextView = (TextView)view.findViewById(R.id.routeAreaTextView);

        // Anchors and stuff
        anchorListView = (ListView) view.findViewById(R.id.anchors_listview);

        anchorListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            RemoveAnchor(position);
            return true;
            }
        });

        anchorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              ReplaceAnchor(position);
          }
      });

        // Special Bindings
    if(parentActivity.isNew) {
        // SEt title prompt
        titleEdit.setText(R.string.routeTitlePrompt);
        areaTextView.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        // bind properties of spinner to areas
        areaSpinner.setAdapter(new AdapterArea(this, areas));
    }
    else
    {
        // Load anchors
        anchorListView.setAdapter(new AdapterAnchor(this, RDC.anchors));
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
            case R.id.deleteButton :
                TryDeleteRoute();
                break;
            case R.id.addAnchorButton:
                AddNewAnchor();
                break;
            default:
                ToastMachine("ERRRRRROR");
                break;
        }
    }

    private void TryDeleteRoute() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        DeleteRoute();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you really want to delete this route?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void SaveNewRoute() {
        RDC = new RouteDetailsClass();
        RDC.name = titleEdit.getText().toString();
        RDC.area = "area-" + ((AreaClass)areaSpinner.getSelectedItem()).id;
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

    private void AddAnchorDialog()
    {
        // Create Object of Dialog class
        final Dialog anchorDialog = new Dialog(getContext());
        // Set GUI of login screen
        anchorDialog.setContentView(R.layout.dialog_anchor);

        // Init button of login GUI
        Button saveButton = (Button) anchorDialog.findViewById(R.id.btnSave);
        Button btnCancel = (Button) anchorDialog.findViewById(R.id.btnCancel);
        final EditText anchorDifficulty = (EditText)anchorDialog.findViewById(R.id.anchorDifficulty);
        final EditText anchorBeta = (EditText)anchorDialog.findViewById(R.id.anchorBeta);

        // Attached listener for login GUI button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anchorDifficulty.getText().toString().trim().length() > 0 && anchorBeta.getText().toString().trim().length() > 0)
                {
                    // Add anchor
                    RDC.AddAnchor(anchorDifficulty.getText().toString(), anchorBeta.getText().toString());
                    TrySaveData();
                    anchorDialog.dismiss();
                }
                else
                {
                    ToastMachine(getResources().getString(R.string.anchorEntryError));
                }
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


    private void AddNewAnchor()
    {
        AddAnchorDialog();
    }

    private void ReplaceAnchor(int _pos)
    {
        String _difficulty = RDC.anchors[_pos].difficulty;
        String _beta = RDC.anchors[_pos].beta;

        ReplaceAnchorDialog(_pos, _difficulty,_beta);
    }

    private void ReplaceAnchorDialog(final int pos, final String _difficulty,final String _beta)
    {
        // Create Object of Dialog class
        final Dialog anchorDialog = new Dialog(getContext());
        // Set GUI of login screen
        anchorDialog.setContentView(R.layout.dialog_anchor);

        // Init button of login GUI
        Button saveButton = (Button) anchorDialog.findViewById(R.id.btnSave);
        Button btnCancel = (Button) anchorDialog.findViewById(R.id.btnCancel);
        final EditText anchorDifficulty = (EditText)anchorDialog.findViewById(R.id.anchorDifficulty);
        final EditText anchorBeta = (EditText)anchorDialog.findViewById(R.id.anchorBeta);

        anchorDifficulty.setText(_difficulty);
        anchorBeta.setText(_beta);

        // Attached listener for login GUI button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anchorDifficulty.getText().toString().trim().length() > 0 && anchorBeta.getText().toString().trim().length() > 0)
                {
                    // Add anchor
                    RDC.ReplaceAnchor(pos, anchorDifficulty.getText().toString(), anchorBeta.getText().toString());
                    TrySaveData();
                    anchorDialog.dismiss();
                }
                else
                {
                    ToastMachine(getResources().getString(R.string.anchorEntryError));
                }
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

    private void RemoveAnchor(final int pos)
    {
        if(RDC == null)
        {
            ToastMachine("Please save route details before adding anchors");
        }
        else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            RDC.RemoveAnchor(pos);
                            TrySaveData();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Do you really want to delete this anchor?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
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

    public void DeleteRoute()
    {
        ToastMachine("Deleting Route");
        JSONObject oldData = ReadDaters();
        try {
            // Get route Area and Route ID
            String areaName = routeDetails.getString("route-area");
            String routeId = "route-" + routeDetails.getString("route-id");

            JSONObject area = oldData.getJSONObject(areaName);
            JSONObject routes = area.getJSONObject("routes");
            routes.remove(routeId);
            boolean suc = WriteNewDatersFile(oldData.toString());
            if(suc){
                ToastMachine("Route Deleted");
                parentActivity.finish();
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
        Intent in = new Intent(parentActivity,ActivityDetails.class);
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
