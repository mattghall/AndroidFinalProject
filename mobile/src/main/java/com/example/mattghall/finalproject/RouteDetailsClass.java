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
    public AnchorClass [] anchors;

    // Constructor made from passing in JSONObject of the route
    public RouteDetailsClass(JSONObject data) {
        try {
            this.name = data.getString("route-name");
            this.area = data.getString("route-area");
            this.gps = data.getString("route-gps");
            this.difficulty = data.getString("route-difficulty");
            this.numAnchors = String.valueOf(data.getJSONObject("anchors").length());
            this.anchors = GetAnchors(data.getJSONObject("anchors"));
            this.name = this.name + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private AnchorClass [] GetAnchors(JSONObject data) throws JSONException {
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
}
