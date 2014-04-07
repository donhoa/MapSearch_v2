package com.thedon.MapSearch.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.thedon.MapSearch.NavigationDrawerItem;
import com.thedon.MapSearch.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: dhoang
 */
public class NavigationDrawerAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<NavigationDrawerItem> mNavDrawerItems;

    public NavigationDrawerAdapter(Context aContext, ArrayList<NavigationDrawerItem> aNavDrawerItems)
    {
        mContext = aContext;
        mNavDrawerItems = aNavDrawerItems;
    }

    @Override
    public int getCount()
    {
        return mNavDrawerItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mNavDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.navigation_drawer_list_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.drawer_item_title);
        txtTitle.setText(mNavDrawerItems.get(position).getTitle());

        return convertView;
    }
}

