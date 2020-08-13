package com.example.Calls.BackEnd.CheapSound;

import com.example.Calls.BackEnd.CheapSound.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Utilities {


    private static Logger log = Logger.getLogger(Utilities.class.getName());
    /**
     * Метод для создания фреймов
     * @param seconds Время в секундах
     * @param mSampleRate Это просто нужно
     * @param mSamplesPerFrame и это просто нужно
     * @return
     */
    protected static int secondsToFrames(double seconds, int mSampleRate,
                                         int mSamplesPerFrame) {
        return (int) (1.0 * seconds * mSampleRate / mSamplesPerFrame + 0.5);
    }

    /**
     * Метод для обрезки аудио файлов
     * @param path Путь к аудио
     * @param start Время старта
     * @param end время окончания
     * @param stage Стадия деления файла
     * @throws IOException
     */
    public static void FileCutter(String path, double start, double end, String stage) throws IOException {

        String pathCut = "/data/data/com.example.Calls/cache/";
        CheapSoundFile soundFile = CheapSoundFile.create(path);
        log.info("filecut1");
        File s = new File(pathCut+stage+".mp3");
        log.info("filecut2");
        int mSampleRate = soundFile.getSampleRate();
        log.info("filecut3");
        int mSamplesPerFrame = soundFile.getSamplesPerFrame();
        log.info("filecut4");
        int startFrame = secondsToFrames(start,mSampleRate, mSamplesPerFrame);
        int endFrame = secondsToFrames(end, mSampleRate,mSamplesPerFrame);
        log.info("filecut5");
        soundFile.WriteFile(s.getAbsoluteFile(), startFrame, endFrame-startFrame);
    }
}
