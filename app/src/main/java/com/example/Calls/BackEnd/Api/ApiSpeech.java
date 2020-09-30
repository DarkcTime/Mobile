package com.example.Calls.BackEnd.Api;

import android.annotation.SuppressLint;
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
public class ApiSpeech extends FileSpeech{

    @SuppressLint("SdCardPath")
    private static final String BufferPathForApplicationFileSystem = "/data/data/com.example.Calls/cache/";

    private final String pathForSaveKeyApi = BufferPathForApplicationFileSystem.concat("key.txt");

    private String key;

    private static Logger log = Logger.getLogger(ApiSpeech.class.getName());

    private final String Token = "V6BBXKPFPAARQCOMTOIGKGQRUBLCGV4R";

    private String pathRecord;

    public ApiSpeech(String _pathRecord) throws IOException {
        setKey(Token);
        pathRecord = _pathRecord;
    }

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
        FileSystem.WriteFile(pathForSaveKeyApi,key,false);
        this.key = key;
    }

    /**
     * parsing json answer
     * @param path путь к выбранной записи
     */
    private void ReturnText(final String path){
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
                            Log.d("ApiSpeechText", str);


                            //write data in file
                            FileSystem.WriteFile(path.concat(".txt"),str,false);
                            //change duration for ProgressBar
                            RecordProcessing.changeDurationTranslatingAndEndTranslation();

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


    //TODO varible for path
    private void startAsyncApiForTranslated(){
        AsyncApi asyncApi = new AsyncApi();
        asyncApi.execute(pathRecord);
    }

    public void writeTextTranslatedRecord(){
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
            myConnection.setRequestProperty ("Authorization","Bearer " + getKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
        myConnection.setRequestProperty("Content-Type", "audio/mpeg3");
        myConnection.setDoOutput(true);
        return myConnection;
    }


    //TODO rewrite method
    /**
     * Перевод слов в текст
     * @throws IOException
     */
    public void SpeechToText() throws IOException {

        Log.d("SpeechToText", pathRecord);

        int Length = FileSpeech.getLengthAudio(pathRecord)/1000;
        int count;
        //String pathCut = "/data/data/com.example.Calls/BackEnd/CheapSound";
        String pathCut = FileSystemParameters.getPathApplicationFileSystem();
        log.info(pathRecord);
        if (Length>19){
            count = Length /19;
            for (int i = 0;i<count;i++){
                int StartValue = i + 1 * 19;
                log.info("file cutter");

                log.info("return cutter");


                ReturnText(pathCut+ i + ".mp3");
            }

            int finalStart = count * 19;


            if (finalStart != Length){


                ReturnText(pathCut+ finalStart +".mp3");
            }

        }
        else {
            startAsyncApiForTranslated();
        }
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
        // [... Сообщите о результате через обновление пользовательского
        // интерфейса, диалоговое окно или уведомление ...]
        RecordProcessing.changeDurationTranslatingAndEndTranslation();
    }

}
