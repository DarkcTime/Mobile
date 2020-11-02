package com.example.Calls.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.Calls.Model.Contact;
import com.example.Calls.R;

import org.w3c.dom.Text;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private LayoutInflater inflater;
    private int layout;
    private List<Contact> contacts;

    public ContactAdapter(Context context, int resource, List<Contact> contacts){
        super(context,resource,contacts);
        this.contacts = contacts;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);

    }
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(this.layout, parent, false);

        TextView nameContact = (TextView) view.findViewById(R.id.textViewNameContact);
        Contact contact = contacts.get(position);

        nameContact.setText(contact.Name);

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}
