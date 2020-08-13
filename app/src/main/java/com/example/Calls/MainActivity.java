package com.example.Calls;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.Calls.BackEnd.Api.AnalyzeCall;
import com.example.Calls.BackEnd.Api.ApiSpeech;
import com.example.Calls.BackEnd.Api.FileSpeech;
import com.example.Calls.BackEnd.Api.SelectMethodSaveText;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.Permissions;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SavedSettings;
import com.example.Calls.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static android.provider.Telephony.Mms.Part.FILENAME;

public class MainActivity extends AppCompatActivity {

    //region variables

    /*

    private Button buttonInformationAboutContact, buttonGroupUsers;

    private TextView textViewSelectedContact, textViewCountRecords, textViewHelpNoFindRecords, textViewWaitMainActivity;

    private RelativeLayout relativeLayoutWaitMainActivity;

    private SharedPreferences mSettings;

    private String pathForRecords;

    private Records records;

    private List<File> listFiles;

    private List<String> listNames = new ArrayList<String>();

    private String[] strListNames;

    private FileAdapter fileAdapter;

    private static int REQUEST_EXTERNAL_STORAGE = 1;

    private AsyncApi asyncApi;

    private ArrayAdapter<String> adapter;

    public String nameRecord;

     */

    //endregion

    private SavedSettings savedSettings;

    private SharedPreferences mSettings;

    private ListView listViewContactsMA;

    private List<File> listFiles = new ArrayList<File>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewContactsMA = (ListView)(findViewById(R.id.listViewContactsMA));

        mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

        Permissions permissions = new Permissions();
        savedSettings = new SavedSettings(mSettings);

        if(!savedSettings.isNull(SavedSettings.APP_PREFERENCES_ISEXPERT)){
            Intent help = new Intent(MainActivity.this, Help.class);
            startActivity(help);
            return;
        }

        if(!SavedSettings.isExpert()){
            startAlertDialog(0);
        }



        //запрос прав у пользователя
        permissions.EnablePermissions(this);

        //установка пути в настройках
        Records.pathForFindRecords = mSettings.getString("path", Records.currentPathForRecordsXiomi);

        //если текущего пути нету, выводит диалоговое окно !!stopped work application
        if(!Records.checkPath(Records.pathForFindRecords)){
            Toast.makeText(this, Records.pathForFindRecords, Toast.LENGTH_SHORT).show();
            return;
        }

        //add list from selected path
        listFiles.addAll(Records.getFiles(Records.pathForFindRecords));

        //вывод список контактов в list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, this.getListContacts(listFiles));

        listViewContactsMA.setAdapter(adapter);

        //определяет выбранный контакт и переходит на следующую activity
        listViewContactsMA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view;
                Contacts.informationAboutUser = text.getText().toString();
                Intent aboutContact = new Intent(MainActivity.this, AboutContact.class);
                startActivity(aboutContact);
            }
        });


        /*

        textViewCountRecords = (TextView)findViewById(R.id.textViewCountRecords);
        textViewHelpNoFindRecords = (TextView) findViewById(R.id.textViewHelpNoFindRecords);
        buttonGroupUsers = (Button) findViewById(R.id.buttonGroupUsers);

        buttonInformationAboutContact = (Button) findViewById(R.id.buttonInformationAboutContact);
        buttonInformationAboutContact.setVisibility(View.GONE);

        relativeLayoutWaitMainActivity = (RelativeLayout) findViewById(R.id.relativeLayoutWaitMainActivity);

        textViewWaitMainActivity = (TextView) findViewById(R.id.textViewWaitMainActivity);

        asyncApi = new AsyncApi();

        listFiles = new ArrayList<File>();

        records = new Records();

        textViewHelpNoFindRecords.setVisibility(View.GONE);

        Permissions permissions = new Permissions();

        mSettings = getSharedPreferences(Settings.APP_PREFERENCES, Context.MODE_PRIVATE);

        if(mSettings.contains(Settings.APP_PREFERENCES_ISSTART)){

            permissions.EnablePermissions(this);

            Records.pathForFindRecords = mSettings.getString("path", Records.currentPathForRecordsXiomi);

            //проверка на наличие пути для записей на устройстве
            if(!Records.isFileExists(Records.pathForFindRecords)){
                Toast.makeText(this, "Данная директория не найдена на вашем устройстве. Проверьте настройки",Toast.LENGTH_SHORT).show();
                return;
            }

            listFiles.addAll(Records.getFiles(Records.pathForFindRecords));

            //вывод список контактов в list

            adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, this.getListContacts(listFiles));

            listViewRecords.setAdapter(adapter);

        }
        else{
            Intent help = new Intent(MainActivity.this, Help.class);
            startActivity(help);
        }

        //обработка нажатия выбранной записи
        //при выборе записи открывает окно для редактирования
        listViewRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view;
                Contacts.informationAboutUser = text.getText().toString();
                updateListFiles(listFiles);
                startFilesDialog(listFiles);
            }
        });

         */

    }

    //region ButtonsClick

    //открывает диалоговое окно с контекстной справкой для данной страницы
    public void onCLickButtonHelp(View view){
        startAlertDialog(0);
    }

    //открывает окно настроек
    public void onCLickButtonSettings(View view){
        Intent settings = new Intent(MainActivity.this, Settings.class);
        startActivity(settings);
    }


    //endregion


    //region helper Methods

    private void startAlertDialog(int numButton){
        FragmentManager manager = getSupportFragmentManager();
        MyDialogHelp.getButton = numButton;
        MyDialogHelp myDialogHelp = new MyDialogHelp();
        myDialogHelp.show(manager, "myDialog");
    }

    private ArrayList<String> getListContacts(List<File> listFiles){

        String[] listNames;
        listNames = Records.getUniqueList(listFiles);


        ArrayList<String> listContacts = new ArrayList<String>();

        Cursor cursor= this.getContentResolver().query(
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

            listContacts.add(name + " | " + phone + "\nГотовность: " + "20%");
        }

        return listContacts;
    }

    //endregion






    /*

    //region helper Methods
    private void updateListFiles(List<File> listFiles){
        Contacts contacts = new Contacts();
        listFiles.clear();
        listFiles.addAll(Records.getFiles(Records.pathForFindRecords));
        Records.getFilterRecords(listFiles, contacts.getNameCurrentContact());
    }


    private void getListRecords(boolean isSelectContact, String path){

        //фильтрация по контактам
        if(isSelectContact){
            Contacts contacts = new Contacts();
            buttonInformationAboutContact.setVisibility(View.VISIBLE);
            buttonInformationAboutContact.setText("Данные о контакте: " + contacts.getNameCurrentContact());
            Records.getFilterRecords(listFiles,contacts.getNameCurrentContact());
        }

        /*

        fileAdapter = new FileAdapter(this, listFiles);

        listViewRecords.setAdapter(fileAdapter);

        //проверяем является ли список файлов пустым
        if(fileAdapter.isEmpty()){
            textViewHelpNoFindRecords.setVisibility(View.VISIBLE);
            textViewCountRecords.setText(getTextViewCountRecord(0));
            return;
        }

        textViewCountRecords.setText(getTextViewCountRecord(listFiles.size()));



    }

    private void setModeWait(boolean setWait){
        if(setWait){
            listViewRecords.setVisibility(View.GONE);

        }
        else{
            listViewRecords.setVisibility(View.VISIBLE);

        }
    }

    private String getTextViewCountRecord(int count){
        return "Количество найденных записей: " + String.valueOf(count);
    }

    private void startAllRecordsForTranslated(List<File> listFiles) throws Exception{
        ApiSpeech api = new ApiSpeech();
        Contacts contacts;
        for(File file : listFiles){
            Contacts.informationAboutUser = "Имя: " +
                    Records.getNameSelectedRecord(file.getName()) + "\nТелефон: " + records.getPhoneNumberSelectedRecord(file.getName());
            contacts = new Contacts();
            api.SpeechToText(file.getAbsolutePath(), contacts);
        }


    }

    private void saveAllRecords(List<File> listFiles) throws  Exception{
        String translatedText;
        Contacts contacts;
        for(File file : listFiles){
            Contacts.informationAboutUser = "Имя: " +
                    Records.getNameSelectedRecord(file.getName()) + "\nТелефон: " + records.getPhoneNumberSelectedRecord(file.getName());
            contacts = new Contacts();
            translatedText = FileSpeech.ReadFileSpeech(contacts, SelectMethodSaveText.oneMessage);
            Records.saveTranslatedRecord(translatedText, contacts, file.getName());
            if(!Records.isRecordExist(file.getName()))
                Records.writeRecord(file.getName());
        }

    }



    private void startFilesDialog(List<File> listFiles){
        FragmentManager manager = getSupportFragmentManager();
        FilesDialog filesDialog = new FilesDialog();
        filesDialog.setContext(this);
        filesDialog.setListRecords(listFiles);
        filesDialog.show(manager, "myDialog");

    }

    public  void startEditRec(){

        Intent editRec = new Intent(this, EditRec.class);
        editRec.putExtra("nameRecord", nameRecord);
        startActivity(editRec);

    }


    //endregion


    //region ButtonsClick
    public void onClickInformationAboutContact(View view){
        try{
            Intent aboutContact = new Intent(MainActivity.this, AboutContact.class);
            startActivity(aboutContact);
        }
        catch (Exception ex){
            Toast.makeText(this,"Ошибка открытия окна: " + ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickGroupUsers(View view){
        try{

            /*
            listFiles.clear();
            this.getListRecords(false, pathForRecords);
            fileAdapter.notifyDataSetChanged();
            buttonInformationAboutContact.setVisibility(View.GONE);
            Toast.makeText(this, "Выведены все записи", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка выбора группы пользователей: " + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSelectContact(View view){
        try{

            Intent selectContact = new Intent(MainActivity.this, SelectContact.class);
            selectContact.putExtra("size", strListNames.length);
            selectContact.putExtra("listNames", strListNames);
            startActivity(selectContact);
        }
        catch (Exception ex) {
            Toast.makeText(this, "Ошибка открытия окна: " + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }



    public void onClickTranslatedReconrdInText(View view){
        try{
            setModeWait(true);
            int duration = 0;
            for(File file : listFiles){
                duration += AnalyzeCall.getDurationRecord(file);
            }
            textViewWaitMainActivity.setText(AnalyzeCall.setDurationRecords(duration));
            startAllRecordsForTranslated(listFiles);
            asyncApi.execute();
            Toast.makeText(this, "Все записи были сохранены", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, "Ошибка при запуске перевода записи" + ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }



    //endregion

    //region AsynkClass
    class AsyncApi extends AsyncTask<Void,Integer,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                setModeWait(false);
                saveAllRecords(listFiles);
                Toast.makeText(MainActivity.this, "Все записи успешно переведены", Toast.LENGTH_SHORT).show();
                listFiles.clear();
                getListRecords(false, Records.pathForFindRecords);
                fileAdapter.notifyDataSetChanged();
                buttonInformationAboutContact.setVisibility(View.GONE);

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }



    //endregion

    //region menu

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_main_menu, menu);
        return true;
    }

    //change menu item
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_help:
                Intent intent1 = new Intent(MainActivity.this , Help.class);
                startActivity(intent1);
                return  true;
            case R.id.item_settings:
                Intent intent2 = new Intent(MainActivity.this , Settings.class);
                startActivity(intent2);
                return true;
            default:
                return false;
        }

    }

    //endregion


     */

}
