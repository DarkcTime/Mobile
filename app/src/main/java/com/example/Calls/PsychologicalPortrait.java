package com.example.Calls;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Model.Contact;
import com.example.Calls.Model.Repositories.ContactRepository;
import com.example.Calls.Model.Repositories.RecordRepository;

import org.tritonus.share.sampled.file.TAudioFileFormat;
import org.w3c.dom.Text;

import java.io.File;
import java.lang.Object;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

public class PsychologicalPortrait extends AppCompatActivity {

    private RelativeLayout relativeLayoutGetPortrait;

    private final String BOARD = "----------";
    private final String TELEGRAMPACKAGE = "org.telegram.messenger";


    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.MainActivity);

    private Button buttonEditTextForPortrait;
    private TextView textViewEditSave;
    private EditText editTextForPortrait;
    private LinearLayout linerLayoutTextForPortrait;
    private TextView textViewNumberWords, textViewSelectedContactPortrait, textViewSelectedContactNumber;


    ClipboardManager myClipboard;
    File fileResult;

    //Протестировать кнопку которую я поменял

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.psychological_portrait);

            relativeLayoutGetPortrait = (RelativeLayout) findViewById(R.id.relativeLayoutGetPortrait);
            buttonEditTextForPortrait = (Button) findViewById(R.id.buttonEditTextForPortrait);
            textViewEditSave = (TextView) findViewById(R.id.textViewEditSave);
            editTextForPortrait = (EditText) findViewById(R.id.editTextForPortrait);
            myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);


            Contact selectedContact = ContactRepository.getSelectedContact();
            textViewSelectedContactPortrait = (TextView) findViewById(R.id.textViewSelectedContactPortrait);
            textViewSelectedContactPortrait.setText(selectedContact.Name);
            textViewSelectedContactNumber = (TextView) findViewById(R.id.textViewSelectedContactNumber);
            textViewSelectedContactNumber.setText(selectedContact.NumberPhone);

            textViewNumberWords = (TextView) findViewById(R.id.textViewNumberWords);
            textViewNumberWords.setText(getTextNumberWords(selectedContact.NumberWords));

            fileResult = new File(FileSystemParameters.getPathFileResultForSelectedContact());
            //if we not have words for selected contact
            if(!fileResult.exists()){
                notHaveWords();
                return;
            }

            String textForPortrait = FileSystem.ReadFile(FileSystemParameters.getPathFileResultForSelectedContact());
            if(!textForPortrait.equals("")){
                editTextForPortrait.setText(textForPortrait);
            }
            else{
                //if we not have words for selected contact
                notHaveWords();
            }

        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "PsychologicalPortrait/onCreate");
        }

    }
    private boolean isHaveWordsForContact(){
        String text = editTextForPortrait.getText().toString();
        return !text.equals("Получите текст для данного контакта");
    }

    private void notHaveWords(){
        editTextForPortrait.setText("Получите текст для данного контакта");
    }

    private String getTextNumberWords(int count){
        return  "Набрано слов: " + String.valueOf(count) + "/1000";
    }

    //region UI Actions
    public void onClickButtonEditForPortrait(View view){
        try{
            if(!isHaveWordsForContact()){
                Toast.makeText(getApplicationContext(),"Получите текст для данного контакта",Toast.LENGTH_LONG).show();
                return;
            }

            if(editTextForPortrait.isEnabled()){
                String textForPortrait = editTextForPortrait.getText().toString();
                FileSystem.WriteFile(fileResult.getAbsolutePath(), textForPortrait, false);
                Toast.makeText(this, "Данные успешно сохранены", Toast.LENGTH_LONG).show();
                textViewEditSave.setText("Редактировать");
                editTextForPortrait.setEnabled(false);
            }
            else{
                textViewEditSave.setText("Сохранить");
                editTextForPortrait.setEnabled(true);
            }
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "PsychologicalPortrait/ButtonEditForPortrait");
        }
    }

    //Send message with text to Telegram
    public void onClickSendTelegram(View view) {
        try {
            if(isHaveWordsForContact()){
                sendMessageTelegram(ContactRepository.getSelectedContact().Name, editTextForPortrait.getText().toString());
            }
            else{
                Toast.makeText(getApplicationContext(),"Получите текст для данного контакта",Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickButtonCopyForPortrait");
        }
    }
    private void sendMessageTelegram(String contactName, String message){
        try{
            if(isTelegramInstalled()){
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String text = BOARD + "\n" + contactName + "\n" + BOARD + "\n" + message;
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);

                sendIntent.setType("text/plain");
                sendIntent.setPackage(TELEGRAMPACKAGE);
                startActivity(Intent.createChooser(sendIntent,"Поделиться"));
            }
            else{
                Toast.makeText(getApplicationContext(), "Установите приложение Telegram на телефон", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception exception){
            Toast.makeText(getApplicationContext(), "sendMessageTelegramException", Toast.LENGTH_LONG).show();
        }
    }
    private boolean isTelegramInstalled(){
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(TELEGRAMPACKAGE, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            dialogMain.showErrorDialogAndTheOutputLogs(e, "isTelegramInstalled");
        }
        return false;
    }

    //endregion
}
