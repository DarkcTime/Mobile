package com.example.Calls.BackEnd.Contacts;


import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.Calls.BackEnd.Records.Records;
import com.example.Calls.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//прости меня господи за этот класс
public class Contacts {


    private static String informationAboutUser = "";

    public static void setInformationAboutUser(String _informationAboutUser){
        informationAboutUser = _informationAboutUser;
    }

    private static String getInformationAboutUser(){
        return informationAboutUser;
    }

    public static String getNameCurrentContact() {
        int startName = 0, endName = getInformationAboutUser().indexOf("|") - 1;
        return getInformationAboutUser().substring(startName, endName);
    }

    public static String getPhoneNumberCurrentContact() {

        int startPhone = getInformationAboutUser().indexOf("|") + 1;
        int endPhone = getInformationAboutUser().indexOf("\n");
        return getInformationAboutUser().substring(startPhone, endPhone);

    }

    public String getCountMinuteStr(int countMinute, int countSecond){
        String cm, cs;
        if(countMinute < 10){
            cm = "0" + String.valueOf(countMinute);
        }
        else{
            cm = String.valueOf(countMinute);
        }
        if(countSecond < 10){
            cs = "0" + String.valueOf(countSecond);
        }
        else{
            cs = String.valueOf(countSecond);
        }

        return "Количество минут: " + cm + ":" + cs;
    }
    public String getPsyProfileStr(String profile){
        return  "Психологический тип: " + profile;
    }

    //TODO рефакторить данный метод
    //TODO добавить логику для получения процентов пользователем
    //возвращает список контактов, записи которых были найдены
    public static ArrayList<String> getListContacts(List<File> listFiles, MainActivity mainActivity){

        String[] listNames;
        listNames = Records.getUniqueList(listFiles);


        ArrayList<String> listContacts = new ArrayList<String>();

        Cursor cursor= mainActivity.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        int count = 0;
        while (cursor.moveToNext()){
            boolean check = false;
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phone = "";

            if(name == null) continue;

            //TODO Влад: протестировать логику во всех ситуациях, поставить максимальное число символов при выводе контакта
            for (String listName : listNames) {

                if(Records.isConstrainNameRecord(listName, name, 10)){
                    check = true;
                    break;
                }

                check = listName.equals(name);

                if(check){
                    break;
                }
            }
            if(!check) continue;

            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if(hasPhoneNumber > 0){
                Cursor pCur;
                pCur = mainActivity.getContentResolver().query(
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

            listContacts.add(name + " | " + phone + "\nГотовность: " + "20%");
        }

        return listContacts;
    }

    //endregion



}
