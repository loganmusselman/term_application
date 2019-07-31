package com.example.wgu_mobile_app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDITOR_REQUEST_CODE = 100;
    private CursorAdapter cursorAdapter;
    public long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cursorAdapter = new termCursorAdaptor(this, null, 0);

        final ListView list = this.findViewById(R.id.id_termList);
        list.setAdapter(cursorAdapter);
        registerForContextMenu(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, termActivity.class);
                Uri uri = Uri.parse(TermProvider.CONTENT_URI + "/" + id);
                intent.putExtra(TermProvider.CONTENT_ITEM_TYPE, uri);
                itemId = id;
                openContextMenu(list);

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
                insertTerm();
                break;
            case R.id.action_delete_all:
                deleteAllTerms();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllTerms() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){

                            getContentResolver().delete(
                                    TermProvider.CONTENT_URI, null, null
                            );
                            restartLoader();

                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void insertTerm() {
        insertTerm("First Term");
        insertTerm("Second Term");
        insertTerm("Third Term");

        restartLoader();
    }

    private void restartLoader() {
        LoaderManager.getInstance(this).restartLoader(0, null, this);
    }



    private void insertTerm(String termTitle){
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, termTitle);
        Uri noteUri = getContentResolver().insert(TermProvider.CONTENT_URI, values);

        assert noteUri != null;
        Log.d("MainActivity", "Inserted term " + noteUri.getLastPathSegment());
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this, TermProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void openEditorForNewTerm(View view) {
        Intent intent = new Intent(this, termActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK){
            restartLoader();
        }
    }

    //create context menu when clicking on individual item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.term_course_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_edit_term:
                Intent editTermIntent = new Intent(this, termActivity.class);
                System.out.println("Edit Term id: " + itemId);
                Uri editTermUri = Uri.parse(TermProvider.CONTENT_URI + "/" + itemId);
                editTermIntent.putExtra(TermProvider.CONTENT_ITEM_TYPE, editTermUri);
                startActivityForResult(editTermIntent, EDITOR_REQUEST_CODE);
                return true;
            case R.id.action_create_course:
                System.out.println("Edit Term id: " + itemId);
                Intent createCourseIntent = new Intent(this, CourseActivity.class);
                Uri createCourseUri = Uri.parse(CourseProvider.COURSE_CONTENT_URI + "/" + itemId);
                createCourseIntent.putExtra("itemId", itemId);
                createCourseIntent.putExtra(CourseProvider.CONTENT_ITEM_TYPE, createCourseUri);
                startActivityForResult(createCourseIntent, EDITOR_REQUEST_CODE);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
