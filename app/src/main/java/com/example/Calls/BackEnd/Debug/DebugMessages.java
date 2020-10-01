package com.example.Calls.BackEnd.Debug;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class DebugMessages {

    public static void ErrorMessage(Exception ex, Context context, String logTag){
        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        Log.d(logTag, ex.getMessage());
    }

    //TODO добавить IOExceptin
    public static void IOExceptionMessage(IOException iex, Context context, String logTag){
        Toast.makeText(context, iex.getMessage(), Toast.LENGTH_LONG).show();
        Log.d(logTag, iex.getMessage());
    }
}
