package com.example.Calls.BackEnd.Contacts;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Records.Records;
import com.example.Calls.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Класс содержить выбранный контакт
 * И методы для фильтрации
 */

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

    /**
     * Возвращает отфильтрованный список контактов
     * @param listFiles список файлов записей для фильтрации
     * @param mainActivity окно для взаимодействия с Cursor
     * @return список контактов у которых есть записи разговоров
     */
    public static ArrayList<String> getListContacts(List<File> listFiles, MainActivity mainActivity) throws NullPointerException{

        /*
        1) получает список контактов у которых есть записи
        2) Cursor - получаем список всех контактов в Android
        3) Проверяем и отсеиваем контакты у которых нет записей разговоров
        4) Для оставшихся контактов получаем номер телефона
         */
            String[] listNames = getUniqueList(listFiles);

            ArrayList<String> listContacts = new ArrayList<String>();

            @SuppressLint("Recycle") Cursor cursor= mainActivity.getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if(cursor == null) throw new NullPointerException("Список контактов пуст");

            while (cursor.moveToNext()){

                boolean check = false;

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phone = "";

                if(name == null) continue;

                //отсеивает контакты у которых нет записей разговоров
                for (String listName : listNames) {

                    if(Records.isConstrainNameRecord(listName, name)){
                        check = true;
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


    public static boolean isHaveDirectoryForCurrentContact() throws Exception{
        try {
            File dirForSelectedContact = new File(FileSystemParameters.getPathForSelectedContact());
            return dirForSelectedContact.exists();
        }
        catch (Exception ex){
            throw new Exception("Contacts/isHaveDir - ".concat(ex.getMessage()));
        }
    }

    /**
     * Получает уникальный список имен которые содержаться в записи
     * @param listFiles список всех файлов записей
     * @return массив уникальных имен
     */
    private static String[] getUniqueList(List<File> listFiles){
        List<String> listNames = new ArrayList<String>();
        /*
            добавляет имена в коллекцию
            если имя записи не является номером телефона
         */
        for (File file : listFiles){
            String nameRecord = Records.getNameContactInRecord(file.getAbsolutePath());
            if (!isName(nameRecord)) continue;
            listNames.add(nameRecord);
        }
        //создание массива типа String[]
        HashSet<String> hashSetListNames = new HashSet<>(listNames);
        String[] strListNames = new String[hashSetListNames.size()];
        hashSetListNames.toArray(strListNames);
        return  strListNames;
    }


    /**
     * Данный метод определяет
     * является ли переданное имя записи - именем контакта
     * или номером телефона
     * @param name имя записи
     * @return true если это имя
     */
    private static boolean isName(String name){
        if(name.length() < 5) return true;
        try{
            Integer.valueOf(name.substring(0,3));
            return false;
        }
        catch (Exception ex){
            Log.d("isName", ex.getMessage());
            return true;
        }

    }




    //endregion



}
