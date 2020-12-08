package com.example.Calls.Model.Repositories;

import com.example.Calls.BackEnd.SharedClasses.SharedMethods;
import com.example.Calls.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    public static final int MAX_PERCENTAGE = 100;

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

    public int getProgressAsPercentage(Contact contact){
        int percentage = Math.round(contact.NumberWords / 10);
        return Math.min(percentage, MAX_PERCENTAGE);
    }

}
