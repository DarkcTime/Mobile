package com.example.Calls;

//region import
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Permissions;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SavedSettings;
import com.example.Calls.BackEnd.SharedVariables;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
//endregion

//логика для главного окна в приложении
public class MainActivity extends AppCompatActivity {

    //объект работы с настройками
    private SharedPreferences mSettings;

    //выводит список контактов пользователю
    private ListView listViewContactsMA;

    //получает список файлов записей с расширениями mp.3
    private List<File> listFiles = new ArrayList<File>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewContactsMA = (ListView)(findViewById(R.id.listViewContactsMA));

        mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

        //объект для работы с настройками
        SavedSettings savedSettings = new SavedSettings(mSettings);

        //Toast.makeText(this, mSettings.getBoolean(SavedSettings.App_P));

        //если приложение запускается впервые выполняет данное условие
        if(!mSettings.getBoolean(SavedSettings.APP_PREFERENCES_HASVISITED, false))
        {
            //выставляет что приложение открывается не впервые
            SharedPreferences.Editor e = mSettings.edit();
            e.putBoolean(SavedSettings.APP_PREFERENCES_HASVISITED, true);
            e.apply();
            //открывает окно с выбором уровня для пользователя
            Intent help = new Intent(MainActivity.this, Help.class);
            startActivity(help);
            return;
        }


        if(SavedSettings.isExpert()){
            //запрашивает разрения у пользователя
            askPermission();
        }
        else{
            //выводит справку если пользователь не эспексперт
            startAlertDialog(0);
        }

        //определяет выбранный контакт и переходит на следующую activity
        listViewContactsMA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view;
                Contacts.informationAboutUser = text.getText().toString();
                //создание отдельной директории для пользователя в файловой системе
                new File(FilesWork.getPathForSelectedUser()).mkdir();
                Intent aboutContact = new Intent(MainActivity.this, AboutContact.class);
                startActivity(aboutContact);
            }
        });


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
    //TODO рефактор, может использоваться повторно в других местах
    //открывает диалоговое окно
    private void startAlertDialog(int numButton){
        FragmentManager manager = getSupportFragmentManager();
        MyDialogHelp.getButton = numButton;
        //MyDialogHelp.setContextMain(this);
        MyDialogHelp myDialogHelp = new MyDialogHelp(this,3);
        myDialogHelp.show(manager, "myDialog");
    }


    //region permissions
    //запрос разрешений
    public void askPermission(){
            Permissions permissions = new Permissions();
            //если разрешения были получены, выводит список записей
            if(!permissions.EnablePermissions(this)) loadMain();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            // permission granted
            loadMain();
        }
        else
            {
                // permission denied
                startAlertDialog(3);
        }

    }

    //endregion

    //загрузка страницы, после запроса прав у пользователя
    private void loadMain(){
        //создаёт директорию для работы приложения с файлами
        new File(SharedVariables.getPathApplicationFileSystem()).mkdir();

        //установка пути в настройках
        Records.pathForFindRecords = mSettings.getString("path", Records.currentPathForRecordsXiomi);

        if(!Records.checkPath(Records.pathForFindRecords)){
            Toast.makeText(this, Records.pathForFindRecords, Toast.LENGTH_SHORT).show();
            return;
        }

        //add list from selected path
        listFiles.addAll(Records.getFiles(Records.pathForFindRecords));

        //вывод список контактов в list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, this.getListContacts(listFiles));

        listViewContactsMA.setAdapter(adapter);
    }

    //возвращает список контактов, записи которых были найдены
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


}
