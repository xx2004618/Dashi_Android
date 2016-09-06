package com.laioffer.laiofferproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantGridFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_grid, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.restaurant_grid);
        gridView.setAdapter(new RestaurantAdapter(getActivity()));
        return view;
    }


}
