package com.example.Calls.BackEnd.Api;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.SharedVariables;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;
import static com.example.Calls.BackEnd.Api.FileSpeech.ReadFile;
import static com.example.Calls.BackEnd.Api.FileSpeech.WriteFile;
import static com.example.Calls.BackEnd.CheapSound.Utilities.FileCutter;

//class work with Api
public class ApiSpeech extends FileSpeech{

    private final String pathForSaveKeyApi = SharedVariables.getBufferPathForApplicationFileSystem().concat("key.txt");

    private String key;

    private static Logger log = Logger.getLogger(ApiSpeech.class.getName());

    private final String Token = "V6BBXKPFPAARQCOMTOIGKGQRUBLCGV4R";

    public ApiSpeech() throws IOException {
        setKey(Token);
    }

    /**
     * check key Api
     * @return key
     */
    private boolean keyExists() throws IOException {
        File file = new File(pathForSaveKeyApi);
        if (file.exists()) {
            try{
                return ReadFile(file) != null;
            }
            catch (NullPointerException ex){
                Log.d("NullPointerException", ex.toString());
            }

        }
        return  false;
    }

    /**
     * get key api
     * @return key
     */
    private String getKey() throws IOException {
        File file = new File(pathForSaveKeyApi);
        if (keyExists()) {
            key = ReadFile(file);
            return key;
        }
        else
            throw new NullPointerException();
    }

    /**
     * set key
     * @param key key
     */
    private void setKey(String key) throws IOException {
        WriteFile(pathForSaveKeyApi,key.getBytes());
        this.key = key;
    }

    /**
     * parsing json answer
     * @param path путь к файлу для сохранения
     * @param contact контакт к которому прикрепляется ответ
     */
    private void ReturnText(final String path, final Contacts contact, final int stage, final String pathDir){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                URL githubEndpoint = null;
                try {
                    HttpsURLConnection myConnection = getWITConnection();
                    SetOutput(myConnection, path);
                    JsonReader jsonReader = getJsonInput(myConnection);
                    while (jsonReader.hasNext()){
                        String key = jsonReader.nextName();

                        if (key.equals("text")) {
                            String str = jsonReader.nextString();
                            Log.d("ApiSpeech", "WriteFile");
                            Log.d("ApiSpeech", str);
                            FileSpeech.WriteFileOnSpeech(contact,str,stage,path,pathDir);
                            log.info(str);
                            break;
                        } else {
                            Log.d("ApiSpeech", "key.noequals");
                            jsonReader.skipValue();
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * Получение ответа от WIT api
     * @param myConnection покдлючение к WIT api
     * @return Ответ в формате json
     * @throws IOException
     */
    private JsonReader getJsonInput(HttpsURLConnection myConnection) throws IOException {
        InputStream responseBody = myConnection.getInputStream();
        InputStreamReader responseBodyReader =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
        }
        JsonReader jsonReader = new JsonReader(responseBodyReader);
        jsonReader.beginObject();
        return jsonReader;
    }

    /**
     * Формирование тела PUT
     * @param myConnection подключение к WIT api
     * @param path путь к аудио файлу
     * @throws IOException
     */
    private void SetOutput(HttpsURLConnection myConnection, String path) throws IOException {
        OutputStream outputStream = myConnection.getOutputStream();
        FileChannel fileChannel = new FileInputStream(path).getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while((fileChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            byte[] b = new byte[byteBuffer.remaining()];
            byteBuffer.get(b);
            outputStream.write(b);
            byteBuffer.clear();
        }
    }

    /**
     * Метод для подключения к WIT api
     * @return подключение к api
     * @throws IOException
     */
    private HttpsURLConnection getWITConnection(){
        URL githubEndpoint = null;
        try {
            githubEndpoint = new URL("https://api.wit.ai/speech");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection myConnection =
                null;
        try {
            myConnection = (HttpsURLConnection) githubEndpoint.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            myConnection.setRequestProperty ("Authorization","Bearer " + getKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
        myConnection.setRequestProperty("Content-Type", "audio/mpeg3");
        myConnection.setDoOutput(true);
        return myConnection;
    }

    /**
     * Перевод слов в текст
     * @param pathSelectRecord путь к файлу записи разговора
     * @param contact контакт запись которого необходимо сохранить
     * @throws IOException
     */
    public void SpeechToText(String pathSelectRecord, Contacts contact, int stage, String pathDir) throws IOException {
        int Length = FileSpeech.getLengthAudio(pathSelectRecord)/1000;
        int count;
        //String pathCut = "/data/data/com.example.Calls/BackEnd/CheapSound";
        String pathCut = SharedVariables.getPathApplicationFileSystem();
        log.info(pathSelectRecord);
        if (Length>19){
            count = Length /19;
            for (int i = 0;i<count;i++){
                int StartValue = i + 1 * 19;
                log.info("file cutter");
                FileCutter(pathSelectRecord,i, StartValue,String.valueOf(i));
                log.info("return cutter");
                ReturnText(pathCut+ i + ".mp3",contact,stage, pathDir);
            }

            int finalStart = count * 19;


            if (finalStart != Length){
                FileCutter(pathSelectRecord, finalStart,Length,String.valueOf(finalStart));
                ReturnText(pathCut+ finalStart +".mp3",contact,stage, pathDir);
            }

        }
        else {
            log.info("api sheech");
            new ApiSpeech().ReturnText(pathSelectRecord,contact,stage, pathDir);
        }
    }

}
