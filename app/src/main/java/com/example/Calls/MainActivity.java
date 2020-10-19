package com.example.Calls;

//region import

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Debug;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Contacts.Contacts;
import com.example.Calls.BackEnd.Debug.DebugMessages;
import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Mail.Mailer;
import com.example.Calls.BackEnd.Permissions.Permissions;
import com.example.Calls.BackEnd.Records.Records;
import com.example.Calls.BackEnd.Settings.SavedSettings;
import com.example.Calls.BackEnd.SharedClasses.Application;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Dialog.MyDialogHelp;

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

    //window dialog
    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.MainActivity);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            listViewContactsMA = (ListView) (findViewById(R.id.listViewContactsMA));

            mSettings = getSharedPreferences(SavedSettings.APP_PREFERENCES, Context.MODE_PRIVATE);

            //объект для работы с настройками
            SavedSettings savedSettings = new SavedSettings(mSettings);

            //если приложение запускается впервые выполняет данное условие
            if (!mSettings.getBoolean(SavedSettings.APP_PREFERENCES_HASVISITED, false)) {
                SharedPreferences.Editor e = mSettings.edit();
                e.putBoolean(SavedSettings.APP_PREFERENCES_HASVISITED, true);
                e.apply();

                //открывает окно с выбором уровня для пользователя
                Intent help = new Intent(MainActivity.this, Help.class);
                startActivity(help);
                return;
            }


            if (SavedSettings.isExpert()) {
                //запрашивает разрения у пользователя
                askPermission();
            } else {
                dialogMain.showHelpDialogFirstLaunch();
            }

            //определяет выбранный контакт и переходит на следующую activity
            listViewContactsMA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try{
                        TextView text = (TextView) view;
                        Contacts.setInformationAboutUser(text.getText().toString());
                        Intent aboutContact = new Intent(MainActivity.this, AboutContact.class);
                        startActivity(aboutContact);
                    }
                    catch (Exception ex){
                        dialogMain.showErrorDialogAndTheOutputLogs(ex, "listViewContactsMA");
                    }
                }
            });


        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onCreateMainActivity");
        }

    }


    //загрузка страницы, после запроса прав у пользователя
    private void loadMain() {
        try {
            //TODO?
            //установка пути в настройках
            Records.setPathForFindRecords(mSettings.getString("path", Records.currentPathForRecordsXiomi));

            if (!Records.checkPath(Records.getPathForFindRecords())) {
                //TODO show dialog window
                Toast.makeText(this, Records.getPathForFindRecords(), Toast.LENGTH_SHORT).show();
                return;
            }

            //make adapter
            ArrayAdapter<String> adapterContact =
                    new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1,
                            Contacts.getFilteredListContacts(this));

            listViewContactsMA.setAdapter(adapterContact);

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "loadMain");
        }

    }


    //region ButtonsClick

    //открывает диалоговое окно с контекстной справкой для данной страницы
    public void onCLickButtonHelp(View view) {
        try{
            dialogMain.showHelpDialogFirstLaunch();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonHelp");
        }
    }

    //открывает окно настроек
    public void onCLickButtonSettings(View view) {
        try {
            Intent settings = new Intent(MainActivity.this, Settings.class);
            startActivity(settings);
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonSettings");
        }
    }

    //endregion

    //region permissions
    public void askPermission() {
        try {
            Permissions permissions = new Permissions();
            //если разрешения были получены, выводит список записей
            if (!permissions.EnablePermissions(this)) loadMain();
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "askPermission");
        }
    }

    //обработка ответа разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            if (grantResults.length > 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                loadMain();
            } else {
                // permission denied
                dialogMain.showMyDialogHelp(MyDialogHelp.Windows.PERMISSIONS);
            }

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onRequestPermissionsResult");
        }

    }

    //endregion


}
