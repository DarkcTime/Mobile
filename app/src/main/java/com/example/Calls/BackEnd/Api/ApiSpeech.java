package com.example.Calls.BackEnd.Api;

import android.annotation.SuppressLint;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Records.RecordProcessing;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

//class work with Api
public class ApiSpeech{

    private KeyApi keyApi;

    private String pathRecord;

    public ApiSpeech(String _pathRecord) throws IOException {
        keyApi = new KeyApi();
        keyApi.setKey();
        pathRecord = _pathRecord;
    }

    //TODO rewrite method
    /**
     * Перевод слов в текст
     * @throws IOException
     */
    void SpeechToText() throws IOException {

        Log.d("SpeechToText", pathRecord);

        int Length = getLengthAudio(pathRecord)/1000;


        if (Length>19){

        }
        else {
            startAsyncApiForTranslated();
        }
    }

    public static int getLengthAudio(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.parseInt(durationStr);
    }


    private void startAsyncApiForTranslated(){
        AsyncApi asyncApi = new AsyncApi();
        asyncApi.execute(pathRecord);
    }


    void writeTextTranslatedRecord(){
        try {
            HttpsURLConnection myConnection = getWITConnection();
            SetOutput(myConnection, pathRecord);
            JsonReader jsonReader = getJsonInput(myConnection);
            while (jsonReader.hasNext()){
                String key = jsonReader.nextName();

                if (key.equals("text")) {
                    String str = jsonReader.nextString();
                    Log.d("ApiSpeechText", str);

                    //write data in file
                    FileSystem.WriteFile(pathRecord.concat(".txt"),str,false);
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
            myConnection.setRequestProperty ("Authorization","Bearer " + keyApi.getKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
        myConnection.setRequestProperty("Content-Type", "audio/mpeg3");
        myConnection.setDoOutput(true);
        return myConnection;
    }

}

class AsyncApi extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... parameters) {
        try{
            new ApiSpeech(parameters[0]).writeTextTranslatedRecord();
        }
        catch (IOException ex){
            Log.d("AsyncApi", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        RecordProcessing.changeDurationTranslatingAndEndTranslation();
    }

}
