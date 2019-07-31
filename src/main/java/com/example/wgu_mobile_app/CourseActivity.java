package com.example.wgu_mobile_app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class CourseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDITOR_REQUEST_CODE = 1000;
    private CursorAdapter cursorAdapter;
    private String[] selectionArgs = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course);

        long termId;
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CourseProvider.CONTENT_ITEM_TYPE);
        termId = getIntent().getExtras().getLong("itemId");
        String stringTermId = Long.toString(termId);
        String[] courseColumn = {DBOpenHelper.COURSE_NAME};

        selectionArgs[0] = stringTermId;
        Cursor cursor = getContentResolver().query(uri, courseColumn, DBOpenHelper.COURSE_TERM_ID, selectionArgs, null);

        cursorAdapter = new courseCursorAdaptor(this, cursor, 1);

        final ListView list = findViewById(R.id.id_courseList);
        list.setAdapter(cursorAdapter);
        System.out.println("This is the adapter: " + list.getAdapter());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseActivity.this, editCourseActivity.class);
                Uri uri = Uri.parse(CourseProvider.COURSE_CONTENT_URI + "/" + id);

                intent.putExtra(CourseProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        LoaderManager.getInstance(this).initLoader(0, null, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_sample:
          //do stuff
                break;
            case R.id.action_delete_all:
//do stuff
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void restartLoader() { LoaderManager.getInstance(this).restartLoader(0, null, this); }

    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this, CourseProvider.COURSE_CONTENT_URI,
                DBOpenHelper.ALL_COURSE_COLUMNS, DBOpenHelper.COURSE_TERM_ID + "=?", selectionArgs, null);    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void openEditorForNewCourse(View view){
        Intent intent = new Intent(CourseActivity.this, editCourseActivity.class);
        long termId = getIntent().getExtras().getLong("itemId");
        intent.putExtra("itemId", termId);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }

}
