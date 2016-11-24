package layout;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mattghall.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_climbing_areas extends Fragment {


    public fragment_climbing_areas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_climbing_areas, container, false);
    }

}
