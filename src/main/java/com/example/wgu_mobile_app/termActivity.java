package com.example.wgu_mobile_app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class termActivity extends AppCompatActivity {

    private String action;
    private TextInputEditText editor;

    private String termFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        editor = findViewById(R.id.createTermInput);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(TermProvider.CONTENT_ITEM_TYPE);

        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_term);
        } else {
            action = Intent.ACTION_EDIT;
            termFilter = DBOpenHelper.TERM_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, DBOpenHelper.ALL_TERMS_COLUMNS, termFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_TITLE));
            editor.setText(oldText);
            editor.requestFocus();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        if(action.equals(Intent.ACTION_EDIT)){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_activity_term, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteTerm();
                break;
        }
        return true;
    }

    private void deleteTerm() {
        getContentResolver().delete(TermProvider.CONTENT_URI, termFilter, null);

        Toast.makeText(this, getString(R.string.term_deleted), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void finishEditing(){
        String newText = editor.getText().toString().trim();
        switch(action){
            case Intent.ACTION_INSERT:
                if(newText.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertTerm(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if(newText.length() == 0){
                    deleteTerm();
                } else if(oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(newText);
                }
        }
        finish();
    }

    private void updateNote(String newText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, newText);
        getContentResolver().update(TermProvider.CONTENT_URI, values, termFilter, null);

        Toast.makeText(this, "Term Updated", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertTerm(String newText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_TITLE, newText);
        getContentResolver().insert(TermProvider.CONTENT_URI, values);

        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed(){
        finishEditing();
    }
}
