package com.example.Calls.BackEnd.Permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Dialog.HelpDialog;
import com.example.Calls.MainActivity;

import java.security.Permission;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Permissions {

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE};
    private String callPhone = Manifest.permission.CALL_PHONE;

    private MainActivity mainActivity;
    public Permissions(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    /**
     * check permissions, if don't have - ask
     * @return true - if have all request for work with Application
     * @throws Exception unexpected error
     */
    public boolean isEnablePermissions() throws Exception{
        try{

            ArrayList<String> requestPermissionsList = new ArrayList<>();

            for (String permission : permissions){
                if(isCheckPermission(permission)){
                    requestPermissionsList.add(permission);
                }
            }

            int sizeRequestList = requestPermissionsList.size();

            if(sizeRequestList > 1)
            {
                //ask permissions
                ActivityCompat.requestPermissions(mainActivity,
                        requestPermissionsList.toArray(new String[sizeRequestList]), 1);
                return false;
            }
            else {
                return true;
            }

        }
        catch (Exception ex){
            throw new Exception("Permissions/EnablePermissions - ".concat(ex.getMessage()));
        }

    }
    /**
     * @param permission string permission
     * @return true - if permission don't give
     */
    private boolean isCheckPermission(String permission){
        return ContextCompat.checkSelfPermission(mainActivity, permission) == PackageManager.PERMISSION_DENIED;
    }



}
