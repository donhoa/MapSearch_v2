package com.thedon.MapSearch.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thedon.MapSearch.R;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: dhoang
 */
public class MapSearchFragment extends Fragment
{
    private FragmentActivity mActivity;
    private Button mSearchButton;
    private EditText mAddressEditText;
    private GoogleMap mGoogleMap;
    private LatLng mLatLng;
    private MarkerOptions markerOptions;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mActivity = getActivity();

        View view = inflater.inflate(R.layout.map_search_fragment, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)mActivity.getSupportFragmentManager().findFragmentById(R.id.map);

        mGoogleMap = supportMapFragment.getMap();

        mAddressEditText = (EditText)view.findViewById(R.id.addressTextbox);


        mSearchButton = (Button)view.findViewById(R.id.searchButton);

        mSearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String addressSearchString;
                addressSearchString = mAddressEditText.getText().toString();
                new GeocoderTask().execute(addressSearchString);
            }
        });

        return view;
    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>
    {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(mActivity);
            List<Address> addresses = null;

            try
            {
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0)
            {
                Toast.makeText(mActivity, "No Location found", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(mActivity, "Found: " + addresses.get(0).getCountryName(), Toast.LENGTH_SHORT).show();

            // Clears all the existing markers on the map
            //mGoogleMap.clear();

            for(int i=0;i<addresses.size();i++)
            {

                Address address = addresses.get(i);

                mLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(mLatLng);
                markerOptions.title(addressText);

                mGoogleMap.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLatLng));
                }
            }
        }
    }
}
