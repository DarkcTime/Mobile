package com.example.Calls.Model;


public class Contact implements Comparable<Contact> {
    public String Name;
    public String NumberPhone;
    public int NumberWords;


    //sort in more to less
    @Override
    public int compareTo(Contact contact) {
        return -(this.NumberWords - contact.NumberWords);
    }
}
