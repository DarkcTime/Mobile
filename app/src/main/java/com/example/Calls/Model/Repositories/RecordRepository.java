package com.example.Calls.Model.Repositories;

import com.example.Calls.BackEnd.Services.RecordsService;
import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
        return list;
    }
    private void orderByDateTime(ArrayList<Record> records){
        Collections.sort(records);
    }
}
