package com.example.Calls.BackEnd.Api;



import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import com.example.Calls.BackEnd.Contacts;
import com.example.Calls.BackEnd.Listener.AListener;
import com.example.Calls.BackEnd.Records;
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

public class ApiSpeech {

    public static String pathForNewFile = "/data/data/com.example.Calls/cache/";

    private static Logger log = Logger.getLogger(ApiSpeech.class.getName());

    public ApiSpeech() throws IOException {
        Log.d("ApiSpeech", "setKey");
        setKey("XXKDHCSNACJ4FDAZX2MZBJTRC3WZWGHT");
        Log.d("ApiSpeech", "endKey");
        //setKey("6VZAY3JEGF2CEY3VL3HGB6KAIBRV77GS");
    }

    List<AListener> WITListeners;

    List<AListener> KeyListeners;

    int count;

    /**
     * ключ к api
     */
    private String key;

    /**
     * Получение ключа
     * @return ключ
     */
    public boolean keyExists() throws IOException {
        String path = "/data/data/com.example.Calls/cache/key.txt";
        File file = new File(path);
        if (file.exists()) {
            if (ReadFile(file) != "") {
                return true;
            }
        }
        return  false;
    }

    /**
     * Получить ключ api
     * @return ключ api
     */
    public String getKey() throws IOException {
        String path = "/data/data/com.example.Calls/cache/key.txt";
        File file = new File(path);
        if (keyExists()) {
            key = ReadFile(file);
            return key;
        }
        else
            throw new NullPointerException();
    }

    /**
     * Задать ключ
     * @param key ключ
     */
    public void setKey(String key) throws IOException {
        String path = "/data/data/com.example.Calls/cache/key.txt";
        WriteFile(path,key.getBytes());
        this.key = key;
    }

    /**
     * Добавить слушателя
     * @param listener слушатель
     */
    public void addListener(AListener listener) {
       WITListeners.add(listener);
    }

    /**
     * Добавить нового слушателя ключа
     * @param keyListener
     */
    public void addKeyListener(AListener keyListener){KeyListeners.add(keyListener); }

    /**
     * Вызвать всех слушателей ключа api
     */
    public void keyDoSomething(){
        for (AListener listener:
                KeyListeners) {
            listener.doEvent();
        }
    }

    /**
     * Активация всех слушателей.
     */
    public void WITDoSomething(){
        for (AListener listener:
                WITListeners) {
            listener.doEvent();
        }
    }

    /**
     * парсинг josn ответа
     * @param path путь к файлу для сохранения
     * @param contact контакт к которому прикрепляется ответ
     */
    public void ReturnText(final String path, final Contacts contact){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                URL githubEndpoint = null;
                try {
                    Log.d("ApiSpeech", "httpURL");
                    HttpsURLConnection myConnection = getWITConnection();
                    SetOutput(myConnection, path);
                    JsonReader jsonReader = getJsonInput(myConnection);
                    Log.d("ApiSpeech", "while");
                    while (jsonReader.hasNext()){
                        Log.d("ApiSpeech", "key");
                        String key = jsonReader.nextName();
                        Log.d("ApiSpeech", "key.equals");
                        if (key.equals("text")) {
                            String str = jsonReader.nextString();
                            Log.d("ApiSpeech", "WriteFile");
                            Log.d("ApiSpeech", str);
                            FileSpeech.WriteFileOnSpeech(contact,str, SelectMethodSaveText.allText);
                            log.info(str);
//                            WITDoSomething();
                            break;
                        } else {
                            Log.d("ApiSpeech", "key.equals");
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
            keyDoSomething();
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
    public void SpeechToText(String pathSelectRecord, Contacts contact) throws IOException {
        int Length = FileSpeech.getLengthAudio(pathSelectRecord)/1000;
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
                ReturnText(pathCut+ i + ".mp3",contact);
            }

            int finalStart = count * 19;

            //cutter?
            if (finalStart != Length){
                FileCutter(pathSelectRecord, finalStart,Length,String.valueOf(finalStart));
                ReturnText(pathCut+ finalStart +".mp3",contact);
            }
          //  WITDoSomething();
        }
        else {
            log.info("api sheech");
            new ApiSpeech().ReturnText(pathSelectRecord,contact);
        }
    }

}
