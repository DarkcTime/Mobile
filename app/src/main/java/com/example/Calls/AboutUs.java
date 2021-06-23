package com.example.Calls;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

    }

    //Open profile with text for contact
    public void onClickButtonGoToHelp(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Resources.UrlHelp));
        startActivity(browserIntent);
    }


}


