package com.thedon.MapSearch.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.thedon.MapSearch.R;
import database.SearchMapDatabaseAdapter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: dhoang
 */
public class HistoryFragment extends Fragment
{
    private SearchMapDatabaseAdapter mDatabaseAdapter;
    private SimpleCursorAdapter mAdapter;
    private Cursor mDataCursor;

    private Activity mActivity;

    private ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mDatabaseAdapter = new SearchMapDatabaseAdapter(mActivity);
        mDatabaseAdapter.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.search_history_fragment, container, false);

        mList = (ListView)view.findViewById(R.id.historyList);

        getFullDataSource();

        return view;
    }

    private void getFullDataSource()
    {
        //Log.v(TAG, "getFullDataSource()");

        mDataCursor = mDatabaseAdapter.getFullSearchHistory();

        String[] columns = mDatabaseAdapter.getColumnMappedNames();
        int[] to = new int[] { R.id.entry_search_string, R.id.entry_description};

        mAdapter = new SimpleCursorAdapter(mActivity, R.layout.search_history_list_item, mDataCursor, columns, to);

        mList.setAdapter(mAdapter);
    }

    private void refreshSearchHistoryList()
    {
        //Log.v(TAG, "refreshSearchHistoryList()");
        if (!mDatabaseAdapter.isDatabaseOpen())
        {
            mDatabaseAdapter.open();
        }
        mDataCursor = mDatabaseAdapter.getFullSearchHistory();
        mAdapter.changeCursor(mDataCursor);
        mList.invalidate();
    }

    private void deleteSearchItem(long aId)
    {
        if (!mDatabaseAdapter.isDatabaseOpen())
        {
            mDatabaseAdapter.deleteSearchHistory(aId);
        }

        refreshSearchHistoryList();
    }


}
