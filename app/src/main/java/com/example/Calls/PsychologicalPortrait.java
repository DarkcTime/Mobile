package com.example.Calls;

import android.media.AudioFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;

import com.example.Calls.Model.Repositories.RecordRepository;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import java.io.File;
import java.lang.Object;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

public class PsychologicalPortrait extends AppCompatActivity {

    private MultiAutoCompleteTextView multiTextViewPortrait;
    private RelativeLayout relativeLayoutGetPortrait;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.psychological_portrait);

        multiTextViewPortrait = (MultiAutoCompleteTextView) findViewById(R.id.multiTextViewPortrait);
        relativeLayoutGetPortrait = (RelativeLayout) findViewById(R.id.relativeLayoutGetPortrait);

    }


    //region UI Actions
    public void onClickButtonGetPortrait(View view){
        relativeLayoutGetPortrait.setVisibility(View.GONE);
        multiTextViewPortrait.setVisibility(View.VISIBLE);
    }

    //endregion
}
