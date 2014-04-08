package com.thedon.MapSearch;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.thedon.MapSearch.adapters.NavigationDrawerAdapter;
import com.thedon.MapSearch.fragments.HistoryFragment;
import com.thedon.MapSearch.fragments.MapSearchFragment;

import java.util.ArrayList;

public class MapSearchActivity extends FragmentActivity
{
    private static final String TAG = MapSearchActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private String[] mDrawerListTitles;
    private ArrayList<NavigationDrawerItem> mDrawerListItems = new ArrayList<NavigationDrawerItem>();
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initNavigationDrawer();

        if (savedInstanceState == null)
        {
            Fragment mapSearchFragment = new MapSearchFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.navigation_drawer_frame, mapSearchFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return (super.onOptionsItemSelected(item));
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void initNavigationDrawer()
    {
        mDrawerListTitles = getResources().getStringArray(R.array.nav_drawer_items);

        for (int i =0; i < mDrawerListTitles.length; i++)
        {
            mDrawerListItems.add(new NavigationDrawerItem(mDrawerListTitles[i]));
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navigation_drawer_list_view);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectItem(position);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        mDrawerList.setAdapter(new NavigationDrawerAdapter(this, mDrawerListItems));

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.about, R.string.about)
        {
            public void onDrawerClosed(View view)
            {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int aPosition)
    {
        mDrawerList.setItemChecked(aPosition, true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (aPosition)
        {
            case 0:
                Fragment oldMapFrag = getSupportFragmentManager().findFragmentById(R.id.map);
                if(oldMapFrag != null)
                {
                    getSupportFragmentManager().beginTransaction().remove(oldMapFrag).commit();
                }
                Fragment mapSearchFragment = new MapSearchFragment();
                ft.replace(R.id.navigation_drawer_frame, mapSearchFragment).commit();
                break;
            case 1:
                Fragment historyFragment = new HistoryFragment();
                ft.replace(R.id.navigation_drawer_frame, historyFragment).commit();
                break;
        }
    }
}
