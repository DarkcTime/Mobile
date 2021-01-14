package com.example.Calls.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.Calls.Model.Record;
import com.example.Calls.R;

import java.io.File;
import java.util.List;

public class AllRecordsAdapter extends ArrayAdapter<File> {

    private LayoutInflater inflater;
    private int layout;
    private List<File> records;

    public AllRecordsAdapter(Context context, int resource, List<File> records){
        super(context,resource,records);
        this.records = records;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);

    }
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(this.layout, parent, false);

        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);

        File record = records.get(position);

        textViewName.setText(record.getName());

        return view;
    }
}
