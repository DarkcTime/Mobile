package com.example.Calls;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Calls.BackEnd.Services.HistoryTranslateService;
import com.example.Calls.BackEnd.Services.ListRecords;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Model.Record;
import com.example.Calls.Model.Repositories.RecordRepository;
import com.example.Calls.Views.RecordAdapter;

import java.util.ArrayList;

public class SelectRecord extends AppCompatActivity {

    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.SelectRecord);

    private ListView listViewRecords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.select_record);

            listViewRecords = (ListView) findViewById(R.id.listViewRecords);

            RecordRepository recordRepository = new RecordRepository();

            ArrayList<Record> listRecords = recordRepository.getDisplayList();
            RecordAdapter recordAdapter = new RecordAdapter(this, R.layout.list_records, listRecords);

            listViewRecords.setAdapter(recordAdapter);

            listViewRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Record selectedRecord = (Record) parent.getItemAtPosition(position);
                    RecordRepository.setSelectedRecord(selectedRecord);
                    Intent intent = new Intent(SelectRecord.this, Play.class);
                    startActivity(intent);

                }
            });

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "SelectRecordOnCreate");
        }
    }

    //Button back
    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(SelectRecord.this, MainActivity.class);
        startActivity(mainActivity);
    }

}
