package com.example.Calls;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class SelectRecord extends AppCompatActivity {

    private ListView listViewRecords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_record);

        listViewRecords = (ListView) findViewById(R.id.listViewRecords);


    }


}
