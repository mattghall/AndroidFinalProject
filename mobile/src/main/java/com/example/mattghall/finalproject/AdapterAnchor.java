package com.example.mattghall.finalproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// https://www.caveofprogramming.com/guest-posts/custom-listview-with-imageview-and-textview-in-android.html
public class AdapterAnchor extends BaseAdapter{
    AnchorClass [] result;
    Context context;
    private static LayoutInflater inflater=null;

    public AdapterAnchor(Fragment_RouteDetails mainActivity, AnchorClass [] anchors) {
        // TODO Auto-generated constructor stub
        result=anchors;
        context=mainActivity.getContext();
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AdapterAnchor(Fragment_EditRoute mainActivity, AnchorClass [] anchors) {
        // TODO Auto-generated constructor stub
        result=anchors;
        context=mainActivity.getContext();
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView idTextView;
        TextView difficultyTextView;
        TextView betaTextView;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.anchor_sublayout, null);

        // Reference Text Views
        holder.idTextView=(TextView) rowView.findViewById(R.id.anchorId);
        holder.difficultyTextView=(TextView) rowView.findViewById(R.id.anchorDifficulty);
        holder.betaTextView=(TextView) rowView.findViewById(R.id.anchorBeta);

        // Set text of said views
        holder.idTextView.setText(result[position].id);
        holder.difficultyTextView.setText(result[position].difficulty);
        holder.betaTextView.setText(result[position].beta);

        return rowView;
    }

} 