package com.example.Calls.Dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;

import com.example.Calls.EditTextRecord;
import com.example.Calls.Help;
import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.Settings;
import com.example.Calls.WaitInEndPlay;


/**
 * call dialog windows
 */

public class DialogMain extends AppCompatDialogFragment {

    private FragmentManager manager;

    public DialogMain(){

    }

    private MainActivity mainActivity;
    private WaitInEndPlay waitInEndPlay;
    private Play play;
    private Help help;
    private EditTextRecord editTextRecord;

    /**
     * create object for show dialog
     * @param context context current activity
     * @param _activity call current activity
     */
    @SuppressLint("ValidFragment")
    public DialogMain(Context context, Activities _activity){
        try{
            switch (_activity){
                case MainActivity:
                    mainActivity = (MainActivity)context;
                    manager = mainActivity.getSupportFragmentManager();
                    break;
                case AboutContact:
                    break;
                case Settings:
                    Settings settings = (Settings) context;
                    manager = settings.getSupportFragmentManager();
                    break;
                case Play:
                    play = (Play) context;
                    manager = play.getSupportFragmentManager();
                    break;
                case WaitInEndPlay:
                    waitInEndPlay = (WaitInEndPlay)context;
                    manager = waitInEndPlay.getSupportFragmentManager();
                    break;
                case Help:
                    help = (Help)context;
                    manager = help.getSupportFragmentManager();
                    break;
                case EditTextRecord:
                    editTextRecord = (EditTextRecord) context;
                    manager = editTextRecord.getSupportFragmentManager();
                    break;
            }

        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "DialogMain");
        }
    }

    public void showHelpDialogFirstLaunch(){
        try{
            HelpDialogFirstLaunch helpDialogFirstLaunch = new HelpDialogFirstLaunch();
            if(mainActivity == null || manager == null) throw new Exception("MainActivity || manager == null");
            helpDialogFirstLaunch.setMainActivity(mainActivity);
            helpDialogFirstLaunch.show(manager, "MainActivity");
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "showHelpDialogFirstLaunch");
        }
    }

    public void showQuestionDialog(){
        try{
            QuestionDialog questionDialog = new QuestionDialog(editTextRecord);
            questionDialog.show(manager, "showQuestionDialog");
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "showQuestionDialog");
        }
    }

    public void showHelpDialog(HelpDialog.Helps help){
        try{
            HelpDialog helpDialog = new HelpDialog();
            helpDialog.setHelp(help);
            helpDialog.show(manager, "HelpDialog");
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "showHelpDialog");
        }
    }

    public void showResultDialog(){
        try{
            ResultDialog resultDialog = new ResultDialog();
            resultDialog.setWaitInEndPlayActivity(waitInEndPlay);
            resultDialog.show(manager, "showResultDialog");
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "showResultDialog");
        }
    }

    public void showMediaEnd(){
        try{
            PlayEndDialog playEndDialog = new PlayEndDialog();
            playEndDialog.setPlay(play);
            playEndDialog.setCancelable(false);
            playEndDialog.show(manager, "showMediaEnd");
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "showMediaEnd");
        }
    }

    public void showPermissionDialog(){
        try{
            PermissionDialog permissionDialog = new PermissionDialog();
            permissionDialog.setMainActivity(mainActivity);
            permissionDialog.setCancelable(false);
            permissionDialog.show(manager, "showPermissionDialog");
        }
        catch (Exception ex){
            showErrorDialogAndTheOutputLogs(ex, "showPermissionDialog");
        }
    }


    /**
     * show window for send email to developer
     */
    public void showErrorDialogAndTheOutputLogs(Exception ex, String tag){
        try{

            String typeException = ex.getClass().getSimpleName();
            Log.d("typeEx", typeException);
            String errorMessage = ex.getMessage();
            Log.d("errorMessage", errorMessage);
            Log.d("tag", tag);

            ErrorDialog errorDialog = new ErrorDialog(typeException, errorMessage, tag);
            errorDialog.show(manager, "startErrorDialog");

        }
        catch (Exception exception){
            Log.d("showErrorDialog", exception.getMessage());
        }
    }

    /**
     * enum for select type Activity
     */
    public enum Activities {
        MainActivity, Play, Settings, AboutContact, WaitInEndPlay, Help, EditTextRecord
    }


}
