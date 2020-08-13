package com.example.Calls;

import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.TextView;


public class ProgressTextView extends AppCompatTextView {

    // Максимальное значение шкалы
    private int mMaxValue = 100;

    // Конструкторы
    public ProgressTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ProgressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressTextView(Context context) {
        super(context);
    }

    // Установка максимального значения
    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
    }

    // Установка значения
    public synchronized void setValue(int value) {
        // Установка новой надписи
        this.setText(String.valueOf(value));

        // Drawable, отвечающий за фон (получаем из xml)
        LayerDrawable background = (LayerDrawable) this.getBackground();

        // Достаём Clip, отвечающий за шкалу, по индексу 1 (думаю)
        ClipDrawable barValue = (ClipDrawable) background.getDrawable(1);

        // Устанавливаем уровень шкалы (work)
        int newClipLevel = (int) (value * 100 / mMaxValue);

        barValue.setLevel(newClipLevel);
        //barValue.draw(canvas);

        // Уведомляем об изменении Drawable
        drawableStateChanged();
    }
}
