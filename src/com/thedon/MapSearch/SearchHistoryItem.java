package com.thedon.MapSearch;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: dhoang
 */
public class SearchHistoryItem
{
    private long mId;
    private String mAddress;
    private String mDescription;

    public SearchHistoryItem(long aId, String aAddress, String aDescription)
    {
        mId = aId;
        mAddress = aAddress;
        mDescription = aDescription;
    }

    public long getId()
    {
        return mId;
    }

    public String getAddress()
    {
        return mAddress;
    }

    public String getDescription()
    {
        return mDescription;
    }
}

