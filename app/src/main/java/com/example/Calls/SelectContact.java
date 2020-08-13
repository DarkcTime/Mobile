package com.example.Calls;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Contacts;

import java.util.ArrayList;
import java.util.List;

public class SelectContact extends AppCompatActivity {

    private ListView listViewContacts;
    private EditText editTextSearch;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> listContacts = new ArrayList<String>();

    String[] listNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_contact);
        listViewContacts = (ListView)findViewById(R.id.listViewContacts);
        editTextSearch = (EditText)findViewById(R.id.editTextSearch);

        int size = getIntent().getIntExtra("size", 0);
        listNames = new String[size];
        listNames = getIntent().getStringArrayExtra("listNames");


        Cursor cursor= this.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        int count = 0;
        while (cursor.moveToNext()){
            boolean check = false;
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phone = "";

            for (String listName : listNames) {
                check = listName.equals(name);
                if(check){
                    break;
                }
            }
            if(!check) continue;

            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if(hasPhoneNumber > 0){
                Cursor pCur;
                pCur = this.getContentResolver().query(
                        ContactsContract.CommonDataKinds
                                .Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds
                                .Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);

                while (pCur.moveToNext()){
                    phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }

            }

            listContacts.add("Имя: " + name + "\nТелефон: " + phone);
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContacts);

        listViewContacts.setAdapter(adapter);

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView text = (TextView) view;
                Contacts.informationAboutUser = text.getText().toString();

                Intent mainActivity = new Intent(SelectContact.this , MainActivity.class);
                startActivity(mainActivity);
            }
        });


        //поиск
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                SelectContact.this.adapter.getFilter().filter(s);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

}
