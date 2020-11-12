package com.example.Calls.Model.Repositories;

import com.example.Calls.BackEnd.SharedClasses.SharedMethods;
import com.example.Calls.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    public static final int MAX_PERCENTAGE = 100;
    public static final int MAX_NUMBERS = 20;

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

    //TODO make percentage correnct
    public int getProgressAsPercentage(Contact contact){
        if(contact.Name.equals("Айна")){
            return 100;
        }
        return Math.round(contact.NumberWords);
    }


}
