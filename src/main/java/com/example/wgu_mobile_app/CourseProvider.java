package com.example.wgu_mobile_app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class CourseProvider extends ContentProvider {

    private static final String COURSE_AUTHORITY = "com.example.wgu_mobile_app-courseprovider";
    private static final String COURSE_BASE_PATH = "courses";
    public static final Uri COURSE_CONTENT_URI = Uri.parse("content://" + COURSE_AUTHORITY + "/" + COURSE_BASE_PATH);

    public static final String CONTENT_ITEM_TYPE = "Course";

    private static final int COURSE = 3;
    private static final int COURSE_ID = 4;

    private static final UriMatcher courseUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        courseUriMatcher.addURI(COURSE_AUTHORITY, COURSE_BASE_PATH, COURSE);
        courseUriMatcher.addURI(COURSE_AUTHORITY, COURSE_BASE_PATH + "/#", COURSE_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {

        selection = selection + "=?";

        return database.query(DBOpenHelper.COURSE_TABLE, DBOpenHelper.ALL_COURSE_COLUMNS, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBOpenHelper.COURSE_TABLE, null, values);
        return Uri.parse(COURSE_BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri,String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.COURSE_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.COURSE_TABLE, values, selection, selectionArgs);
    }
}
