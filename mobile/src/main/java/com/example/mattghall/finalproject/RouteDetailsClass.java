package com.example.mattghall.finalproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mattghall on 11/28/2016.
 */

public class RouteDetailsClass {
    public String name;
    public String area;
    public String gps;
    public String difficulty;
    public String numAnchors;


    // Constructor made from passing in JSONObject of the route
    public RouteDetailsClass(JSONObject data) {
        try {
            this.name = data.getString("route-name");
            this.area = data.getString("route-area");
            this.gps = data.getString("route-gps");
            this.difficulty = data.getString("route-difficulty");
            this.numAnchors = String.valueOf(data.getJSONObject("anchors").length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
