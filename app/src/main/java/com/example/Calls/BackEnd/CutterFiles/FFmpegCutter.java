package com.example.Calls.BackEnd.CutterFiles;


//imported library - com.writingminds:FFmpegAndroid:0.3.2
//check build.gradle
//documentation https://ffmpeg.org/ffmpeg.html#Manual-stream-selection
//documentation https://writingminds.github.io/ffmpeg-android-java/

import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

//TODO do listener FFmpegProgress
public class FFmpegCutter {

    private FFmpeg ffmpeg;

    public FFmpegCutter(){

    }

    private void executeFFMpegCommand(String[] commands) throws FFmpegCommandAlreadyRunningException {

        try{
            ffmpeg.execute(commands, new ExecuteBinaryResponseHandler(){
                @Override
                public void onFailure(String message) {
                    Log.d("onFailure", message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d("onSuccess", message.toString());
                }

                @Override
                public void onProgress(String message) {
                    Log.d("onProgress", message);
                }
            });
        }
        catch (Exception ex){
            Log.d("executeEx", ex.getMessage());
        }

    }

    private void loadFFMpegBinary() throws FFmpegNotSupportedException {
        if(ffmpeg == null){
            ffmpeg = FFmpeg.getInstance(this);
        }

        ffmpeg.loadBinary(new LoadBinaryResponseHandler(){
            @Override
            public void onFailure() {
                Log.d("loadBinary", "failure");
            }

            @Override
            public void onSuccess() {
                Log.d("loadBinary", "success");
            }
        });
    }


}
