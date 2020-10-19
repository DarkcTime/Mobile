package com.example.Calls.Dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.Calls.MainActivity;
import com.example.Calls.R;


public class HelpDialogFirstLaunch extends AppCompatDialogFragment {

    private MainActivity MainActivity;

    public void setMainActivity(MainActivity _MainActivity){
        MainActivity = _MainActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
                            if(MainActivity != null) MainActivity.askPermission();
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
        catch (Exception ex){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Exception")
                    .setMessage(ex.getMessage());
            return builder.create();
        }
    }
}
