package com.example.mattghall.finalproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MattG on 11/30/2016.
 */
//{ "area-0": { "area-id": "0", "area-name": "Sweet Climbin", "trailhead-name": "test-trailhead", "trailhead-gps": "000200020", "routes": { "route-0": { "route-id": "0", "route-name": "Fun Route", "route-area": "area-0", "route-gps": "000000", "route-difficulty": "5-9", "route-img": "DCS 4320", "anchors": { "anchor-0": { "anchor-num": "0", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" }, "anchor-1": { "anchor-num": "1", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" }, "anchor-2": { "anchor-num": "2", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" } } }, "route-1": { "route-id": "1", "route-name": "Sucky Route", "route-area": "area-0", "route-gps": "000000", "route-difficulty": "5-9", "route-img": "DCS 4320", "anchors": { "anchor-0": { "anchor-num": "0", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" }, "anchor-1": { "anchor-num": "1", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" }, "anchor-2": { "anchor-num": "2", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" } } } } }, "area-1": { "area-id": "1", "area-name": "Vertical World", "trailhead-name": "Commodore Ave", "trailhead-gps": "000200020", "routes": { "route-0": { "route-id": "0", "route-name": "The Crack", "route-area": "area-1", "route-gps": "000000", "route-difficulty": "5-9", "route-img": "DCS 4320", "anchors": { "anchor-0": { "anchor-num": "0", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" }, "anchor-1": { "anchor-num": "1", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" }, "anchor-2": { "anchor-num": "2", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" } } }, "route-1": { "route-id": "1", "route-name": "Slabin it", "route-area": "area-1", "route-gps": "000000", "route-difficulty": "5-11c", "route-img": "DCS 4320", "anchors": { "anchor-0": { "anchor-num": "0", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" }, "anchor-1": { "anchor-num": "1", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" }, "anchor-2": { "anchor-num": "2", "anchor-difficulty": "5-8", "anchor-beta": "this is easy clip" } } } } } }
public class ClimbingAreaClass {
    public String id;
    public String name;
    public String trailHeadName;
    public String trailHeadGPS;

    public ClimbingAreaClass(JSONObject area) {
        try {
            this.id = area.getString("area-id");
            this.name = area.getString("area-name");
            this.trailHeadName = area.getString("trailhead-name");
            this.trailHeadGPS = area.getString("trailhead-gps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ClimbingAreaClass(String _name, String _trailHeadName, String _trailHeadGPS)
    {
        this.name = _name;
        this.trailHeadName = _trailHeadName;
        this.trailHeadGPS = _trailHeadGPS;
    }

    // Does not include routes
    public JSONObject GetJSON()
    {
        // sample = { "area-id": "1", "area-name": "Vertical World", "trailhead-name": "Commodore Ave", "trailhead-gps": "000200020", "routes": {} }
        String json = "{ \"area-id\": \"" + this.id + "\", \"area-name\": \"" + this.name + "\", \"trailhead-name\": \"" + this.trailHeadName + "\", \"trailhead-gps\": \"" + this.trailHeadGPS + "\", \"routes\": {} }";
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }
}