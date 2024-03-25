// SpeedometerNeedle.java
package com.example.gps_speedometer;

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
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = getWidth() / 2 - 20; // İğnenin etrafındaki dairenin yarı çapı

        // Hız göstergesi daireni çiz
        canvas.drawCircle(centerX, centerY, radius, gaugePaint);

        // Hız çizgilerini ve rakamlarını ekleyin
        for (int i = -90; i <= 270; i += 20) { // Start at -90 to place the top axis at 0, increment by 20 for a gap of 20 km
            float angle = (float) Math.toRadians(i);
            float startX = centerX + (float) Math.cos(angle) * (radius - 10);
            float startY = centerY + (float) Math.sin(angle) * (radius - 10);
            float endX = centerX + (float) Math.cos(angle) * (radius - 30);
            float endY = centerY + (float) Math.sin(angle) * (radius - 30);

            // Hız çizgilerini çiz
            canvas.drawLine(startX, startY, endX, endY, gaugePaint);

            // Rakamları ekleyin
            String text = String.valueOf((i + 90) / 20 * 20); // Add 90 to account for the starting angle, multiply by 20 for a gap of 20 km
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            float textX = centerX + (float) Math.cos(angle) * (radius - 40);
            float textY = centerY + (float) Math.sin(angle) * (radius - 40) + (float) bounds.height() / 2;
            canvas.drawText(text, textX, textY, textPaint);
        }

        // Hızın derece cinsinden açısını ayarla (duyarlılığı kontrol etmek için çarpanı ayarla)
        float angle = speed * 2; // İğnenin hassasiyetini kontrol etmek için çarpanı ayarla

        // İğneyi belirtilen açıda çiz
        canvas.rotate(angle, centerX, centerY);
        canvas.drawLine(centerX, centerY, centerX, centerY - radius + 10, needlePaint);
    }
}
