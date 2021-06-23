package com.example.Calls;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Calls.BackEnd.Services.HistoryTranslateService;
import com.example.Calls.BackEnd.Services.ListRecords;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Model.Record;
import com.example.Calls.Model.Repositories.ContactRepository;
import com.example.Calls.Model.Repositories.RecordRepository;
import com.example.Calls.Views.RecordAdapter;

import java.util.ArrayList;

public class SelectRecord extends AppCompatActivity {

    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.SelectRecord);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.select_record);

            LinearLayout linerLayoutSelectRecord, linerLayoutIfHaveNotRecords;
            linerLayoutSelectRecord = (LinearLayout) (findViewById(R.id.linerLayoutSelectRecord));
            linerLayoutIfHaveNotRecords = (LinearLayout) (findViewById(R.id.linerLayoutIfHaveNotRecords));

            TextView textViewSelectedContact = (TextView) findViewById(R.id.textViewSelectedContact);
            textViewSelectedContact.setText(ContactRepository.getSelectedContact().Name);

            ListView listViewRecords;
            listViewRecords = (ListView) findViewById(R.id.listViewRecords);

            RecordRepository recordRepository = new RecordRepository();
            ArrayList<Record> listRecords = recordRepository.getDisplayList();

            if (listRecords.size() != 0) {
                linerLayoutSelectRecord.setVisibility(View.VISIBLE);

                RecordAdapter recordAdapter = new RecordAdapter(this, R.layout.list_records, listRecords);
                //list records
                listViewRecords.setAdapter(recordAdapter);
                //open play activity
                listViewRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //set selectedRecord
                        Record selectedRecord = (Record) parent.getItemAtPosition(position);
                        RecordRepository.setSelectedRecord(selectedRecord);
                        //open Play
                        Intent intent = new Intent(SelectRecord.this, Play.class);
                        startActivity(intent);
                    }
                });
            } else {
                linerLayoutIfHaveNotRecords.setVisibility(View.VISIBLE);
            }

        } catch (NullPointerException nullPointerException) {
            dialogMain.showErrorDialogAndTheOutputLogs(nullPointerException, "SelectRec - NullPointEx");
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "SelectRecordOnCreate");
        }
    }


    //Open profile with text for contact
    public void onClickOpenProfile(View view){
        Intent profile = new Intent(SelectRecord.this, PsychologicalPortrait.class);
        startActivity(profile);
    }

    //Button help
    public void onClickButtonHelpForGetRecords(View view){
        //TODO открытие справки
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }


    public void onClickButtonBackSelectedPlay(View view) {
        openMainActivity();
    }

    //Button back
    @Override
    public void onBackPressed() {
        openMainActivity();
    }

    private void openMainActivity() {
        Intent mainActivity = new Intent(SelectRecord.this, MainActivity.class);
        startActivity(mainActivity);
    }

}
