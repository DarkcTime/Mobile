package com.example.Calls.BackEnd.Api;

import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;

import java.io.File;
import java.io.IOException;


//класс для работы с ключом api
public class KeyApi {

    private String key;

    private final String PathForSaveApiKey = FileSystemParameters.getPathApplicationFileSystem().concat("key.txt");

    private final String Token = "V6BBXKPFPAARQCOMTOIGKGQRUBLCGV4R";

    /**
     * get key api
     * @return key
     */
    public String getKey() throws IOException {
        if (keyExists()) {
            key = FileSystem.ReadFile(PathForSaveApiKey);
            return key;
        }
        else
            throw new NullPointerException();
    }

    /**
     * set key
     */
    public void setKey() throws IOException {
        FileSystem.WriteFile(PathForSaveApiKey,Token,false);
        this.key = Token;
    }


    /**
     * check key Api
     * @return key
     */
    private boolean keyExists() throws IOException {
        if (new File(PathForSaveApiKey).exists()) {
            try{
                FileSystem.ReadFile(PathForSaveApiKey);
                return true;
            }
            catch (NullPointerException ex){
                Log.d("NullPointerException", ex.toString());
            }

        }
        return  false;
    }

}
