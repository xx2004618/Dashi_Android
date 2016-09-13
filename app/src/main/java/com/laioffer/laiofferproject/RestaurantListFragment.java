package com.laioffer.laiofferproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantListFragment extends Fragment {
    private View view;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DataService dataService;
    private Button mapViewBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        listView = (ListView) view.findViewById(R.id.restaurant_list);
        mapViewBtn = (Button) view.findViewById(R.id.mapViewBtn);

        // Set a listener to ListView.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<LatLng> latLngs = new ArrayList<LatLng>();

                latLngs.add(getLatLngByPosition(position));
                // Prepare all the data we need to start map activity.
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(
                        RestaurantMapActivity.EXTRA_LATLNG,
                        latLngs
                );
                Intent intent = new Intent(view.getContext(), RestaurantMapActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }

        });
        dataService = new DataService();
        refreshRestaurantList(dataService);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRestaurantList(dataService);
            }
        });

        mapViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView != null) {
                    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
                    for (int i = 0; i < listView.getCount(); i++) {
                        latLngs.add(getLatLngByPosition(i));
                    }

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(
                            RestaurantMapActivity.EXTRA_LATLNG,
                            latLngs
                    );

                    Intent intent = new Intent(view.getContext(), RestaurantMapActivity.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }

            }
        });
        return view;
    }

    private LatLng getLatLngByPosition(int position) {
        Restaurant r = (Restaurant) listView.getItemAtPosition(position);
        return new LatLng(r.getLat(), r.getLng());
    }

    // Make a async call to get restaurant data.
    private void refreshRestaurantList(DataService dataService) {
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
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(fragment.getActivity(), "Data service error.", Toast.LENGTH_LONG);
            }
        }
    }

}
