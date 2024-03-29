package com.example.mattghall.finalproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MattG on 12/1/2016.
 */

//{"anchor-0":{"anchor-num":"0","anchor-difficulty":"5-8","anchor-beta":"this is easy clip"},"anchor-1":{"anchor-num":"1","anchor-difficulty":"5-8","anchor-beta":"this is easy clip"},"anchor-2":{"anchor-num":"2","anchor-difficulty":"5-8","anchor-beta":"this is easy clip"}}
public class AnchorClass {
    public String id;
    public String difficulty;
    public String beta;

    public AnchorClass(){};

    public AnchorClass(JSONObject _anchor) {
        this.id = "";
        this.difficulty = "";
        this.beta = "";
        try {
            this.id = _anchor.getString("anchor-num");
            this.difficulty = _anchor.getString("anchor-difficulty");
            this.beta = _anchor.getString("anchor-beta");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AnchorClass(String _id, String _difficulty, String _beta)
    {
        this.id = _id;
        this.difficulty = _difficulty;
        this.beta = _beta;
    }

    @Override
    public String toString(){
        return "anchor-" + this.id + ". Difficulty: " + this.difficulty + ". \nBeta: " + this.beta;
    }


    public String GetJSONString() throws JSONException {
        String json = "\"anchor-" + this.id + "\": { \"anchor-num\": \"" + this.id + "\", \"anchor-difficulty\": \"" + this.difficulty + "\", \"anchor-beta\": \"" + this.beta + "\" }";
        return json;
    }
}
