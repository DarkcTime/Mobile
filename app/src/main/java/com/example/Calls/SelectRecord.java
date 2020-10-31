package com.example.Calls;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.BackEnd.Records.ListRecords;
import com.example.Calls.Dialog.DialogMain;

import java.util.logging.Level;

public class SelectRecord extends AppCompatActivity {

    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.MainActivity);

    private ListView listViewRecords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.select_record);

            listViewRecords = (ListView) findViewById(R.id.listViewRecords);
            ListRecords listRecords = new ListRecords();

            ArrayAdapter<String> adapterContacts = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,listRecords.getListRecords());

            listViewRecords.setAdapter(adapterContacts);

            listViewRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView text = (TextView) view;

                    Intent intent = new Intent(SelectRecord.this, Play.class);
                    startActivity(intent);

                }
            });

        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "SelectRecordOnCreate");
        }
    }


}
