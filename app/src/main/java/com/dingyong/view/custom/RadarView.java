package com.dingyong.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/4/18.
 * RadarView
 */
public class RadarView extends View {
    private final String TAG = RadarView.class.getSimpleName();
    private int mCount = 6;
    private int mCenterX;
    private int mCenterY;
    private float mRadius;
    private Paint mMainPaint;
    private float mAngle = (float) (Math.PI * 2 / mCount);

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
        mMainPaint.setColor(Color.GRAY);
        mMainPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        mRadius = Math.min(w, h) / 2 * 0.9f;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
        drawLine(canvas);
    }

    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        for (int j = 0; j < mCount; j++) {
            float curRadius = mRadius / (mCount - 1) * j;
            for (int i = 0; i < mCount; i++) {
                if (i == 0) {
                    path.moveTo(mCenterX + curRadius, mCenterY);
                } else {
                    float x = (float) (mCenterX + curRadius * Math.cos(i * mAngle));
                    float y = (float) (mCenterY + curRadius * Math.sin(i * mAngle));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mMainPaint);
        }
    }


    private void drawLine(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < mCount; i++) {
            path.moveTo(mCenterX, mCenterY);
            float x = (float) (mCenterX + mRadius * Math.cos(i * mAngle));
            float y = (float) (mCenterY + mRadius * Math.sin(i * mAngle));
            path.lineTo(x, y);
        }
        path.close();
        canvas.drawPath(path, mMainPaint);
    }
}
