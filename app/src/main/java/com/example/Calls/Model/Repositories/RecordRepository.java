package com.example.Calls.Model.Repositories;

import com.example.Calls.BackEnd.Records.ListRecords;
import com.example.Calls.Model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordRepository {
    private static ArrayList<Record> ListRecords;
    public static ArrayList<Record> getListRecords() {
        return ListRecords;
    }
    public static void setListRecords(ArrayList<Record> records) {
        ListRecords = records;
    }

    //TODO method on work
    public static Record findRecord(String dateTime) {
        return new Record();
    }

}
