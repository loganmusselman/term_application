package com.example.wgu_mobile_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    // Constants for database name and version
    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying tables and columns
    //TERMS_TABLE
    public static final String TERMS_TABLE = "terms";
    //TERMS_TABLE COLUMNS
    public static final String TERM_ID = "_id";
    public static final String TERM_TITLE = "term_title";
    public static final String TERM_START = "term_start_date";
    public static final String TERM_END = "term_end_date";

    //COURSE_TABLE
    public static final String COURSE_TABLE  = "courses";
    //COURSE_TABLE COLUMNS
    public static final String COURSE_ID = "_id";
    public static final String COURSE_TERM_ID = "course_term_id";
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_START_DATE = "course_start_date";
    public static final String COURSE_END_DATE = "course_end_date";
    public static final String COURSE_STATUS = "course_status";
    public static final String COURSE_MENTOR_NAME = "course_mentor_name";
    public static final String COURSE_MENTOR_PHONE = "course_mentor_phone";
    public static final String COURSE_MENTOR_EMAIL = "course_mentor_email";

    //NOTES_TABLE
    public static final String NOTES_TABLE = "notes";
    //NOTES TABLE COLUMNS
    public static final String NOTES_ID = "_id";
    public static final String NOTES_MESSAGE = "notes_message";
    public static final String NOTES_COURSE_ID = "notes_class_id";


    public static final String[] ALL_TERMS_COLUMNS = {
            TERM_ID, TERM_TITLE, TERM_START, TERM_END
    };

    public static final String[] ALL_COURSE_COLUMNS = {
            COURSE_ID, COURSE_NAME, COURSE_START_DATE, COURSE_END_DATE,
            COURSE_STATUS, COURSE_MENTOR_NAME, COURSE_MENTOR_PHONE, COURSE_MENTOR_EMAIL, COURSE_TERM_ID
    };

    public static final String[] ALL_NOTES_COLUMNS = {
            NOTES_ID, NOTES_MESSAGE, NOTES_COURSE_ID
    };

    //CREATE TABLES FOR EACH ENTITY
    private static final String TERMS_TABLE_CREATE =
            "CREATE TABLE " + TERMS_TABLE + " (" +
                    TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_TITLE + " TEXT, " +
                    TERM_START + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    TERM_END + " TEXT DEFAULT CURRENT_TIMESTAMP" + ")";

    private static final String COURSE_TABLE_CREATE =
            "CREATE TABLE " + COURSE_TABLE + " (" +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_TERM_ID + " INTEGER, " +
                    COURSE_NAME + " TEXT, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_START_DATE + " TEXT default CURRENT_TIMESTAMP, " +
                    COURSE_END_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    COURSE_MENTOR_NAME + " TEXT, " +
                    COURSE_MENTOR_EMAIL + " TEXT, " +
                    COURSE_MENTOR_PHONE + " INTEGER, " +
                    " FOREIGN KEY (" + COURSE_TERM_ID + ") REFERENCES " + TERMS_TABLE + "(" + TERM_ID + ")" + ")";

    public static final String NOTES_TABLE_CREATE =
            "CREATE TABLE " + NOTES_TABLE + " (" +
                    NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTES_MESSAGE + " TEXT, " +
                    NOTES_COURSE_ID + " INTEGER, " +
                    " FOREIGN KEY (" + NOTES_COURSE_ID + " ) REFERENCES " + COURSE_TABLE + "( "+ COURSE_ID + " )" + " );";



    public DBOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(TERMS_TABLE_CREATE);
        db.execSQL(COURSE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TERMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        onCreate(db);
    }

}