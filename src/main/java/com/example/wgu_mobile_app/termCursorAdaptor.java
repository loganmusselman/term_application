package com.example.wgu_mobile_app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class termCursorAdaptor extends CursorAdapter {


    public termCursorAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.term_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String termText = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.TERM_TITLE));
        int pos = termText.indexOf(10);
        if(pos != -1){
            termText = termText.substring(0, pos) + "...";
        }
        TextView tv = view.findViewById(R.id.tvTerm);
        tv.setText(termText);

    }


}
