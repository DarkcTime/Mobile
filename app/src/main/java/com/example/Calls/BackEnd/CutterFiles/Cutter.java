package com.example.Calls.BackEnd.CutterFiles;

import android.content.Context;
import android.util.Log;

import com.example.Calls.BackEnd.Files.Directories;
import com.example.Calls.BackEnd.Files.FileSystem;
import com.example.Calls.BackEnd.Files.FileSystemParameters;
import com.example.Calls.Model.Repositories.RecordRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Данный класс содержит в себе методы для управления интервалами
 * и запускает обрезку файлов
 */

public class Cutter {

    private List<CutterInterval> intervalList;

    private final String pathFile = FileSystemParameters.getPathForSelectedContact() + RecordRepository.getSelectedRecord().FullName + ".txt";
    public List<CutterInterval> getIntervalList() {
        return intervalList;
    }
    public boolean isNullIntervalList(){
        return intervalList.isEmpty();
    }

    private int id = 0;

    private CutterInterval interval;

    //record have duration > 19 sec
    //don't work with Api
    private final int MAX_DURATION = 19;

    public void AddInterval(int start){
        interval = new CutterInterval(id, start);
        id++;
    }
    public void StopInterval(int end) {
        interval.setEnd(end);
        intervalList.add(interval);
    }
    public void RemoveInterval(int id){
        intervalList.remove(id);
    }

    public boolean IsHaveEnd(){
        return interval.getEnd() != 0;
    }

    public boolean IsNullInterval(){
        return interval == null;
    }

    private boolean checkDurationInterval(int end){
        int duration = end - interval.getStart();
        return duration > MAX_DURATION;
    }

    private void splitTheInterval(int end){
        StopInterval(interval.getStart() + MAX_DURATION);
        AddInterval(interval.getStart() + MAX_DURATION);
        StopInterval(end);
    }


    public Cutter(){
        intervalList = new ArrayList<CutterInterval>();
    }

    //полный путь с именем записи
    //контакт

    public void startCutFileIntervals(Context _context) throws Exception {
        FFmpegCutter.isStop = false;

        FFmpegCutter fFmpegCutter = new FFmpegCutter(_context);

        fFmpegCutter.executeCommandForCutFileAfterPlay(getFilesForCutter(intervalList));

    }

    public void SaveIntervalsToFile(){
        try{
            for(CutterInterval cutterInterval : getIntervalList()){
                String id = String.valueOf(cutterInterval.getId());
                String start = String.valueOf(cutterInterval.getStart());
                String end = String.valueOf(cutterInterval.getEnd());

                String str = id + "," + start + "," + end;
                String path = FileSystemParameters.getPathForSelectedContact() + RecordRepository.getSelectedRecord().FullName + ".txt";
                FileSystem.WriteFile(path, str, true, true);
            }
        }
        catch (Exception ex){
            Log.d("SaveIntervalsToFile", ex.getMessage());
        }
    }
    public boolean IsHaveInterval(){
        return new File(pathFile).exists();
    }
    public void FillIntervalsFromFile(){
        try{
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(pathFile));
            String line = reader.readLine();
            while (line != null) {
                String[] numbers = line.split(",");
                AddInterval(Integer.parseInt(numbers[1]));
                StopInterval(Integer.parseInt(numbers[2]));
                line = reader.readLine();
            }
            reader.close();

        }
        catch (Exception ex){
            Log.d("FillIntervalsFromFile", ex.getMessage());
        }
    }
    public void DeleteIntervalFile(){
        try{
            new File((pathFile)).delete();
        }catch (Exception exception){
            Log.d("FillIntervalsFromFile", exception.getMessage());
        }
    }

    /**
     * Генерирует список файлов для резчика,
     * параметры файлов устанавливаются на основе листа с интервалами
     * @param intervalList
     * @return
     */
    public static List<FileForCutter> getFilesForCutter(List<CutterInterval> intervalList){
        int i = 0;
        List<FileForCutter> fileForCutterList = new ArrayList<FileForCutter>();

        for (CutterInterval interval : intervalList) {
            //generation parameters
            int start = interval.getStart();
            int end = interval.getEnd();
            //remove incorrect intervals
            if(end <= start)
                continue;

            int duration = end - start;

            File targetFile = new File(FileSystemParameters.getPathForSelectedRecordsForCutter().concat(String.valueOf(i)).concat(".mp3"));
            File sourceFile = new File(RecordRepository.getSelectedRecord().Path);

            //генерация объекта
            FileForCutter fileForCutter = new FileForCutter(interval.getStart(),
                    duration,
                    sourceFile,
                    targetFile);

            //заполнение листа
            fileForCutterList.add(fileForCutter);
            i++;
        }

        return fileForCutterList;
    }



}
