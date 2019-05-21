package com.example.dwest;


import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;


public class CallList extends AppCompatActivity
{
    DatabaseHelper db;

    ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setEmptyView(findViewById(R.id.empty));
        db = new DatabaseHelper(this);

        fillListView();
    }

    private void fillListView() {
        Cursor data = db.getData();

        ArrayList<CallData> listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(new CallData(data.getString(0), data.getString(1), data.getString(2), data.getString(3)));
        }

        CustomArrayAdapter adapter = new CustomArrayAdapter(this, listData);
        mListView.setAdapter(adapter);
    }

}
