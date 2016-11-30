package com.example.mattghall.finalproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteList_Fragment extends Fragment {

    public RouteList_Fragment() {
    }

    MainActivity mainActivity;
    private String[] routeNames = { "Route A","Route B", "Route C","Route Error" };
    private String[] routeIds = { "0","1","2" };
    private ListView routeListView;
    private ArrayAdapter arrayAdapter;
    JSONObject areaData = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MainActivity)getActivity();
        Bundle buns = this.getArguments();
        try {
            if(buns == null)
                throw new Exception();
            areaData = new JSONObject(buns.getString("area"));
            routeNames = GetRouteNames(areaData);
            routeIds = GetRouteIds(areaData);
        } catch (Exception e) {
            ToastMachine("Could not get routes");
            e.printStackTrace();
        }

        View fragmentView = inflater.inflate(R.layout.fragment_routes,container,false);

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
                       OpenArea(route.getString("route-id"),route);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
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

    String [] GetRouteNames(JSONObject data) throws JSONException {
        String temp = "";
        JSONObject obj = null;
        int l = data.getJSONObject("routes").length();

        String[] routeNames = new String[l];

        for(int i = 0; i < l; i++)
        {
            temp = "route-" + String.valueOf(i);
            obj = data.getJSONObject("routes").getJSONObject(temp);
            temp = obj.getString("route-name");
            routeNames[i] = temp;
        }
        return routeNames;
    }


    String [] GetRouteIds(JSONObject data) throws JSONException {
        String temp = "";
        JSONObject obj = null;
        int l = data.getJSONObject("routes").length();

        String[] routeIds = new String[l];

        for(int i = 0; i < l; i++)
        {
            temp = "route-" + String.valueOf(i);
            obj = data.getJSONObject("routes").getJSONObject(temp);
            temp = obj.getString("route-id");
            routeIds[i] = temp;
        }

        return routeIds;
    }
    void OpenArea(String climbingAreaId, JSONObject area)
    {
        Intent in = new Intent(getActivity(),DetailsActivity.class);
        String putme = area.toString();
        in.putExtra("route", putme);
        startActivity(in);
    }
}
