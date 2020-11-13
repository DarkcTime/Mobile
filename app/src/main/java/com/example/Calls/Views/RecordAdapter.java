package com.example.Calls.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Record;
import com.example.Calls.R;

import java.util.List;

public class RecordAdapter extends ArrayAdapter<Record> {

    private LayoutInflater inflater;
    private int layout;
    private List<Record> records;

    public RecordAdapter(Context context, int resource, List<Record> records){
        super(context,resource,records);
        this.records = records;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);

    }
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(this.layout, parent, false);



        TextView dateTime = (TextView) view.findViewById(R.id.textViewDateTime);
        TextView duration = (TextView) view.findViewById(R.id.textViewDuration);

        Record record = records.get(position);
        String dt = record.Date + "  " + record.Time;
        String dur = "Длительность: " + record.Duration;

        dateTime.setText(dt);
        duration.setText(dur);
        return view;
    }
}
