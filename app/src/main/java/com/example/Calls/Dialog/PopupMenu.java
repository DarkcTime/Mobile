package com.example.Calls.Dialog;

import android.content.Context;
import android.view.View;

import com.example.Calls.R;

public class PopupMenu {

    public static void showPopupMenu(Context context, View view){
        android.widget.PopupMenu popup = new android.widget.PopupMenu(context, view);
        popup.setOnMenuItemClickListener((android.widget.PopupMenu.OnMenuItemClickListener) context);
        popup.inflate(R.menu.menu_settings);
        popup.show();
    }
}
