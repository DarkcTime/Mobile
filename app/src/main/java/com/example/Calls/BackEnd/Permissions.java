package com.example.Calls.BackEnd;



import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Permissions {

    ArrayList<String> permissions;
    public void EnablePermissions(Activity activity) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "EnablePermissions:aaaaaaaaaaaaaaaaaaaaaaaa ");
            int accessStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int accessContact = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
            int accessCall = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
            int accessAudio = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            int internet = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
            int accessInternet = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE);

            permissions = new ArrayList();

            if (accessStorage == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (accessContact == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (accessCall == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.CALL_PHONE);
            }
            if (accessAudio == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (internet == PackageManager.PERMISSION_DENIED){
                permissions.add(Manifest.permission.INTERNET);
            }
            if (accessInternet == PackageManager.PERMISSION_DENIED){
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }

            if(permissions.size() > 0) {
                ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), 1);
            }else{
                Toast.makeText(activity,"permissions granted",Toast.LENGTH_LONG).show();
            }
        }
    }
}
