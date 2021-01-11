package com.example.Calls;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.media.AudioFormat;
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

    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.MainActivity);

    private Button buttonEditTextForPortrait;
    private EditText editTextForPortrait;
    private LinearLayout linerLayoutTextForPortrait;

    ClipboardManager myClipboard;
    File fileResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.psychological_portrait);

            relativeLayoutGetPortrait = (RelativeLayout) findViewById(R.id.relativeLayoutGetPortrait);
            buttonEditTextForPortrait = (Button) findViewById(R.id.buttonEditTextForPortrait);
            editTextForPortrait = (EditText) findViewById(R.id.editTextForPortrait);
            myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

            Contact selectedContact = ContactRepository.getSelectedContact();
            TextView textViewSelectedContactPortrait = (TextView) findViewById(R.id.textViewSelectedContactPortrait);
            textViewSelectedContactPortrait.setText(selectedContact.Name);

            
            //if we have more 1000 words
            if(selectedContact.NumberWords >= ContactRepository.MAX_PERCENTAGE){
                relativeLayoutGetPortrait.setVisibility(View.VISIBLE);
                linerLayoutTextForPortrait.setVisibility(View.GONE);
                return;
            }

            fileResult = new File(FileSystemParameters.getPathFileResultForSelectedContact());
            //if we not have words for selected contact
            if(!fileResult.exists()){
                return;
            }

            String textForPortrait = FileSystem.ReadFile(FileSystemParameters.getPathFileResultForSelectedContact());
            editTextForPortrait.setText(textForPortrait);
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "PsychologicalPortrait/onCreate");
        }

    }

    //region UI Actions
    public void onClickButtonEditForPortrait(View view){
        try{
            if(editTextForPortrait.isEnabled()){
                String textForPortrait = editTextForPortrait.getText().toString();
                FileSystem.WriteFile(fileResult.getAbsolutePath(), textForPortrait, false);
                Toast.makeText(this, "Данные успешно сохранены", Toast.LENGTH_LONG).show();
                buttonEditTextForPortrait.setText("Редактировать");
                editTextForPortrait.setEnabled(false);
            }
            else{
                buttonEditTextForPortrait.setText("Сохранить");
                editTextForPortrait.setEnabled(true);
            }
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "PsychologicalPortrait/ButtonEditForPortrait");
        }
    }

    public void onClickButtonCopyForPortrait(View view){
        ClipData myClip;
        String text = editTextForPortrait.getText().toString();
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(this, "Текст скопирован", Toast.LENGTH_LONG).show();
    }

    public void onClickButtonGetPortrait(View view){
        relativeLayoutGetPortrait.setVisibility(View.GONE);
    }


    //endregion
}
