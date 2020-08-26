package com.example.Calls.BackEnd.CutterFiles;


//imported library - com.writingminds:FFmpegAndroid:0.3.2
//check build.gradle
//documentation https://ffmpeg.org/ffmpeg.html#Manual-stream-selection
//documentation https://writingminds.github.io/ffmpeg-android-java/

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.Calls.WaitInEndPlay;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.util.List;

//TODO do listener FFmpegProgress
public class FFmpegCutter {

    private FFmpeg ffmpeg;

    //object activity
    private Context context;

    //load FFMpeg library
    public FFmpegCutter(Context _context){
        try{
            if(isNullContext(_context)) loadFFMpegBinary();
        }
        catch (FFmpegNotSupportedException ex){
            Log.d("FFmpegNotSupportedEx", ex.getMessage());
        }
        catch (Exception ex){
            Log.d("FFmpegCutterException", ex.getMessage());
        }

    }

    private boolean isNullContext(Context _context){
        if(_context != null){
            context = _context;
            return true;
        }
        else{
            Log.d("FFmpegContext", "contextNull");
            return false;
        }
    }

    private void loadFFMpegBinary() throws FFmpegNotSupportedException {
        if(ffmpeg == null){
            ffmpeg = FFmpeg.getInstance(context);
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

    public void executeCommandForCutFileAfterPlay(List<FileForCutter> filesForCutter){
        for(FileForCutter file : filesForCutter){
            try{
                executeFFMpegCommand(getCommand(file));
            }
            catch (FFmpegCommandAlreadyRunningException ex){
                Log.d("FFAlreadyRunningEx", ex.getMessage());
            }
            catch (Exception ex){
                Log.d("ExecuteFFMpegException", ex.getMessage());
            }

        }
    }

    private String[] getCommand(FileForCutter fileForCutter){
        String intent = "-i ".concat(fileForCutter.getSource().getAbsolutePath()).concat(" ");
        String start = "-ss ".concat(String.valueOf(fileForCutter.getStart())).concat(" ");
        String duration = "-t ".concat(String.valueOf(fileForCutter.getDuration())).concat(" ");
        String command = intent.concat(start).concat(duration).concat("-b ").concat(fileForCutter.getBitrate().concat(" ").concat(fileForCutter.getDestination().getAbsolutePath()));
        Log.d("command", command);
        return command.split(" ");
    }

    private void executeFFMpegCommand(String[] command) throws FFmpegCommandAlreadyRunningException {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler(){
                @Override
                public void onFailure(String message) {
                    Log.d("onFailure", message);
                }

                @Override
                public void onSuccess(String message) {
                    Toast.makeText(context, "Записи успешно переведены", Toast.LENGTH_SHORT).show();
                    WaitInEndPlay waitInEndPlay = (WaitInEndPlay) context;
                    waitInEndPlay.setProgressBarWaitEndCutter("успех");
                    Log.d("onSuccess", message.toString());
                }

                @Override
                public void onProgress(String message) {
                    Log.d("onProgress", message);
                }

                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });

    }


}
