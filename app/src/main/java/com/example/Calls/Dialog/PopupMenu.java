package com.example.Calls.Dialog;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.Calls.R;

public class PopupMenu {

    public static void showPopupMenu(Context context, View view, int resource){
        android.widget.PopupMenu popup = new android.widget.PopupMenu(context, view);
        //popup.setOnMenuItemClickListener((android.widget.PopupMenu.OnMenuItemClickListener) context);
        popup.inflate(resource);
        popup.show();
    }


}
