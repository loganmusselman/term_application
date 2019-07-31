package com.example.wgu_mobile_app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class TermProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.wgu_mobile_app-termprovider";
    private static final String BASE_PATH =  "terms";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_ITEM_TYPE = "Term";

    private static final int TERM = 1;
    private static final int TERM_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, TERM);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TERM_ID);
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

        if(uriMatcher.match(uri) == TERM_ID){
            selection = DBOpenHelper.TERM_ID + "=" + uri.getLastPathSegment();
        }

        return database.query(DBOpenHelper.TERMS_TABLE, DBOpenHelper.ALL_TERMS_COLUMNS, selection, null, null, null,
                DBOpenHelper.TERM_ID + " ASC;");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = database.insert(DBOpenHelper.TERMS_TABLE, null, values);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri,String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TERMS_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TERMS_TABLE, values, selection, selectionArgs);
    }
}
