package com.example.wgu_mobile_app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class editCourseActivity extends AppCompatActivity {

    private String action;
    private String courseFilter;
    private TextInputEditText editorCourseName;
    private EditText editorCourseStartDate;
    private EditText editorCourseEndDate;
    public long termId;

    private String oldName;
    private String oldStartDate;
    private String oldEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(CourseProvider.CONTENT_ITEM_TYPE);

        editorCourseName = findViewById(R.id.courseName);
        editorCourseStartDate = findViewById(R.id.startDate);
        editorCourseEndDate = findViewById(R.id.endDate);

        if(uri == null){
            action = Intent.ACTION_INSERT;
            setTitle("New Course");
        } else {
            action = Intent.ACTION_EDIT;
            courseFilter = DBOpenHelper.COURSE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, DBOpenHelper.ALL_COURSE_COLUMNS, courseFilter, null,null);
            cursor.moveToFirst();



            oldName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NAME));
            oldStartDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_START_DATE));
            oldEndDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_END_DATE));

            editorCourseName.setText(oldName);
            editorCourseStartDate.setText(oldStartDate);
            editorCourseEndDate.setText(oldEndDate);

            editorCourseName.requestFocus();

        }

    }

    private void insertCourse(String newCourseName, String newCourseStartDate, String newCourseEndDate, Long termNum) {
        ContentValues values = new ContentValues();

        System.out.println("Term ID inserted as: " + termNum);

        values.put(DBOpenHelper.COURSE_NAME, newCourseName);
        values.put(DBOpenHelper.COURSE_START_DATE, newCourseStartDate);
        values.put(DBOpenHelper.COURSE_END_DATE, newCourseEndDate);
        values.put(DBOpenHelper.COURSE_TERM_ID, termNum);
        getContentResolver().insert(CourseProvider.COURSE_CONTENT_URI, values);

        System.out.println(values.toString());

        setResult(RESULT_OK);

        Toast.makeText(this, getString(R.string.course_created), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void finishEditing(){
        String newCourseName = editorCourseName.getText().toString().trim();
        String newCourseStartDate = editorCourseStartDate.getText().toString().trim();
        String newCourseEndDate = editorCourseEndDate.getText().toString().trim();

        termId = getIntent().getExtras().getLong("itemId");

        System.out.println("found term ID: " + termId);

        switch(action){
            case Intent.ACTION_INSERT:
                if(newCourseName.length() == 0){
                    setResult(RESULT_CANCELED);
                } else {
                    insertCourse(newCourseName, newCourseStartDate, newCourseEndDate, termId);
                }
                break;
            case Intent.ACTION_EDIT:
                if(newCourseName.length() == 0){
                    deleteCourse();
                } else {
                    updateCourse(newCourseName, newCourseStartDate, newCourseEndDate);
                }
        }
        finish();
    }

    private void updateCourse(String newCourseName, String newCourseStartDate, String newCourseEndDate) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NAME, newCourseName);
        values.put(DBOpenHelper.COURSE_START_DATE, newCourseStartDate);
        values.put(DBOpenHelper.COURSE_END_DATE, newCourseEndDate);

        getContentResolver().update(CourseProvider.COURSE_CONTENT_URI, values, courseFilter, null);

        Toast.makeText(this, "Course Updated", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void deleteCourse() {
        getContentResolver().delete(CourseProvider.COURSE_CONTENT_URI, courseFilter, null);

        Toast.makeText(this, getString(R.string.course_cancelled), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteCourse();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){ finishEditing();}

    public void cancelCourse(View view) {
        Toast.makeText(this, getString(R.string.course_cancelled), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();

    }

    public void insertCourse(View view) {
        String newCourseName = editorCourseName.getText().toString().trim();
        String newCourseStartDate = editorCourseStartDate.getText().toString().trim();
        String newCourseEndDate = editorCourseEndDate.getText().toString().trim();

        insertCourse(newCourseName, newCourseStartDate, newCourseEndDate, termId);
    }
}
