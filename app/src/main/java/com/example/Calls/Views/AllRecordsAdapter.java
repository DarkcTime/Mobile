package com.example.Calls.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Record;
import com.example.Calls.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllRecordsAdapter extends ArrayAdapter<File> {

    private final LayoutInflater inflater;
    private final int layout;


    private ArrayList<File> originalData;
    private ArrayList<File> filteredData;
    private AllRecordsAdapter.ItemFilter itemFilter = new AllRecordsAdapter.ItemFilter();

    public AllRecordsAdapter(Context context, int resource, ArrayList<File> records){
        super(context,resource,records);
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.originalData = records;
        this.filteredData = records;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(this.layout, parent, false);

        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);

        File record = originalData.get(position);

        textViewName.setText(record.getName());

        return view;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }
    @Override
    public File getItem(int position) {
        return filteredData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Filter getFilter() {
        return itemFilter;
    }
    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0)
            {
                results.values = originalData;
                results.count = originalData.size();
            }
            else
            {
                ArrayList<File> filterResultsData = new ArrayList<File>();

                for(File data : originalData)
                {
                    //In this loop, you'll filter through originalData and compare each item to charSequence.
                    //If you find a match, add it to your new ArrayList
                    //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                    String fileName = data.getPath();
                    String constr = constraint.toString().toLowerCase();
                    if(fileName.contains(constr))
                    {
                        filterResultsData.add(data);
                    }
                }

                results.values = filterResultsData;
                results.count = filterResultsData.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<File> bufferList = (ArrayList<File>)results.values;
            filteredData = bufferList;
            notifyDataSetChanged();
        }
    }



}
