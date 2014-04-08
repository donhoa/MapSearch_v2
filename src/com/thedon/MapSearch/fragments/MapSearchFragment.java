package com.thedon.MapSearch.fragments;

import android.app.ActionBar;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thedon.MapSearch.R;
import com.thedon.MapSearch.Utils;
import database.SearchMapDatabaseAdapter;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: dhoang
 */
public class MapSearchFragment extends Fragment
{
    private static final String TAG = MapSearchFragment.class.getSimpleName();

    private FragmentActivity mActivity;
    private Button mSearchButton;
    private EditText mAddressEditText;
    private EditText mDescriptionEditText;
    private GoogleMap mGoogleMap;
    private LatLng mLatLng;
    private MarkerOptions markerOptions;

    private SearchMapDatabaseAdapter mDatabaseAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        final ActionBar actionBar = mActivity.getActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        mDatabaseAdapter = new SearchMapDatabaseAdapter(mActivity);
        mDatabaseAdapter.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.map_search_fragment, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)mActivity.getSupportFragmentManager().findFragmentById(R.id.map);

        mGoogleMap = supportMapFragment.getMap();

        mAddressEditText = (EditText)view.findViewById(R.id.addressTextbox);
        mDescriptionEditText = (EditText)view.findViewById(R.id.descriptionTextBox);

        mSearchButton = (Button)view.findViewById(R.id.searchButton);

        mSearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String addressSearchString;
                addressSearchString = mAddressEditText.getText().toString();

                if (Utils.isNullOrEmptyString(addressSearchString))
                {
                    Toast.makeText(mActivity, getResources().getString(R.string.emptyFieldsToast), Toast.LENGTH_SHORT).show();
                    return;
                }

                new GeocoderTask().execute(addressSearchString);

                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(mActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
        });

        return view;
    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>
    {

        @Override
        protected List<Address> doInBackground(String... locationName) {
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

            if(addresses == null || addresses.size() == 0)
            {
                Toast.makeText(mActivity, getResources().getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            mGoogleMap.clear();

            for(int i = 0; i<addresses.size(); i++)
            {
                Address address = addresses.get(i);

                mLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(mLatLng);
                markerOptions.title(addressText);
                markerOptions.snippet(mDescriptionEditText.getText().toString());

                mGoogleMap.addMarker(markerOptions);

                //Show the first location
                if(i == 0)
                {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));

                    String addressSearchString = mAddressEditText.getText().toString();
                    String descriptionString = mDescriptionEditText.getText().toString();

                    if (mLatLng != null)
                    {
                        Log.d(TAG, "Address: " + addressSearchString + " - latitude: " + mLatLng.latitude + " - longitude: " + mLatLng.longitude );
                        mDatabaseAdapter.addSearchHistory(addressSearchString, descriptionString, String.valueOf(mLatLng.latitude), String.valueOf(mLatLng.longitude));
                    }
                }

            }
        }
    }
}
