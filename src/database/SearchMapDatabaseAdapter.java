package database;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.SQLException;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public final class SearchMapDatabaseAdapter
{
    private static final String DATABASE_NAME = "map_search_storage.db";
    private static final String TAG = "SearchMapDatabaseAdapter";

    private final DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final int DATABASE_VERSION = 1;

    public SearchMapDatabaseAdapter(Context aContext)
    {
        mDbHelper = new DatabaseHelper(aContext);
    }

    private static final class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            Log.v(TAG, "Creating database...");
            createTables(db);
            Log.v(TAG, "Database created.");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            dropTables(db);
            createTables(db);
        }
    }

    private static void createTables(SQLiteDatabase aDb)
    {
        Log.v(TAG, "Creating tables...");

        aDb.execSQL(Schema.SearchHistory.CREATE_SQL);

        Log.v(TAG, "Tables created.");

    }

    private static void dropTables(SQLiteDatabase aDb)
    {
        Log.v(TAG, "Dropping tables...");
        aDb.execSQL("DROP TABLE IF EXISTS " + Schema.SearchHistory.TABLE_NAME);
        Log.v(TAG, "Tables dropped.");
    }

    // Init
    private static final Object DB_LOCK = new Object();

    public void flushData()
    {
        synchronized(DB_LOCK)
        {
            Log.v(TAG, "Flushing database...");
            dropTables(mDb);
            createTables(mDb);
            Log.v(TAG, "Database flushed.");
        }
    }

    public void open() throws SQLException
    {
        synchronized(DB_LOCK)
        {
            Log.v(TAG, "Opening database...");
            mDb = mDbHelper.getWritableDatabase();
            Log.v(TAG, "Database opened.");
        }
    }

    public void close()
    {
        synchronized(DB_LOCK)
        {
            Log.v(TAG, "Closing database...");
            mDbHelper.close();
            Log.v(TAG, "Database closed.");
        }
    }

    public boolean isDatabaseOpen()
    {
        boolean isOpen = false;

        if (mDb != null)
        {
            isOpen = mDb.isOpen();
        }

        return isOpen;
    }


    public long addSearchHistory(String aSearchString, String aDescription, String aLatitude, String aLongitude)
    {
        Log.v("Don", "addSearchHistory(" + aSearchString + ", " + aDescription + ")");
        final ContentValues initialValues = new ContentValues();

        initialValues.put(Schema.SearchHistory.SEARCH_STRING, aSearchString);
        initialValues.put(Schema.SearchHistory.DESCRIPTION, aDescription);
        initialValues.put(Schema.SearchHistory.LATITUDE, aLatitude);
        initialValues.put(Schema.SearchHistory.LONGITUDE, aLongitude);

        final long retValue = mDb.insertOrThrow(Schema.SearchHistory.TABLE_NAME, null, initialValues);

        return retValue;
    }

    public void deleteSearchHistory(long aSearchId)
    {
        Log.v("Don", "deleteSearchHistory(): " + aSearchId);
        mDb.delete(Schema.SearchHistory.TABLE_NAME, Schema.SearchHistory.AUTO_ID_PK + " = ?", new String[] {String.valueOf(aSearchId)});
        mDb.close();
    }

    public Cursor getFullSearchHistory()
    {
        Log.v(TAG, "getFullSearchHistory()");

        Cursor data = mDb.rawQuery("SELECT * FROM " + Schema.SearchHistory.TABLE_NAME, null);

        return data;
    }

    public String[] getColumnMappedNames()
    {
        return new String[] {Schema.SearchHistory.SEARCH_STRING, Schema.SearchHistory.DESCRIPTION};
    }

}
