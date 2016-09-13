package com.laioffer.laiofferproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantGridFragment extends Fragment {
    private GridView gridView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DataService dataService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_grid, container, false);
        gridView = (GridView) view.findViewById(R.id.restaurant_grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant r = (Restaurant) gridView.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(RestaurantMapActivity.EXTRA_LATLNG, new LatLng(r.getLat(), r.getLng()));
                Intent intent = new Intent(view.getContext(), RestaurantMapActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        dataService = new DataService();
        refreshRestaurantGrid(dataService);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_grid);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRestaurantGrid(dataService);
            }
        });

        return view;
    }

    private void refreshRestaurantGrid(DataService dataService) {
        new GetRestaurantsNearbyAsyncTask(this, dataService).execute();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class GetRestaurantsNearbyAsyncTask extends AsyncTask<Void, Void, List<Restaurant>> {
        private Fragment fragment;
        private DataService dataService;
        private Clock clock;

        public GetRestaurantsNearbyAsyncTask(Fragment fragment, DataService dataService) {
            this.fragment = fragment;
            this.dataService = dataService;
            this.clock = new Clock();
            this.clock.reset();
        }

        @Override
        protected List<Restaurant> doInBackground(Void... params) {
            clock.start();
            return dataService.getNearbyRestaurants();
        }

        @Override
        protected void onPostExecute(List<Restaurant> restaurants) {
            // Measure the latency of the API call.
            clock.stop();
            Log.e("Latency", Long.toString(clock.getCurrentInterval()));

            if (restaurants != null) {
                super.onPostExecute(restaurants);
                RestaurantAdapter adapter = new RestaurantAdapter(fragment.getActivity(), restaurants);
                gridView.setAdapter(adapter);
            } else {
                Toast.makeText(fragment.getActivity(), "Data service error.", Toast.LENGTH_LONG);
            }
        }
    }
}
