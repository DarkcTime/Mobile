package com.example.Calls.BackEnd.Debug;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.Calls.AboutContact;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.WaitInEndPlay;

import java.io.IOException;

public class DebugMessages {


    public static void ErrorMessage(Exception ex, Context context, String logTag){
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        Log.d(logTag, ex.getMessage());
    }

    public static void ErrorMessage(Exception ex, AboutContact aboutContact, String logTag){
        DialogMain.startErrorDialog(aboutContact, ex);
        Log.d(logTag, ex.getMessage());
    }

    public static void ErrorMessage(Exception ex, Play play, String logTag){
        DialogMain.startErrorDialog(play, ex);
        Log.d(logTag, ex.getMessage());
    }

    public static void ErrorMessage(Exception ex, WaitInEndPlay waitInEndPlay, String logTag){
        DialogMain.startErrorDialog(waitInEndPlay, ex);
        Log.d(logTag, ex.getMessage());
    }


    //TODO добавить IOExceptin
    public static void IOExceptionMessage(IOException iex, Context context, String logTag){
        Toast.makeText(context, iex.getMessage(), Toast.LENGTH_LONG).show();
        Log.d(logTag, iex.getMessage());
    }
}
