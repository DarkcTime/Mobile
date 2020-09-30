package com.example.Calls.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.MainActivity;
import com.example.Calls.R;
import com.example.Calls.WaitInEndPlay;

import java.io.IOException;

public class MyDialogHelp extends AppCompatDialogFragment{

    //TODO refactor getButton!

    private static String ClickResultMain = "";

    private static void setClickResultMain(String _clickResult){
        ClickResultMain = _clickResult;
    }

    public static String getClickResultMain(){
        return ClickResultMain;
    }

    public static int getButton;

    private MainActivity context;

    private WaitInEndPlay contextWaitEndPlay;

    @SuppressLint("ValidFragment")
    public MyDialogHelp(MainActivity _context,int _getButton){
        context = _context;
        getButton = _getButton;
    }

    @SuppressLint("ValidFragment")
    public MyDialogHelp(WaitInEndPlay _context,int _getButton){
        contextWaitEndPlay = _context;
        getButton = _getButton;
    }

    public MyDialogHelp(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        switch (getButton){
            case 0:
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
                                context.askPermission();
                                dialog.cancel();
                            }
                        });

                break;
            case 1:
                builder.setTitle("Голосовые потоки")
                        .setIcon(R.drawable.que)
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
            case 2:
                builder.setTitle("Запись звонка успешно выделена")
                        .setMessage("Здесь должен быть текст который получиться после перевода записи")
                        .setPositiveButton("ОК, понял", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Закрываем окно
                                dialog.cancel();
                            }
                        });
            case 3:
                builder.setTitle("Дать приложению разрешения")
                        .setMessage("Если вы не дадите разрешения, то прижение не сможет использовать задуманный функционал( \n" +
                                "Приложение использует защищенный протокол обработки данных. Также все записи и результаты переводов храняться на вашем телефоне без использования сторонних серверов")
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Закрываем окно
                                dialog.cancel();
                                context.askPermission();
                            }
                        })
                        .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                context.finish();
                            }
                        });
            case 4:
                try{
                    builder.setTitle("Результат текста полученный в данной записи")
                            .setMessage(contextWaitEndPlay.getApiMain().readFullFileSelectedRecord())
                            .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    contextWaitEndPlay.dialogResultAdd();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    contextWaitEndPlay.dialogResultCancel();
                                    dialog.cancel();
                                }
                            });
                }
                catch (IOException ioe)
                {
                    Log.d("MyDialogHelpEx", ioe.getMessage());
                }

        }

        return builder.create();
    }




}
