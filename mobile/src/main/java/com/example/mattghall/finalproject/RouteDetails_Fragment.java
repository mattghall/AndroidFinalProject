package com.example.mattghall.finalproject;

import android.app.FragmentTransaction;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mattghall.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetails_Fragment extends Fragment implements View.OnClickListener {
    JSONObject routeDetails = null;
    String FILENAME = "data_file";
    private String[] anchorNames = new String[0];
    private String[] anchorIds = new String[0];
    private String[] anchorBeta = new String[0];
    AnchorClass [] anchors = null;

    public RouteDetails_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the form and get all the daters and stuff
        DetailsActivity parentActivity = (DetailsActivity) getActivity();
        routeDetails = parentActivity.GetDataTails();
        RouteDetailsClass RDC = new RouteDetailsClass(routeDetails);

        // Get The Anchor Stuff
        try {
            anchors = GetAnchors(routeDetails.getJSONObject("anchors"));
        } catch (JSONException e) {
            ToastMachine("Could not load anchors");
            e.printStackTrace();
        }

        // Thanks Android Studio Documentation for leaving me to figure this out completely on my own and not thinking to update your documentation at all
        // Set the binding
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_route__details,container,false);
        View eternalAnger = binding.getRoot();
        binding.setVariable(BR.RDC,RDC);
        binding.setVariable(BR.DataFile,routeDetails.toString());

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
                ToastMachine("EDITTTT");
                getView().findViewById(R.id.dataFileText).setVisibility(View.VISIBLE);
                break;
            case R.id.saveButton :
                TrySaveData();
                break;
            default:
                ToastMachine("ERRRRRROR");
                break;
        }
    }

    public void TrySaveData(){
        EditText editText = (EditText) getView().findViewById(R.id.dataFileText);
        // Check to see if Valid JSON
        try {
            JSONObject newData = new JSONObject(editText.getText().toString());
            SaveData(newData);
            editText.setVisibility(View.INVISIBLE);
        } catch (JSONException e) {
            ToastMachine("Data is invalid. Try again");
            e.printStackTrace();
        }
    }

    public void SaveData (JSONObject newRouteDetails)
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

    AnchorClass [] GetAnchors(JSONObject data) throws JSONException {
        String temp = "";
        AnchorClass obj = null;
        int l = data.length();

        AnchorClass[] anchors = new AnchorClass[l];

        for(int i = 0; i < l; i++)
        {
            temp = "anchor-" + String.valueOf(i);
            obj = new AnchorClass(data.getJSONObject(temp));
            anchors[i] = obj;
        }
        return anchors;
    }


    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), msg, duration);
        toast.show();
    }
}
