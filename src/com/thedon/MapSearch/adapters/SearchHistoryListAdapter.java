package com.thedon.MapSearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.thedon.MapSearch.R;
import com.thedon.MapSearch.SearchHistoryItem;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: dhoang
 */
public class SearchHistoryListAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<SearchHistoryItem> mHistoryArrayList;
    private SearchHistoryListener mListener;

    public interface SearchHistoryListener
    {
        public void onDeleteItem(long id);
        public void onShareViaEmail(SearchHistoryItem item);
        public void onShareViaSMS(SearchHistoryItem item);
    }

    public SearchHistoryListAdapter(Context context, ArrayList<SearchHistoryItem> aHistoryArrayList, SearchHistoryListener aListener)
    {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mHistoryArrayList = aHistoryArrayList;
        mListener = aListener;
    }

    @Override
    public int getCount()
    {
        return mHistoryArrayList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return mHistoryArrayList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int aPosition, View aView, ViewGroup viewGroup)
    {
        SearchHistoryViewHolder viewHolder;

        if (aView == null)
        {
            aView = mInflater.inflate(R.layout.search_history_list_item, null);
            viewHolder = new SearchHistoryViewHolder();
            viewHolder.searchString = (TextView)aView.findViewById(R.id.entry_search_string);
            viewHolder.description = (TextView)aView.findViewById(R.id.entry_description);
            viewHolder.deleteButton = (Button)aView.findViewById(R.id.deleteItemButton);
            viewHolder.sendEmailButton = (Button)aView.findViewById(R.id.shareViaEmailButton);
            viewHolder.sendSMSButton = (Button)aView.findViewById(R.id.shareViaSMSButton);
            aView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (SearchHistoryViewHolder)aView.getTag();
        }

        final SearchHistoryItem currentHistoryItem = mHistoryArrayList.get(aPosition);

        viewHolder.searchString.setText(currentHistoryItem.getAddress());
        viewHolder.description.setText(currentHistoryItem.getDescription());

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onDeleteItem(currentHistoryItem.getId());
            }
        });

        viewHolder.sendEmailButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onShareViaEmail(currentHistoryItem);
            }
        });

        viewHolder.sendSMSButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onShareViaSMS(currentHistoryItem);
            }
        });

        return aView;
    }

    static class SearchHistoryViewHolder
    {
        TextView searchString;
        TextView description;
        Button deleteButton;
        Button sendEmailButton;
        Button sendSMSButton;
    }
}
