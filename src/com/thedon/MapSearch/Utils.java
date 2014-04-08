package com.thedon.MapSearch;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: dhoang
 */
public class Utils
{
    private static final String TAG = "Utils";

    public static boolean isNullOrEmptyString(final String aString)
    {
        return ((aString == null) || (aString.length() == 0));
    }
}
