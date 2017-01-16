package com.dw.andro.dictionary.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dvayweb on 01/04/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Favorite Word table
    private static final String DATABASE_NAME = "Dictionary_DB";
    private static final int DATABASE_VERSION = 9;
    public static final String FAVORITE_TABLE = "Favorite_Table";
    public static final String FAVORITE_ID = "Favorite_Id";
    public static final String FAVORITE_WORD = "Word";
    public static final String FAVORITE_WORD_ID = "Word_Id";
    public static final String LEVEL = "Level";

    private static final String DROP_FAVORITE_TABLE = "DROP TABLE IF EXISTS " + FAVORITE_TABLE;
    private static final String CREATE_FAVORITE_TABLE = "CREATE TABLE " + FAVORITE_TABLE +
            " (" + FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FAVORITE_WORD + " VARCHAR(255), " +
            FAVORITE_WORD_ID + " VARCHAR, " + LEVEL + " VARCHAR(5));";

    //Favorite Word description table
    public static final String FAVORITE_DETAIL_TABLE = "Favorite_Detail_Table";
    public static final String FAVORITE_DETAIL_ID = "Detail_id";
    public static final String FAVORITE_MEANING = "Meaning";
    public static final String FAVORITE_DESCRIPTION = "Description";
    public static final String FAVORITE_TRANSLATION = "Translation";
    static final String FOREIGN_FAVORITE_DETAIL_ID = "Favorite_Id";

    private static final String DROP_DETAIL_TABLE = "DROP TABLE IF EXISTS " + FAVORITE_DETAIL_TABLE;
    private static final String CREATE_DETAIL_TABLE = "CREATE TABLE " + FAVORITE_DETAIL_TABLE +
            " (" + FAVORITE_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FAVORITE_MEANING + " VARCHAR(255), " +
            FAVORITE_DESCRIPTION + " VARCHAR(255), " + FAVORITE_TRANSLATION + " VARCHAR(255), " + FOREIGN_FAVORITE_DETAIL_ID + " INTEGER, " +
            "FOREIGN KEY(" + FOREIGN_FAVORITE_DETAIL_ID + ") REFERENCES " + FAVORITE_TABLE + "(" + FAVORITE_WORD_ID + "));";

    //Word of the day table
    public static final String WOD_TABLE = "Wod_Table";
    public static final String WOD_ID = "WOD_Id";
    public static final String WOD_Word_ID = "WOD_Word_Id";
    public static final String WOD_WORD = "Word";
    static final String WOD_DATE = "Date";

    private static final String DROP_WOD_TABLE = "DROP TABLE IF EXISTS " + WOD_TABLE;
    private static final String CREATE_WOD_TABLE = "CREATE TABLE " + WOD_TABLE +
            " (" + WOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+WOD_Word_ID + " VARCHAR, " + WOD_WORD + " VARCHAR(255)," + WOD_DATE + " VARCHAR(255));";

    //Word of the day description table
    public static final String WOD_DETAIL_TABLE = "WOD_Detail_Table";
    public static final String WOD_DETAIL_ID = "Detail_id";
    public static final String WOD_MEANING = "Meaning";
    public static final String WOD_DESCRIPTION = "Description";
    public static final String WOD_TRANSLATION = "Translation";
    static final String FOREIGN_WOD_DETAIL_ID = "WOD_Id";

    private static final String DROP_WOD_DETAIL_TABLE = "DROP TABLE IF EXISTS " + WOD_DETAIL_TABLE;
    private static final String CREATE_WOD_DETAIL_TABLE = "CREATE TABLE " + WOD_DETAIL_TABLE +
            " (" + WOD_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WOD_MEANING + " VARCHAR(255)," + WOD_DESCRIPTION + " VARCHAR(255), " + WOD_TRANSLATION + " VARCHAR(255), " + FOREIGN_WOD_DETAIL_ID + " INTEGER, " +
            "FOREIGN KEY(" + FOREIGN_WOD_DETAIL_ID + ") REFERENCES " + WOD_TABLE + "(" + WOD_ID + "));";

    public static final String SEARCH_TABLE = "Search_Table";
    public static final String SEARCH_ID = "Search_Id";
    public static final String SEARCH_WORD = "Word";
    public static final String SEARCH_WORD_ID = "Word_Id";

    private static final String DROP_SEARCH_TABLE = "DROP TABLE IF EXISTS " + SEARCH_TABLE;
    private static final String CREATE_SEARCH_TABLE = "CREATE TABLE " + SEARCH_TABLE +
            " (" + SEARCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SEARCH_WORD + " VARCHAR(255), " +
            SEARCH_WORD_ID + " VARCHAR);";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_FAVORITE_TABLE);
            db.execSQL(CREATE_DETAIL_TABLE);
            db.execSQL(CREATE_WOD_TABLE);
            db.execSQL(CREATE_WOD_DETAIL_TABLE);
            db.execSQL(CREATE_SEARCH_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL(DROP_FAVORITE_TABLE);
            db.execSQL(DROP_DETAIL_TABLE);
            db.execSQL(DROP_WOD_TABLE);
            db.execSQL(DROP_WOD_DETAIL_TABLE);
            db.execSQL(DROP_SEARCH_TABLE);
            onCreate(db);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
