package com.example.mattghall.finalproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mattghall on 11/28/2016.
 */

public class RouteDetailsClass {
    public String id;

    public String getFullId() {
        return "route-" + id;
    }

    public String name;
    public String area;
    public String gps;
    public String difficulty;
    public String numAnchors;
    public AnchorClass [] anchors;

    // Constructor made from passing in JSONObject of the route
    public RouteDetailsClass(JSONObject data) {
        try {
            this.id = data.getString("route-id");
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

    public RouteDetailsClass(String _id, String _name, String _area, String _gps, String _difficulty,String _numAnchors, AnchorClass [] _anchors) {
        this.id = _id;
        this.name = _name;
        this.area = _area;
        this.gps = _gps;
        this.difficulty = _difficulty;
        this.numAnchors = _numAnchors;
        this.anchors = _anchors;
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

    public void AddAnchor(String anchorDifficlty, String anchorBeta)
    {
        int i = 0;
        int l = this.anchors.length;
        AnchorClass[] newAnchors = new AnchorClass[l+1];

        while( i < l)
        {
            newAnchors[i] = anchors[i];
            i++;
        }

        newAnchors[l] = new AnchorClass(String.valueOf(l),anchorDifficlty,anchorBeta);
        anchors = newAnchors;
        numAnchors = String.valueOf(l+1);
    }

    //"routes\": { \"route-0\": { \"route-id\": \"0\", \"route-name\": \"Fun Route\", \"route-area\": \"area-0\", \"route-gps\": \"000000\", \"route-difficulty\": \"5-9\", \"route-img\": \"DCS 4320\", \"anchors\": { \"anchor-0\": { \"anchor-num\": \"0\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-1\": { \"anchor-num\": \"1\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" }, \"anchor-2\": { \"anchor-num\": \"2\", \"anchor-difficulty\": \"5-8\", \"anchor-beta\": \"this is easy clip\" } } }
    public JSONObject GetJSON() throws JSONException {
        String img = "n/a";
        String json = "{ \"route-id\": \""+ this.id +"\", \"route-name\": \"" + this.name +
                "\", \"route-area\": \""+ this.area +"\", \"route-gps\": \""+ this.gps +"\", \"route-difficulty\": \""+ this.difficulty +
                "\", \"route-img\": \""+ img +"\", \"anchors\": { ";

        String jsonAnchors = "";
        for(AnchorClass anchor : anchors)
        {
            if(!anchor.id.equals("0"))  jsonAnchors += ",";
            jsonAnchors += anchor.GetJSONString();

        }
        // Close anchors and routes brackets
        jsonAnchors += "}}";

        json += jsonAnchors;
        return new JSONObject(json);
    }
}
