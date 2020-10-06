package com.example.Calls.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.BackEnd.Api.ApiMain;
import com.example.Calls.MainActivity;
import com.example.Calls.Play;
import com.example.Calls.R;
import com.example.Calls.WaitInEndPlay;

import java.io.IOException;

import static com.example.Calls.Dialog.MyDialogHelp.Windows.API;

public class MyDialogHelp extends AppCompatDialogFragment{

    //TODO refactor getButton!

    private Windows window;

    private MainActivity MainActivityContext;

    private WaitInEndPlay WaitEndPlayContext;

    private Play PlayContext;

    @SuppressLint("ValidFragment")
    public MyDialogHelp(MainActivity _context,Windows _window){
        MainActivityContext = _context;
        window = _window;
    }

    @SuppressLint("ValidFragment")
    public MyDialogHelp(WaitInEndPlay _context,Windows _window){
        WaitEndPlayContext = _context;
        window = _window;
    }

    @SuppressLint("ValidFragment")
    public MyDialogHelp(Play _context, Windows _window){
        PlayContext = _context;
        window = _window;
    }

    public MyDialogHelp(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            switch (window){
                case HELP:
                    builder.setTitle("Справка")
                            .setIcon(R.drawable.que)
                            .setMessage("1) Ознакомьтесь со справкой\n2) Выберите распложение записей разговоров в настройках\n(если марка телефона Xiaomi," +
                                    "продолжайте далее, в ином случае обратитесь к разделу `Помощь`\n" +
                                    "3) Нажмите кнопку `Начать использование`\n"
                                    + "4) В списке записей выберите необходимую\n"
                                    + "5) Нажмите кнопку перевода записи в текст и ожидайте результат\n"
                                    + "6) Прослушайте запись и отредактируйте слова собеседника\n"
                                    + "7) Нажмите кнопку сохранения записи\n"
                                    + "8) Повторите шаги 4 - 7 со всеми записями или нажмите кнопку общего перевода\n"
                                    + "9) Соберите 1000 слов и зайдите в общее окно для пользователя\n"
                                    + "10) Получив 1000 слов откроется доступ к кнопке `Получить психологический портрет`")
                            .setPositiveButton("Ок, понял", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Закрываем окно
                                    MainActivityContext.askPermission();
                                    dialog.cancel();
                                }
                            });

                    break;
                case PLAY:
                    builder.setTitle("Голосовые потоки")
                            .setIcon(R.drawable.success)
                            .setMessage("Цель: выделить свой голос и голос собеседника,\n" +
                                    "для определения психологических портретов.\n" +
                                    "Алгоритм использования:\n" +
                                    "1) Нажать кнопку - начать" +
                                    "2) Выделять свой голос и голос собеседника\n" +
                                    "3) После разметки всей записи, нажать кнопку завершить\n" +
                                    "Функционал кнопок:\n" +
                                    "1) Кнопка начать, запускает аудио дорожку\n" +
                                    "2) Кнопка Я выделяет ваш голос\n" +
                                    "3) Кнопка Собеседник выделяет голос собеседника\n" +
                                    "4) Кнопки << и >> проматывают аудио, выделяю собеседника\n" +
                                    "5) При бездействии останавливается воспроизведение дорожки\n" +
                                    ",появляется кнопка завершить")
                            .setPositiveButton("ОК, понял", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Закрываем окно
                                    dialog.cancel();
                                }
                            });
                case PERMISSIONS:
                    builder.setTitle("Дать приложению разрешения")
                            .setMessage("Если вы не дадите разрешения, то прижение не сможет использовать задуманный функционал( \n" +
                                    "Приложение использует защищенный протокол обработки данных. Также все записи и результаты переводов храняться на вашем телефоне без использования сторонних серверов")
                            .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Закрываем окно
                                    dialog.cancel();
                                    MainActivityContext.askPermission();
                                }
                            })
                            .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    MainActivityContext.finish();
                                }
                            });
                case API:
                    try{
                        builder.setTitle("Результат текста полученный в данной записи")
                                .setIcon(R.drawable.success)
                                .setMessage(ApiMain.readFullFileSelectedContact())
                                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        WaitEndPlayContext.dialogResultAdd();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        WaitEndPlayContext.dialogResultCancel();
                                        dialog.cancel();
                                    }
                                });
                    }
                    catch (IOException ioe)
                    {
                        Log.d("MyDialogHelpEx", ioe.getMessage());
                    }
                case MEDIA:
                    builder.setTitle("Запись выделена")
                            .setIcon(R.drawable.media)
                            .setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PlayContext.startCutterAndTranslateRecord();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Вернуться к выбору записи", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PlayContext.startAboutContact();
                                    dialog.cancel();
                                }
                            });

            }

            return builder.create();
    }

    public enum Windows{
        MEDIA, HELP, PERMISSIONS, API, PLAY, TEST,
    }




}
