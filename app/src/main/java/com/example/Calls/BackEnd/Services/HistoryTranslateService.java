package com.example.Calls.BackEnd.Services;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.Model.Record;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryTranslateService {

    final static String pathFile = FileSystemParameters.getPathApplicationFileSystem() + "history.txt";

    public static void writeRecord(Record record) throws Exception{
        FileSystem.WriteFile(pathFile, record.FullName + "\n", true);
    }

    public static ArrayList<String> getHistoryTranslated() throws Exception{
        String result = FileSystem.ReadFile(pathFile);
        String[] array = result.split("\n");
        return new ArrayList<>(Arrays.asList(array));
    }


}
