package com.example.wgu_mobile_app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class courseCursorAdaptor extends CursorAdapter {
    public courseCursorAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.course_list_item, parent, false
        );
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String courseText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NAME));
        int pos = courseText.indexOf(10);
        if (pos != -1) {
            courseText = courseText.substring(0, pos) + "...";
        }
        TextView courseTv = view.findViewById(R.id.tvCourse);
        courseTv.setText(courseText);
        System.out.println("This is the text that is in the list now: " + courseTv.getText());

    }
}
