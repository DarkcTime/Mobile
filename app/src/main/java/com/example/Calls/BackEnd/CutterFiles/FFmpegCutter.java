package com.example.Calls.BackEnd.CutterFiles;


//imported library - com.writingminds:FFmpegAndroid:0.3.2
//check build.gradle
//documentation https://ffmpeg.org/ffmpeg.html#Manual-stream-selection
//documentation https://writingminds.github.io/ffmpeg-android-java/

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.Calls.BackEnd.Debug.DebugMessages;
import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Records.RecordProcessing;
import com.example.Calls.WaitInEndPlay;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.IOException;
import java.util.List;

//TODO do listener FFmpegProgress
public class FFmpegCutter {

    private FFmpeg ffmpeg;

    //object activity
    private Context context;

    //load FFMpeg library
    public FFmpegCutter(Context _context) {
        try {
            if (isNullContext(_context)) loadFFMpegBinary();
        } catch (FFmpegNotSupportedException ex) {
            Log.d("FFmpegNotSupportedEx", ex.getMessage());
        } catch (Exception ex) {
            Log.d("FFmpegCutterException", ex.getMessage());
        }

    }

    private boolean isNullContext(Context _context) {
        if (_context != null) {
            context = _context;
            return true;
        } else {
            Log.d("FFmpegContext", "contextNull");
            return false;
        }
    }

    private void loadFFMpegBinary() throws FFmpegNotSupportedException {
        if (ffmpeg == null) {
            ffmpeg = FFmpeg.getInstance(context);
        }

        ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
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

    public void executeCommandForCutFileAfterPlay(List<FileForCutter> filesForCutter) {

        try{
            RecordProcessing.setMaxDurationProcessing(filesForCutter.size());

            for (FileForCutter file : filesForCutter) {
                try {
                    File sourceFile = new File(file.getDestination().getAbsolutePath());
                    File copyFile = new File(getTargetFileForCopy(FileSystemParameters.getPathForSelectedRecordApi(), file.getDestination().getName()).getAbsolutePath());
                    executeFFMpegCommand(getCommand(file), sourceFile, copyFile);
                } catch (FFmpegCommandAlreadyRunningException ex) {
                    Log.d("FFAlreadyRunningEx", ex.getMessage());
                } catch (Exception ex) {
                    Log.d("ExecuteFFMpegException", ex.getMessage());
                }

            }
        }
        catch (Exception ex){
            DebugMessages.ErrorMessage(ex, (WaitInEndPlay)context, "executeCommand");
        }
    }

    private File getTargetFileForCopy(String _pathForCopy, String fileName) {
        return new File(_pathForCopy.concat(fileName));
    }


    /**
     * Генерирует команду для резчика
     * Пример: -i /storage/emulated/0/MIUI/sound_recorder/call_rec/Миха(89636504365)_20200910105924.mp3 -ss 0
     * -t 4 -b 32k /storage/emulated/0/Android/data/com.Calls/Миха/Миха(89636504365)_20200910105924/records/0.mp3
     * @param fileForCutter
     * @return
     */
    private String[] getCommand(FileForCutter fileForCutter) {
        String intent = "-i ".concat(fileForCutter.getSource().getAbsolutePath()).concat(",");
        String start = "-ss ".concat(String.valueOf(fileForCutter.getStart())).concat(",");
        String duration = "-t ".concat(String.valueOf(fileForCutter.getDuration())).concat(",");
        String command = intent.concat(start).concat(duration).concat("-b ").concat(fileForCutter.getBitrate().concat(",").concat(fileForCutter.getDestination().getAbsolutePath()));
        Log.d("command", command);
        return command.split(",");
    }

    private void executeFFMpegCommand(String[] command, final File sourceFile, final File copyFile) throws FFmpegCommandAlreadyRunningException {
        final WaitInEndPlay waitInEndPlay = (WaitInEndPlay) context;

        ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
            @Override
            public void onFailure(String message) {
                try {
                    throw new Exception(message);
                }
                catch (Exception ex){
                    DebugMessages.ErrorMessage(ex,waitInEndPlay, "executeFFMpegCommand");
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String message) {
                try {

                    //copy files in dir for work with Api
                   FileSystem.CopyFile(sourceFile, copyFile);
                    Log.d("Copy", "FileCopy");
                    //set Text View Duration
                    RecordProcessing.changeDurationProcessingAndStartApi();


                } catch (IOException io) {
                    DebugMessages.ErrorMessage(io, waitInEndPlay, "IOExceptionCutter");
                } catch (Exception ex) {
                    DebugMessages.ErrorMessage(ex, waitInEndPlay, "ExceptionFFMpeg");
                }

            }

            @Override
            public void onProgress(String message) {

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
