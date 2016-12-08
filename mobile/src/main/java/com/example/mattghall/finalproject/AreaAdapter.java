package com.example.mattghall.finalproject;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

// http://stackoverflow.com/questions/6562236/android-spinner-databind-using-array-list
// https://www.caveofprogramming.com/guest-posts/custom-listview-with-imageview-and-textview-in-android.html
public class AreaAdapter extends BaseAdapter implements SpinnerAdapter{
    /**
     * The internal data (the ArrayList with the Objects).
     */
    private final List<ClimbingAreaClass> data;
    Context context;
    private static LayoutInflater inflater=null;

    public AreaAdapter(EditRoute_Fragment mainActivity, List<ClimbingAreaClass> data){
        this.data = data;
        context=mainActivity.getContext();
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Returns the Size of the ArrayList
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Returns one Element of the ArrayList
     * at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    /**
     * Returns the View that is shown when a element was
     * selected.
     */
    public View getView(int position, View recycle, ViewGroup parent) {
        TextView text;
        if (recycle != null){
            // Re-use the recycled view here!
            text = (TextView) recycle;
        } else {
            // No recycled view, inflate the "original" from the platform:
            text = (TextView)  inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false
            );
        }
        text.setTextColor(Color.BLACK);
        text.setText(data.get(position).name);
        return text;
    }



} 