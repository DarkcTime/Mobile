package com.example.Calls.Model.Repositories;

import com.example.Calls.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    private static ArrayList<Contact> listContacts;
    public static ArrayList<Contact> getListContacts(){
        return listContacts;
    }
    public static void setListContacts(ArrayList<Contact> contacts){
        listContacts = contacts;
    }

    private static Contact selectedContact;
    public static Contact getSelectedContact(){
        return selectedContact;
    }
    public static void setSelectedContact(Contact contact){
        selectedContact = contact;
    }


}
