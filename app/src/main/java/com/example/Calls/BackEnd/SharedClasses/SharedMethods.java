package com.example.Calls.BackEnd.SharedClasses;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class SharedMethods {

    /**
     * check string
     * @param str string
     * @return true if == null
     */
    public static boolean isNullOrWhiteSpace(String str){
        if(str == null) return true;
        return str.trim().isEmpty();
    }


}
