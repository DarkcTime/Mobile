package com.example.Calls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.Touch;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Calls.BackEnd.Analysis.AnalyzeCall;
import com.example.Calls.BackEnd.CutterFiles.Cutter;
import com.example.Calls.BackEnd.CutterFiles.CutterInterval;
import com.example.Calls.BackEnd.Files.Directories;
import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.BackEnd.Media.MediaPlayerClass;
import com.example.Calls.BackEnd.Services.HistoryTranslateService;
import com.example.Calls.BackEnd.Services.RecordsService;
import com.example.Calls.BackEnd.Settings.SavedSettings;
import com.example.Calls.BackEnd.SharedClasses.SharedMethods;
import com.example.Calls.Dialog.DialogMain;
import com.example.Calls.Dialog.HelpDialog;
import com.example.Calls.Model.Repositories.RecordRepository;
import com.example.Calls.PaintGame.MarkerView;
import com.example.Calls.PaintGame.SamplePlayer;
import com.example.Calls.PaintGame.SongMetadataReader;
import com.example.Calls.PaintGame.WaveformView;
import com.example.Calls.PaintGame.soundFile.SoundFile;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

//TODO декомпозировать код после определения интерфейса и планов на переработку

public class Play extends AppCompatActivity
        implements WaveformView.WaveformListener {

    //dialog windows
    final DialogMain dialogMain = new DialogMain(this, DialogMain.Activities.Play);

    private static Cutter cutter;
    public static Cutter getCutter(){
        return cutter;
    }
    public CutterInterval selectedInterval;
    public EditText editTextStart,editTextEnd;
    private Button buttonBackRewind, buttonFordRewind;

    public Directories directories = new Directories(RecordRepository.getSelectedRecord());

    //layouts
    private LinearLayout startLayoutPage;
    private LinearLayout linerLayoutPaintGame;
    private LinearLayout linerLayoutButtonComplete,linerLayoutRemoveInterval;

    private SamplePlayer mPlayer;
    private MediaPlayer mediaPlayer;
    private Button buttonNoPerson, buttonPerson;
    private Button buttonRawBackPlay, buttonRawForwardPlay;

    //region Paint For Cutter varibles
    private long mLoadingLastUpdateTime;
    private boolean mLoadingKeepGoing;
    private long mRecordingLastUpdateTime;
    private boolean mRecordingKeepGoing;
    private double mRecordingTime;
    private boolean mFinishActivity;
    private TextView mTimerTextView;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private SoundFile mSoundFile;
    private File mFile;
    private String mFilename;
    private String mArtist;
    private String mTitle;
    private int mNewFileKind;
    private boolean mWasGetContentIntent;
    private WaveformView mWaveformView;
    private MarkerView mStartMarker;
    private MarkerView mEndMarker;
    private TextView mStartText;
    private TextView mEndText;
    private TextView mInfo;
    private String mInfoContent;
    private ImageButton mPlayButton;
    private ImageButton mRewindButton;
    private ImageButton mFfwdButton;
    private boolean mKeyDown;
    private String mCaption = "";
    private int mWidth;
    private int mMaxPos;
    private int mStartPos;
    private int mEndPos;
    private boolean mStartVisible;
    private boolean mEndVisible;
    private int mLastDisplayedStartPos;
    private int mLastDisplayedEndPos;
    private int mOffset;
    private int mOffsetGoal;
    private int mFlingVelocity;
    private int mPlayStartMsec;
    private int mPlayEndMsec;
    private Handler mHandler;
    private boolean mIsPlaying;

    private boolean mTouchDragging;
    private float mTouchStart;
    private int mTouchInitialOffset;
    private int mTouchInitialStartPos;
    private int mTouchInitialEndPos;
    private long mWaveformTouchStartMsec;
    private float mDensity;
    private int mMarkerLeftInset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mMarkerBottomOffset;
    private Thread mLoadSoundFileThread;
    private Thread mRecordAudioThread;
    private Thread mSaveSoundFileThread;

    private boolean isButtonNoPerson = false;

    //endregion

    //endregion

    //region load
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.play);

            //Layouts on page
            startLayoutPage = (LinearLayout) (findViewById(R.id.startLayoutPage));
            linerLayoutPaintGame = (LinearLayout) (findViewById(R.id.linerLayoutPaintGame));
            linerLayoutButtonComplete = (LinearLayout) (findViewById(R.id.linerLayoutButtonComplete));
            linerLayoutRemoveInterval = (LinearLayout) (findViewById(R.id.linerLayoutRemoveInterval));

            //generate objects
            //set file name
            mFilename = RecordRepository.getSelectedRecord().Path;
            mSoundFile = null;
            cutter = new Cutter();
            mHandler = new Handler();

            editTextStart = (EditText) findViewById(R.id.editTextStart);
            editTextEnd = (EditText) findViewById(R.id.editTextEnd);
            //region buttons

            //if have file with intervals
            if(cutter.IsHaveInterval()){
                LinearLayout linearLayoutStartPlay = findViewById(R.id.linerLayoutStartPlay);
                LinearLayout linearLayoutHaveIntervals = findViewById(R.id.linerLayoutHaveIntervals);
                linearLayoutStartPlay.setVisibility(View.GONE);
                linearLayoutHaveIntervals.setVisibility(View.VISIBLE);
            }

            //button raw <<

            buttonRawBackPlay = (Button) (findViewById(R.id.buttonRawBackPlay));
            buttonRawBackPlay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(isDown(event)){
                        Log.d("buttonRaw", String.valueOf(isButtonNoPerson));
                        RawBack();
                    }
                    if(isUp(event)){
                        Log.d("startModeWait", "swm");
                    }
                    return false;
                }
            });
            //button raw >>
            buttonRawForwardPlay = (Button) (findViewById(R.id.buttonRawForwardPlay));
            buttonRawForwardPlay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(isDown(event)){
                        if(isButtonNoPerson){
                            addInterval();
                            endIntervalRaw();
                        }
                        RawForward();
                    }
                    if(isUp(event)){
                        Log.d("startModeWait", "swm");
                    }
                    return  false;
                }
            });
            //button No I
            buttonNoPerson = (Button) (findViewById(R.id.buttonNoPerson));
            buttonNoPerson.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(isDown(event)){
                        if(cutter.isNullIntervalList()){
                            buttonRawForwardPlay.setEnabled(true);
                            buttonRawBackPlay.setEnabled(true);
                        }
                        addInterval();
                        isButtonNoPerson = true;
                        stopModeWait();
                    }

                    if(isUp(event)){
                        endInterval();
                        startModeWait();
                    }
                    return false;
                }
            });
            //button I
            buttonPerson = (Button) (findViewById(R.id.buttonPerson));
            buttonPerson.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(isDown(event)){
                        if(cutter.isNullIntervalList()){
                            buttonRawForwardPlay.setEnabled(true);
                            buttonRawBackPlay.setEnabled(true);
                        }
                        isButtonNoPerson = false;
                        stopModeWait();
                    }
                    if(isUp(event)){
                        startModeWait();
                    }
                    return false;
                }
            });

        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onCreatePlay");
        }
    }

    //region buttons start and end
    public void onClickStartPlay(View view){
        try{
            setPlayUI();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickStartPlay");
        }

    }
    //start load and view play
    private void setPlayUI(){
        try{
            loadGui();
            loadFromFile();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "setPlayUI");
        }
    }
    private void VisibilityPlay(){
        startLayoutPage.setVisibility(View.GONE);
        linerLayoutPaintGame.setVisibility(View.VISIBLE);
    }

    public void onClickButtonContinue(View view){
        Directories directories = new Directories(RecordRepository.getSelectedRecord());
        directories.deleteDirectory(new File(FileSystemParameters.getPathForSelectedRecordDir()));
        cutter.FillIntervalsFromFile();
        openWaitEndPlay();

    }
    public void onClickButtonAgain(View view){
        Directories directories = new Directories(RecordRepository.getSelectedRecord());
        directories.deleteDirectory(new File(FileSystemParameters.getPathForSelectedRecordDir()));
        cutter.DeleteIntervalFile();
        setPlayUI();
    }
    //endregion

    //region events buttons
    public void onClickButtonRemove(View view){
        try{
            cutter.RemoveInterval(selectedInterval.getId());
            updateDisplay();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "buttonRemove");
        }
    }
    public void onClickEndGame(View view){
        try{
            //open wait End Play Activity
            if(!cutter.isNullIntervalList()){
                directories.createDirectoryApplication();
                directories.createDirectoryForContact();
                cutter.SaveIntervalsToFile();
                openWaitEndPlay();
            }
            else{
                Toast.makeText(this, "Запись разговора не размечена. Выделите собеседника", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onClickEndGame");
        }
    }
    private void openWaitEndPlay(){
        Intent intent = new Intent(Play.this, WaitInEndPlay.class);
        startActivity(intent);
    }

    //endregion



    //region Load and Update GUI
    private void loadGui() {
        //Inflate our UI from its XML layout description
        try{
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            mDensity = metrics.density;
            mWaveformView = (WaveformView)findViewById(R.id.waveform);
            mWaveformView.setListener(this);

            mMaxPos = 0;
            mLastDisplayedStartPos = -1;
            mLastDisplayedEndPos = -1;

            if (mSoundFile != null && !mWaveformView.hasSoundFile()) {
                mWaveformView.setSoundFile(mSoundFile);
                mWaveformView.recomputeHeights(mDensity);
                mMaxPos = mWaveformView.maxPos();
            }

            updateDisplay();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "loadGuiPlay");
        }

    }
    private void loadFromFile() {
        try{
            mFile = new File(mFilename);
            SongMetadataReader metadataReader = new SongMetadataReader(
                    this, mFilename);
            mTitle = metadataReader.mTitle;
            mArtist = metadataReader.mArtist;

            String titleLabel = mTitle;
            if (mArtist != null && mArtist.length() > 0) {
                titleLabel += " - " + mArtist;
            }
            setTitle(titleLabel);

            mLoadingLastUpdateTime = getCurrentTime();
            mLoadingKeepGoing = true;
            mFinishActivity = false;
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setOnCancelListener(
                    new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            mLoadingKeepGoing = false;
                            mFinishActivity = true;
                        }
                    });
            mProgressDialog.show();

            final SoundFile.ProgressListener listener =
                    new SoundFile.ProgressListener() {
                        public boolean reportProgress(double fractionComplete) {
                            long now = getCurrentTime();
                            if (now - mLoadingLastUpdateTime > 100) {
                                mProgressDialog.setProgress(
                                        (int) (mProgressDialog.getMax() * fractionComplete));
                                mLoadingLastUpdateTime = now;
                            }
                            return mLoadingKeepGoing;
                        }
                    };

            // Load the sound file in a background thread
            mLoadSoundFileThread = new Thread() {
                public void run() {
                    try {
                        mSoundFile = SoundFile.create(mFile.getAbsolutePath(), listener);

                        if (mSoundFile == null) {
                            mProgressDialog.dismiss();
                            String name = mFile.getName().toLowerCase();
                            String[] components = name.split("\\.");
                            String err;
                            if (components.length < 2) {
                                err = "getResources().getString()";

                            } else {
                                err = "getResources().getString()";

                            }
                            final String finalErr = err;
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    showFinalAlert(new Exception(), finalErr);
                                }
                            };
                            mHandler.post(runnable);
                            return;
                        }
                        mPlayer = new SamplePlayer(mSoundFile);
                    } catch (final Exception e) {
                        mProgressDialog.dismiss();
                        e.printStackTrace();
                        mInfoContent = e.toString();
                        runOnUiThread(new Runnable() {
                            public void run() {

                            }
                        });

                        Runnable runnable = new Runnable() {
                            public void run() {

                            }
                        };
                        mHandler.post(runnable);
                        return;
                    }
                    mProgressDialog.dismiss();
                    if (mLoadingKeepGoing) {
                        Runnable runnable = new Runnable() {
                            public void run() {
                                finishOpeningSoundFile();
                            }
                        };
                        mHandler.post(runnable);
                    } else if (mFinishActivity){
                        finish();
                    }
                }
            };
            mLoadSoundFileThread.start();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "loadFromFile");
        }
    }
    private void finishOpeningSoundFile() {
        try{
            mWaveformView.setSoundFile(mSoundFile);
            mWaveformView.recomputeHeights(mDensity);

            mMaxPos = mWaveformView.maxPos();
            mLastDisplayedStartPos = -1;
            mLastDisplayedEndPos = -1;

            mTouchDragging = false;

            mOffset = 0;
            mOffsetGoal = 0;
            mFlingVelocity = 0;
            resetPositions();
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            VisibilityPlay();
            updateDisplay();
            Toast.makeText(this, "Начните выделять свой голос или голос собеседника", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "finishOpeningSoundFile");
        }
    }

    private synchronized void onPlay(int startPosition) {
        try {
            if (mIsPlaying) {
                handlePause();
                return;
            }
            if (mPlayer == null) {
                // Not initialized yet
                return;
            }
            mPlayStartMsec = mWaveformView.pixelsToMillisecs(startPosition);
            if (startPosition < mStartPos) {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mStartPos);
            } else if (startPosition > mEndPos) {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mMaxPos);
            } else {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mEndPos);
            }
            mPlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener() {
                @Override
                public void onCompletion() {
                    handlePause();
                }
            });
            mIsPlaying = true;

            mPlayer.seekTo(mPlayStartMsec);
            mPlayer.start();
            updateDisplay();

        } catch (Exception ex) {
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "onPlay");
        }
    }

    private synchronized void updateDisplay() {
        try{
            if (mIsPlaying) {
                int now = mPlayer.getCurrentPosition();
                int frames = mWaveformView.millisecsToPixels(now);
                mWaveformView.setPlayback(frames);
                setOffsetGoalNoUpdate(frames - mWidth / 2);
                if (now >= mPlayEndMsec) {
                    handlePause();
                }
            }

            if (!mTouchDragging) {
                int offsetDelta;

                if (mFlingVelocity != 0) {
                    offsetDelta = mFlingVelocity / 30;
                    if (mFlingVelocity > 80) {
                        mFlingVelocity -= 80;
                    } else if (mFlingVelocity < -80) {
                        mFlingVelocity += 80;
                    } else {
                        mFlingVelocity = 0;
                    }

                    mOffset += offsetDelta;

                    if (mOffset + mWidth / 2 > mMaxPos) {
                        mOffset = mMaxPos - mWidth / 2;
                        mFlingVelocity = 0;
                    }
                    if (mOffset < 0) {
                        mOffset = 0;
                        mFlingVelocity = 0;
                    }
                    mOffsetGoal = mOffset;
                } else {
                    offsetDelta = mOffsetGoal - mOffset;

                    if (offsetDelta > 10)
                        offsetDelta = offsetDelta / 10;
                    else if (offsetDelta > 0)
                        offsetDelta = 1;
                    else if (offsetDelta < -10)
                        offsetDelta = offsetDelta / 10;
                    else if (offsetDelta < 0)
                        offsetDelta = -1;
                    else
                        offsetDelta = 0;

                    mOffset += offsetDelta;
                }
            }

            //TODO go to start and end position for mWaveformView
            //send list to WaveForm for show intervals
            mWaveformView.setParameters(cutter.getIntervalList(), mOffset);
            mWaveformView.invalidate();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "updateDisplay");
        }
    }
    //endregion

    //region Playing
    private boolean isDown(MotionEvent event){
        return  event.getAction() == MotionEvent.ACTION_DOWN;
    }
    private boolean isUp(MotionEvent event){
        return event.getAction() == MotionEvent.ACTION_UP;
    }

    private void addInterval(){
        int sec = MediaPlayerClass.getCurrentPositionSec(mPlayer.getCurrentPosition());
        cutter.AddInterval(sec);
    }
    private void endInterval(){
        int sec = MediaPlayerClass.getCurrentPositionSec(mPlayer.getCurrentPosition());
        cutter.StopInterval(sec);
    }

    private void endIntervalRaw(){
        int sec = MediaPlayerClass.getCurrentPositionSec(mPlayer.getCurrentPosition()) + 2;
        cutter.StopInterval(sec);
    }

    private void startModeWait(){
        mPlayer.pause();
    }
    private void stopModeWait(){
        if(mPlayer.getCurrentPosition() != 0){
            mPlayer.start();
        }
        else{
            onPlay(0);
        }
    }

    private void RawBack(){
        int currentPosition = mPlayer.getCurrentPosition();
        int backCurrentPosition = currentPosition - 2000;
        if(backCurrentPosition < 0)
            return;
        mPlayer.seekTo(backCurrentPosition);
    }
    private void RawForward(){
        int currentPosition = mPlayer.getCurrentPosition();

        int backCurrentPosition = currentPosition - 2000;
        mPlayer.seekTo(mPlayer.getCurrentPosition() + 2000);
    }
    //endregion

    /**
     * Called when the orientation changes and/or the keyboard is shown
     * or hidden.  We don't need to recreate the whole activity in this
     * case, but we do need to redo our layout somewhat.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try{
            final int saveZoomLevel = mWaveformView.getZoomLevel();
            super.onConfigurationChanged(newConfig);
            loadGui();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    mWaveformView.setZoomLevel(saveZoomLevel);
                    mWaveformView.recomputeHeights(mDensity);
                    updateDisplay();
                }
            }, 500);

        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/onConfigurationChanged");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            onPlay(mStartPos);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //
    // region WaveformListener
    //

    /**
     * Every time we get a message that our waveform drew, see if we need to
     * animate and trigger another redraw.
     */
    public void waveformDraw() {
        try{
            mWidth = mWaveformView.getMeasuredWidth();
            if (mOffsetGoal != mOffset && !mKeyDown)
                updateDisplay();
            else if (mIsPlaying) {
                updateDisplay();
            } else if (mFlingVelocity != 0) {
                updateDisplay();
            }
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/waveformDraw");
        }
    }

    public void waveformTouchStart(float x) {
        try{
            selectedInterval = null;
            List<CutterInterval> listIntervals = cutter.getIntervalList();
            int position = mPlayer.getCurrentPosition()/1000;
            for(CutterInterval interval : listIntervals){
                Log.d("id", String.valueOf(interval.getId()));
                if(interval.getStart() < position && position < interval.getEnd()){
                    selectedInterval = interval;
                    setUIForSelectedInterval();
                    break;
                }
            }

            if(selectedInterval != null){
                linerLayoutRemoveInterval.setVisibility(View.VISIBLE);
            }
            else{
                linerLayoutRemoveInterval.setVisibility(View.GONE);
            }

            mTouchDragging = true;
            mTouchStart = x;
            mTouchInitialOffset = mOffset;
            mFlingVelocity = 0;
            mWaveformTouchStartMsec = getCurrentTime();

        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/waveformTouchStart");
        }
    }
    private void setUIForSelectedInterval(){
        editTextStart.setText(SharedMethods.secondsToTime(selectedInterval.getStart()));
        editTextEnd.setText(SharedMethods.secondsToTime(selectedInterval.getEnd()));
    }

    public void waveformTouchMove(float x) {
        mOffset = trap((int)(mTouchInitialOffset + (mTouchStart - x)));
        updateDisplay();
    }

    public void waveformTouchEnd() {
        try{
            mTouchDragging = false;
            mOffsetGoal = mOffset;

            long elapsedMsec = getCurrentTime() - mWaveformTouchStartMsec;
            if (elapsedMsec < 300) {
                if (mIsPlaying) {
                    int seekMsec = mWaveformView.pixelsToMillisecs(
                            (int)(mTouchStart + mOffset));
                    if (seekMsec >= mPlayStartMsec &&
                            seekMsec < mPlayEndMsec) {
                        mPlayer.seekTo(seekMsec);
                    } else {
                        handlePause();
                    }
                } else {
                    onPlay((int)(mTouchStart + mOffset));
                }
            }
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/waveformTouchEnd");
        }
    }

    public void waveformFling(float vx) {
        try{
            mTouchDragging = false;
            mOffsetGoal = mOffset;
            mFlingVelocity = (int)(-vx);
            updateDisplay();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/waveformFling");
        }
    }

    public void waveformZoomIn() {
        try{
            mWaveformView.zoomIn();
            mStartPos = mWaveformView.getStart();
            mEndPos = mWaveformView.getEnd();
            mMaxPos = mWaveformView.maxPos();
            mOffset = mWaveformView.getOffset();
            mOffsetGoal = mOffset;
            updateDisplay();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/waveformZoomIn");
        }
    }

    public void waveformZoomOut() {
        try{
            mWaveformView.zoomOut();
            mStartPos = mWaveformView.getStart();
            mEndPos = mWaveformView.getEnd();
            mMaxPos = mWaveformView.maxPos();
            mOffset = mWaveformView.getOffset();
            mOffsetGoal = mOffset;
            updateDisplay();
        }
        catch (Exception ex){
            dialogMain.showErrorDialogAndTheOutputLogs(ex, "Play/waveformZoomOut");
        }
    }

    //endregion

    //
    // Internal methods
    //

    private Runnable mTimerRunnable = new Runnable() {
        public void run() {
            // Updating an EditText is slow on Android.  Make sure
            // we only do the update if the text has actually changed.
            if (mStartPos != mLastDisplayedStartPos &&
                    !mStartText.hasFocus()) {
                mLastDisplayedStartPos = mStartPos;
            }
            if (mEndPos != mLastDisplayedEndPos &&
                    !mEndText.hasFocus()) {
                mLastDisplayedEndPos = mEndPos;
            }
            mHandler.postDelayed(mTimerRunnable, 100);
        }
    };

    private void resetPositions() {
        mStartPos = mWaveformView.secondsToPixels(0.0);
        mEndPos = mWaveformView.secondsToPixels(1000);
    }

    private int trap(int pos) {
        if (pos < 0)
            return 0;
        if (pos > mMaxPos)
            return mMaxPos;
        return pos;
    }

    private void setOffsetGoalNoUpdate(int offset) {
        if (mTouchDragging) {
            return;
        }
        mOffsetGoal = offset;
        if (mOffsetGoal + mWidth / 2 > mMaxPos)
            mOffsetGoal = mMaxPos - mWidth / 2;
        if (mOffsetGoal < 0)
            mOffsetGoal = 0;
    }

    private synchronized void handlePause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        mWaveformView.setPlayback(-1);
        mIsPlaying = false;
    }

    /**
     * Show a "final" alert dialog that will exit the activity
     * after the user clicks on the OK button.  If an exception
     * is passed, it's assumed to be an error condition, and the
     * dialog is presented as an error, and the stack trace is
     * logged.  If there's no exception, it's a success message.
     */
    private void showFinalAlert(Exception e, CharSequence message) {
        CharSequence title;
        if (e != null) {
            Log.e("Ringdroid", "Error: " + message);
            Log.e("Ringdroid", getStackTrace(e));
            title = getResources().getText(R.string.alert_title_failure);
            setResult(RESULT_CANCELED, new Intent());
        } else {
            Log.v("Ringdroid", "Success: " + message);
            title = getResources().getText(R.string.alert_title_success);
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        R.string.alert_ok_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();
                            }
                        })
                .setCancelable(false)
                .show();
    }

    private void showFinalAlert(Exception e, int messageResourceId) {
        showFinalAlert(e, getResources().getText(messageResourceId));
    }

    private long getCurrentTime() {
        return System.nanoTime() / 1000000;
    }

    private String getStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    //region Destroy
    /** Called when the activity is finally destroyed. */
    @Override
    protected void onDestroy() {
        mLoadingKeepGoing = false;
        mRecordingKeepGoing = false;
        mLoadSoundFileThread = null;
        mRecordAudioThread = null;
        mSaveSoundFileThread = null;
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if(mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        if (mPlayer != null) {
            if (mPlayer.isPlaying() || mPlayer.isPaused()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }

    /** Called with an Activity we started with an Intent returns. */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent dataIntent) {
        Log.v("Ringdroid", "EditActivity onActivityResult");
    }


    @Override
    public void onBackPressed() {
        dialogMain.showQuestionDialogPlay();
    }

    //endregion

}
