package com.example.Calls;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.Calls.BackEnd.Records;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends ArrayAdapter<File> {

    public FileAdapter(Context context, List<File> files) {
        super(context, android.R.layout.simple_list_item_1, files);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        TextView view = (TextView) super.getView(position, convertView, parent);
        File file = getItem(position);
        assert file != null;
        try{
            view.setText(file.getName() + addType(isRecordTranslated(file)));
        }
        catch (Exception ex){
            return  view;
        }
        return view;
    }

    public String addType(boolean isRecordTranslated){
        return isRecordTranslated ? "\n(Переведено)" : "\n(Не переведено)";
    }

    public boolean isRecordTranslated(File file) throws Exception{
        List<String> list = new ArrayList<String>();
        list = Records.readRecord();
        boolean check = false;
        for(String obj : list){
            check = obj.equals(file.getName());
            if(check) break;
        }
        return check;
    }





}