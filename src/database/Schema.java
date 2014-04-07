package database;

final class Schema
{
    // This class cannot be instantiated
    private Schema() {}

    /**
     * SearchHistory table. Stores search history information. *
     */
    static final class SearchHistory
    {
        static final String TABLE_NAME = "searchHistory";

        // This class cannot be instantiated
        private SearchHistory() {}

        // Columns
        static final String AUTO_ID_PK = "_id"; //Primary key
        static final String SEARCH_STRING = "search_string";
        static final String DESCRIPTION = "description";


        static final String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
                + AUTO_ID_PK + " INTEGER PRIMARY KEY,"
                + SEARCH_STRING + " TEXT NOT NULL,"
                + DESCRIPTION + " TEXT NOT NULL"
                + ");";
    }
}
