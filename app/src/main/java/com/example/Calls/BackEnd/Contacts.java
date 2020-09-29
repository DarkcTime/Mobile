package com.example.Calls.BackEnd;



//прости меня господи за этот класс
public class Contacts {

    public static String informationAboutUser = "";

    private String nameCurrentContact, phoneNumberCurrentContact;

    public String getNameCurrentContact() {
        return nameCurrentContact;
    }

    public  String getPhoneNumberCurrentContact() {
        return phoneNumberCurrentContact;
    }

    private String getNameCurrentContact(String strAboutPerson) {
        int startName = 0, endName = strAboutPerson.indexOf("|") - 1;
        String name = strAboutPerson.substring(startName, endName);
        return name;
    }

    private  String getPhoneNumberCurrentContact(String strAboutPerson) {

        int startPhone = strAboutPerson.indexOf("|") + 1;
        int endPhone = strAboutPerson.indexOf("\n");
        String phone = strAboutPerson.substring(startPhone, endPhone);
        return phone;
    }

    public Contacts(){
        nameCurrentContact = getNameCurrentContact(informationAboutUser);
        phoneNumberCurrentContact = getPhoneNumberCurrentContact(informationAboutUser);
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



    //TODO Ваня: метод в который можно положить вывод всей информации о звонках
    public String getAllRecordsCalls(String test){
        return "";
    }

}
