package database;

public final class Schema
{
    // This class cannot be instantiated
    private Schema() {}

    /**
     * SearchHistory table. Stores search history information. *
     */
    public static final class SearchHistory
    {
        static final String TABLE_NAME = "searchHistory";

        // This class cannot be instantiated
        private SearchHistory() {}

        // Columns
        public static final String AUTO_ID_PK = "_id"; //Primary key
        public static final String SEARCH_STRING = "search_string";
        public static final String DESCRIPTION = "description";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";


        static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
                + AUTO_ID_PK + " INTEGER PRIMARY KEY,"
                + SEARCH_STRING + " TEXT NOT NULL,"
                + DESCRIPTION + " TEXT NOT NULL,"
                + LATITUDE + " TEXT NOT NULL,"
                + LONGITUDE + " TEXT NOT NULL"
                + ");";
    }
}
