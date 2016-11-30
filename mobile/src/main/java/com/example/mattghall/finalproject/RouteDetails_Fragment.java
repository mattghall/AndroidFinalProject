package com.example.mattghall.finalproject;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mattghall.finalproject.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetails_Fragment extends Fragment {

    JSONObject routeDetails = null;
    String [] anchors = null;

    public RouteDetails_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DetailsActivity parentActivity = (DetailsActivity) getActivity();
        routeDetails = parentActivity.GetDataTails();

        RouteDetailsClass RDC = new RouteDetailsClass(routeDetails);

        // Thanks Android Studio Documentation for leaving me to figure this out completely on my own and not thinking to update your documentation at all
        ViewDataBinding binding = DataBindingUtil.setContentView(parentActivity, R.layout.fragment_route__details);
        binding.setVariable(BR.RDC, RDC);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route__details, container, false);
    }

    void ToastMachine(String msg){
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), msg, duration);
        toast.show();
    }

    String[] GetAnchors(JSONObject routeDetails) throws JSONException {
        String temp = "";
        JSONObject obj = null;

        int l = routeDetails.getJSONObject("anchors").length();

        String[] achors = new String[l];

        for(int i = 0; i < l; i++)
        {
            temp = obj.getString("anchor-name");
            anchors[i] = temp;
            ToastMachine(temp);
        }
        return achors;
    }

}
