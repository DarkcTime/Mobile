package com.example.Calls.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.MainActivity;
import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Repositories.ContactRepository;
import com.example.Calls.PsychologicalPortrait;
import com.example.Calls.R;
import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ContactRepository contactRepository = new ContactRepository();

    private MainActivity mainActivity;

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Contact> originalData;
    private ArrayList<Contact> filteredData;
    private ItemFilter itemFilter = new ItemFilter();

    private final int MAX_PROGRESS = 100;

    public ContactAdapter(Context context, int resource, ArrayList<Contact> contacts){
        super(context,resource,contacts);
        this.mainActivity = (MainActivity) context;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.originalData = contacts;
        this.filteredData = contacts;

    }
    public View getView(int position, View convertView, ViewGroup parent){
        @SuppressLint("ViewHolder") View view = inflater.inflate(this.layout, parent, false);

        TextView nameContact = (TextView) view.findViewById(R.id.textViewNameContact);
        com.github.lzyzsd.circleprogress.CircleProgress progressBar =
                (com.github.lzyzsd.circleprogress.CircleProgress) view.findViewById(R.id.circleProgress);
        final Contact contact = filteredData.get(position);

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactRepository.setSelectedContact(contact);
                Intent intent = new Intent(mainActivity, PsychologicalPortrait.class);
                mainActivity.startActivity(intent);
            }
        });

        int progress = contactRepository.getProgressAsPercentage(contact);

        if(progress >= MAX_PROGRESS){
            progress = MAX_PROGRESS;
            progressBar.setFinishedColor(R.color.light_green);
        }

        progressBar.setProgress(progress);

        nameContact.setText(contact.Name);

        return view;
    }

    public void onClickProgressBarContact(View view){

    }

    @Override
    public int getCount() {
        return filteredData.size();
    }
    @Override
    public Contact getItem(int position) {
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
                 ArrayList<Contact> filterResultsData = new ArrayList<Contact>();

                 for(Contact data : originalData)
                 {
                     //In this loop, you'll filter through originalData and compare each item to charSequence.
                     //If you find a match, add it to your new ArrayList
                     //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                     String nameContact = data.Name.toLowerCase();
                     String constr = constraint.toString().toLowerCase();
                     if(nameContact.contains(constr))
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
             ArrayList<Contact> bufferList = (ArrayList<Contact>)results.values;
             filteredData = bufferList;
             notifyDataSetChanged();
         }
     }
}

