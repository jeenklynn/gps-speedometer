package com.example.gps_speedometer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class SpeedometerNeedle extends View {

    private Paint needlePaint;
    private Paint gaugePaint;
    private Paint textPaint;
    private float speed;
    private float angle;

    public SpeedometerNeedle(Context context) {
        super(context);
        init();
    }

    public SpeedometerNeedle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpeedometerNeedle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        needlePaint = new Paint();
        needlePaint.setColor(Color.RED);
        needlePaint.setStrokeWidth(10);

        gaugePaint = new Paint();
        gaugePaint.setColor(Color.GRAY);
        gaugePaint.setStyle(Paint.Style.STROKE);
        gaugePaint.setStrokeWidth(20);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24);
        textPaint.setTextAlign(Paint.Align.CENTER);

        angle = 0;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        calculateAngle();
        invalidate();
    }

    private void calculateAngle() {
        angle = 180 - speed;
        if (angle < 0)
            angle += 360;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = getWidth() / 2 - 20;

        canvas.drawCircle(centerX, centerY, radius, gaugePaint);

        for (int i = 180; i >= -180; i -= 10) {
            float angle = (float) Math.toRadians(i);
            float startX = centerX + (float) Math.cos(angle) * (radius - 10);
            float startY = centerY - (float) Math.sin(angle) * (radius - 10);
            float endX = centerX + (float) Math.cos(angle) * (radius - 30);
            float endY = centerY - (float) Math.sin(angle) * (radius - 30);

            canvas.drawLine(startX, startY, endX, endY, gaugePaint);

            String text = String.valueOf((i + 360) % 360);
            @SuppressLint("DrawAllocation") Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            float textX = centerX - (float) Math.cos(angle) * (radius - 70) - (float) bounds.width() / 2; // Rakamın yerini ayarla
            float textY = centerY - (float) Math.sin(angle) * (radius - 70) + (float) bounds.height() / 2;
            if (i % 90 == 0) {
                textX = centerX - (float) Math.cos(angle) * (radius - 90) - (float) bounds.width() / 2; // Büyük rakamların yerini ayarla
                textY = centerY - (float) Math.sin(angle) * (radius - 90) + (float) bounds.height() / 2;
            }
            canvas.drawText(text, textX, textY, textPaint);
        }

        canvas.rotate(-angle, centerX, centerY);

        canvas.drawLine(centerX, centerY, centerX + radius - 10, centerY, needlePaint);
    }
}