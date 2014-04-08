package com.thedon.MapSearch.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.thedon.MapSearch.R;
import com.thedon.MapSearch.SearchHistoryItem;
import com.thedon.MapSearch.adapters.SearchHistoryListAdapter;
import database.Schema;
import database.SearchMapDatabaseAdapter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: dhoang
 */
public class HistoryFragment extends Fragment implements SearchHistoryListAdapter.SearchHistoryListener
{
    private static final String GOOGLE_MAPS_QUERY_BASE_STRING = "http://maps.google.com/maps?q=";

    private SearchMapDatabaseAdapter mDatabaseAdapter;
    private SearchHistoryListAdapter mAdapter;
    private Cursor mDataCursor;

    private Activity mActivity;

    private ListView mList;

    private SearchHistoryListAdapter.SearchHistoryListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mListener = this;

        mDatabaseAdapter = new SearchMapDatabaseAdapter(mActivity);
        mDatabaseAdapter.open();

        final ActionBar actionBar = mActivity.getActionBar();
        actionBar.setTitle(getResources().getString(R.string.searchHistoryTitle));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.search_history_fragment, container, false);

        mList = (ListView)view.findViewById(R.id.historyList);

        getFullDataSource();

        return view;
    }

    @Override
    public void onDeleteItem(long aId)
    {
        Log.v("Don", "onDeleteItem(): " + aId);

        deleteSearchItem(aId);
    }

    @Override
    public void onShareViaEmail(SearchHistoryItem item)
    {
        Log.v("Don", "onShareViaEmail(): " + item.getAddress());

        shareViaEmail(item.getAddress(), GOOGLE_MAPS_QUERY_BASE_STRING + item.getLatitude() + "," + item.getLongitude());
    }

    @Override
    public void onShareViaSMS(SearchHistoryItem item)
    {
        Log.v("Don", "onShareViaSMS(): " + item.getAddress());

        shareViaSMS(GOOGLE_MAPS_QUERY_BASE_STRING + item.getLatitude() + "," + item.getLongitude());
    }

    private void getFullDataSource()
    {
        //Log.v(TAG, "getFullDataSource()");

        mDataCursor = mDatabaseAdapter.getFullSearchHistory();

        ArrayList<SearchHistoryItem> list = new ArrayList<SearchHistoryItem>();

        if (mDataCursor.moveToFirst())
        {
            do
            {
                long id = mDataCursor.getLong(mDataCursor.getColumnIndex(Schema.SearchHistory.AUTO_ID_PK));
                String searchString = mDataCursor.getString(mDataCursor.getColumnIndex(Schema.SearchHistory.SEARCH_STRING));
                String description = mDataCursor.getString(mDataCursor.getColumnIndex(Schema.SearchHistory.DESCRIPTION));
                String latitude = mDataCursor.getString(mDataCursor.getColumnIndex(Schema.SearchHistory.LATITUDE));
                String longitude = mDataCursor.getString(mDataCursor.getColumnIndex(Schema.SearchHistory.LONGITUDE));
                list.add(new SearchHistoryItem(id, searchString, description, latitude, longitude));

                Log.v("Don", "ID: " + id + " - SEARCH STRING: " + searchString + " - " + description + " - " + latitude + " - " + longitude);

            } while(mDataCursor.moveToNext());
        }

        mAdapter = new SearchHistoryListAdapter(mActivity, list, mListener);

        mList.setAdapter(mAdapter);
    }

    private void refreshSearchHistoryList()
    {
        //Log.v(TAG, "refreshSearchHistoryList()");
        if (!mDatabaseAdapter.isDatabaseOpen())
        {
            mDatabaseAdapter.open();
        }

        getFullDataSource();
    }

    private void deleteSearchItem(long aId)
    {
        if (mDatabaseAdapter.isDatabaseOpen())
        {
            mDatabaseAdapter.deleteSearchHistory(aId);
        }

        refreshSearchHistoryList();
    }

    private void shareViaEmail(String aDescription, String aLocationString)
    {
        Log.v("Don", "shareViaEmail(): " + aLocationString);
        Intent  emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, aDescription);
        emailIntent.putExtra(Intent.EXTRA_TEXT, aLocationString);

        startActivity(Intent.createChooser(emailIntent, "Share via:"));
    }

    private void shareViaSMS(String aLocationString)
    {
        Log.v("Don", "shareViaSMS(): " + aLocationString);
        try
        {
            Uri smsUri = Uri.parse("smsto:");
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
            smsIntent.putExtra("sms_body", aLocationString);
            startActivity(smsIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(mActivity, getResources().getText(R.string.no_sms_app), Toast.LENGTH_SHORT).show();
        }
    }
}
