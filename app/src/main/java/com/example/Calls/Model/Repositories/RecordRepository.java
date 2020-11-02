package com.example.Calls.Model.Repositories;

import com.example.Calls.BackEnd.Services.RecordsService;
import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Record;

import java.util.ArrayList;

public class RecordRepository {
    private static ArrayList<Record> ListRecords;
    public ArrayList<Record> getListRecords() {
        return ListRecords;
    }
    public static void setListRecords(ArrayList<Record> records) {
        ListRecords = records;
    }

    private static Record selectedRecord;
    public Record getSelectedRecord(){
        return selectedRecord;
    }
    public void setSelectedRecord(Record record){
        selectedRecord = record;
    }

    public ArrayList<Record> getListFilteredByContact(){
        ArrayList<Record> list = new ArrayList<Record>();
        for(Record record : getListRecords()){
            if(RecordsService.isConstrainNameRecord(ContactRepository.getSelectedContact().Name,record.Contact)) list.add(record);
        }
        return list;
    }

    public ArrayList<String> getDisplayRecords(ArrayList<Record> records){
        ArrayList<String> list = new ArrayList<String >();
        for(Record record : records){
            list.add(record.FullName);
        }
        return list;
    }



}
