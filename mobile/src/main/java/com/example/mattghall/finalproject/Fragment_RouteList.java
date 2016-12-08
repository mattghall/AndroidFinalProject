package com.example.mattghall.finalproject;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment_RouteList extends Fragment {

    public Fragment_RouteList() {
    }

    ActivityMain mainActivity;
    private String[] routeNames = { "Route A","Route B", "Route C","Route Error" };
    private String[] routeIds = { "0","1","2" };
    private ListView routeListView;
    private ArrayAdapter arrayAdapter;
    JSONObject areaData = null;
    AreaClass climbingAreaClass = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (ActivityMain)getActivity();
        Bundle buns = this.getArguments();
        try {
            if(buns == null)
                throw new Exception();
            areaData = new JSONObject(buns.getString("area"));
            climbingAreaClass = new AreaClass(areaData);
            routeNames = GetRouteNames(areaData);
            routeIds = GetRouteIds(areaData);

        } catch (Exception e) {
            ToastMachine(getResources().getString(R.string.couldNotLoadRoutesMessage));
            e.printStackTrace();
        }

        ViewDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_routes,container,false);
        View fragmentView = binding.getRoot();
        binding.setVariable(BR.areaData,climbingAreaClass);

        routeListView = (ListView) fragmentView.findViewById(R.id.routes_listview);

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, routeNames);
        routeListView.setAdapter(arrayAdapter);

        routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(routeIds.length > position && routeNames.length > position) {
                   String temp = "route-" + String.valueOf(routeIds[position]);
                   try {
                       JSONObject route = areaData.getJSONObject("routes").getJSONObject(temp);
                       route = mainActivity.data.getJSONObject(route.getString("route-area")).getJSONObject("routes").getJSONObject(temp);
                       OpenRoute(route);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
                else
               {
                   ToastMachine(getResources().getString(R.string.couldNotLoadRoutesMessage));
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

    String [] GetRouteNames(JSONObject data) throws JSONException {
        String temp = "";
        JSONObject obj = null;
        int l = data.getJSONObject("routes").length();

        String[] _routeNames = new String[l];
        JSONArray names = data.getJSONObject("routes").names();

        for(int i = 0; i < l; i++)
        {
            temp = names.get(i).toString();
            obj = data.getJSONObject("routes").getJSONObject(temp);
            temp = obj.getString("route-name");
            _routeNames[i] = temp;
        }
        return _routeNames;
    }


    String [] GetRouteIds(JSONObject data) throws JSONException {
        String temp = "";
        JSONObject obj = null;
        int l = data.getJSONObject("routes").length();

        String[] _routeIds = new String[l];
        JSONArray names = data.getJSONObject("routes").names();

        for(int i = 0; i < l; i++)
        {
            temp = names.get(i).toString();
            obj = data.getJSONObject("routes").getJSONObject(temp);
            temp = obj.getString("route-id");
            _routeIds[i] = temp;
        }
        return _routeIds;
    }
    void OpenRoute(JSONObject _route)
    {
        Intent in = new Intent(getActivity(),ActivityDetails.class);
        String putme = _route.toString();
        in.putExtra("route", putme);
        startActivity(in);
    }
}
