package com.example.Calls.BackEnd.CheapSound;

import android.util.Log;

import com.example.Calls.BackEnd.CutterFiles.CutterInterval;
import com.example.Calls.BackEnd.FilesWork;
import com.example.Calls.BackEnd.Records;
import com.example.Calls.BackEnd.SharedVariables;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Cut {
    private static Logger log = Logger.getLogger(Utilities.class.getName());

    private List<CutterInterval> intervalList;

    private CutterInterval interval;

    public void AddInterval(int start){
        interval = new CutterInterval(start);
    }

    public void StopInterval(int end) {
        interval.setEnd(end);
        intervalList.add(interval);
    }

    public List<CutterInterval> getIntervalList(){
        return intervalList;
    }

    public Cut(){
        intervalList = new ArrayList<CutterInterval>();

    }

    //полный путь с именем записи
    //контакт
    public void Cutter(String nameRec) throws Exception {
        int i = 0;
        //получение записи для чтения
        SoundFileCutter soundFile = SoundFileCutter.create(Records.pathForFindRecords + nameRec);

        //создание файловой директории для записи
        new File(FilesWork.getPathForOnlyRecord(nameRec)).mkdir();

        Log.d("filerecord", "директория не создалась");

        //создание файловой директории для сохранения отдельных записей
        new File(FilesWork.getPathForListRecord(nameRec)).mkdir();

        Log.d("filerecord", "директория не создалась");

        for (CutterInterval interval : intervalList) {
            FileCutter(nameRec,soundFile,interval.getStart(),interval.getEnd(),String.valueOf(i));
            i++;
        }
    }


    private void FileCutter(String nameRec, SoundFileCutter soundFile, float start, float end, String stage) throws Exception {

        //имя папки помещаемое в файл
        File fileName = new File(FilesWork.getPathForListRecord(nameRec).concat("/").concat(stage + ".mp3"));

        assert soundFile != null;
        int startFrame = soundFile.getFrameFromSeconds(start);
        int endFrame = soundFile.getFrameFromSeconds(end);

        soundFile.WriteFile(fileName.getAbsoluteFile(), (int)startFrame, (int) endFrame - startFrame);
    }

    //path for create dir
        private String getPathCut(String nameRec){
        return SharedVariables.getPathApplicationFileSystem() + nameRec.replace(".mp3", "");
    }

}
