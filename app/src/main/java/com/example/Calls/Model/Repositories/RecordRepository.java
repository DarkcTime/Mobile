package com.example.Calls.Model.Repositories;

import android.util.Log;

import com.example.Calls.BackEnd.Services.HistoryTranslateService;
import com.example.Calls.BackEnd.Services.RecordsService;
import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class RecordRepository {
    private static ArrayList<Record> ListRecords;
    public ArrayList<Record> getListRecords() {
        return ListRecords;
    }
    public static void setListRecords(ArrayList<Record> records) {
        ListRecords = records;
    }

    private static Record selectedRecord;
    public static Record getSelectedRecord(){
        return selectedRecord;
    }
    public static void setSelectedRecord(Record record){
        selectedRecord = record;
    }

    public ArrayList<Record> getDisplayList(){
        ArrayList<Record> list = getListFilteredByContact();
        orderByDateTime(list);
        return list;
    }
    private ArrayList<Record> getListFilteredByContact(){
        ArrayList<Record> list = new ArrayList<Record>();
        for(Record record : getListRecords()){
            if(RecordsService.isConstrainNameRecord(ContactRepository.getSelectedContact().Name,record.Contact)) list.add(record);
        }
        filterHistory(list);
        return list;
    }
    private void orderByDateTime(ArrayList<Record> records){
        Collections.sort(records);
    }
    private void filterHistory(ArrayList<Record> records){
        try {
            Iterator<Record> iterator = records.iterator();

            ArrayList<String> history = HistoryTranslateService.getHistoryTranslated();
            while (iterator.hasNext()) {
                String fullName = iterator.next().FullName;
                for(String his : history){
                    if(fullName.equals(his)){
                        iterator.remove();
                    };
                }
            }
        }
        catch (Exception ex){
            Log.d("filterHistory", ex.getMessage());
        }
    }
}
