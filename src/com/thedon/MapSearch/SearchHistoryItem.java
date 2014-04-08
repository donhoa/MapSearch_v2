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
    private String mLatitude;
    private String mLongitude;

    public SearchHistoryItem(long aId, String aAddress, String aDescription, String aLatitude, String aLongitude)
    {
        mId = aId;
        mAddress = aAddress;
        mDescription = aDescription;
        mLatitude = aLatitude;
        mLongitude = aLongitude;
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

    public String getLatitude()
    {
        return mLatitude;
    }

    public String getLongitude()
    {
        return mLongitude;
    }
}

