package com.dingyong.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dingyong.view.R;

/**
 * Created by Administrator on 2017/4/21.
 * TempControlView
 */
public class TempControlView extends View {
    private int mWidth;
    private int mHeight;
    private float mDialRadius;
    private float mArcRadius;
    private int mDialHeight = DensityUtil.dip2px(getContext(), 10);

    private Paint mDialPaint;
    private Paint mArcPaint;
    private Paint mTitlePaint;
    private Paint mTemperaturePaint;
    private Paint mButtonPaint;

    private Paint mTempPaint;

    private int mCenterX;
    private int mCenterY;
    private int mMinTemp = 15;
    private int mMaxTemp = 30;
    private int mTemperature = 16;

    private String mTitle = "最高温度设置";

    private Bitmap mButtonImage = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_rotate);
    private Bitmap mButtonImageShadow = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_rotate_shadow);
    private float mRotateAngle;
    // 抗锯齿
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public TempControlView(Context context) {
        this(context, null);
    }

    public TempControlView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TempControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDialPaint = new Paint();
        mDialPaint.setAntiAlias(true);
        mDialPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2f));
        mArcPaint.setColor(Color.parseColor("#3CB7EA"));

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setTextSize(DensityUtil.dip2px(getContext(), 15f));
        mTitlePaint.setColor(Color.parseColor("#3B434E"));


        mTemperaturePaint = new Paint();
        mTemperaturePaint.setAntiAlias(true);
        mTemperaturePaint.setColor(Color.parseColor("#E37364"));
        mTemperaturePaint.setStyle(Paint.Style.STROKE);
        mTemperaturePaint.setTextSize(DensityUtil.dip2px(getContext(), 18f));

        mButtonPaint = new Paint();
        mButtonPaint.setAntiAlias(true);

        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);


        mTempPaint = new Paint();
        mTempPaint.setAntiAlias(true);
        mTempPaint.setTextSize(DensityUtil.dip2px(getContext(), 50));
        mTempPaint.setColor(Color.parseColor("#E37364"));


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mHeight = Math.min(w, h);
        mDialRadius = mWidth / 2 - DensityUtil.dip2px(getContext(), 20);
        mArcRadius = mDialRadius - DensityUtil.dip2px(getContext(), 20);
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScale(canvas);
        drawArc(canvas);
        drawTitle(canvas);
        drawButton(canvas);
        drawTemp(canvas);
    }


    private void drawScale(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(-135f);
        mDialPaint.setColor(Color.parseColor("#3CB7EA"));
        for (int i = 0; i < 61; i++) {
            canvas.drawLine(0, -mDialRadius, 0, -mDialRadius + mDialHeight, mDialPaint);
            canvas.rotate(4.5f);
        }
        canvas.rotate(85.5f);
        mDialPaint.setColor(Color.parseColor("#E37364"));
        for (int i = 0; i < ((mTemperature - mMinTemp) * 4) + 1; i++) {
            canvas.drawLine(0, -mDialRadius, 0, -mDialRadius + mDialHeight, mDialPaint);
            canvas.rotate(4.5f);
        }
        canvas.restore();
    }


    private void drawArc(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(135f);
        canvas.drawArc(new RectF(-mArcRadius, -mArcRadius, mArcRadius, mArcRadius), 0f, 270f, false, mArcPaint);
        canvas.restore();
    }

    private void drawTitle(Canvas canvas) {
        canvas.save();
        int titleWidth = (int) mTitlePaint.measureText(mTitle);
        canvas.drawText(mTitle, (mWidth - titleWidth) / 2, mDialRadius * 2 + DensityUtil.dip2px(getContext(), 15), mTitlePaint);

        canvas.rotate(48f, mWidth / 2, mHeight / 2);
        String maxTemp = String.valueOf(mMaxTemp);
        String minTemp = String.valueOf(mTemperature);
        int maxTitleWidth = (int) mTemperaturePaint.measureText(maxTemp);
        int minTitleWidth = (int) mTemperaturePaint.measureText(minTemp);
        canvas.drawText(minTemp, (mWidth - minTitleWidth) / 2, mHeight, mTemperaturePaint);

        canvas.rotate(-95f, mWidth / 2, mHeight / 2);
        canvas.drawText(maxTemp, (mWidth - maxTitleWidth) / 2, mHeight, mTemperaturePaint);
        canvas.restore();
    }

    private void drawButton(Canvas canvas) {
        canvas.save();
        int buttonWidth = mButtonImage.getWidth();
        int buttonHeight = mButtonImage.getHeight();
        int buttonShadowWidth = mButtonImageShadow.getWidth();
        int buttonShadowHeight = mButtonImageShadow.getHeight();

        canvas.drawBitmap(mButtonImageShadow, (mWidth - buttonShadowWidth) / 2, (mHeight - buttonShadowHeight) / 2, mButtonPaint);

        Matrix matrix = new Matrix();
        matrix.setTranslate(buttonWidth / 2, buttonHeight / 2);
        matrix.preRotate(45f + mRotateAngle);
        matrix.preTranslate(-buttonWidth / 2, -buttonHeight / 2);
        matrix.postTranslate((mWidth - buttonWidth) / 2, (mHeight - buttonHeight) / 2);
        canvas.drawBitmap(mButtonImage, matrix, mButtonPaint);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.restore();
    }


    private void drawTemp(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        String tempStr = String.valueOf(mTemperature) + "°";
        float tempWidth = mTempPaint.measureText(tempStr);
        float tempHeight = (mTempPaint.ascent() + mTempPaint.descent()) / 2;
        canvas.drawText(tempStr, -tempWidth / 2, -tempHeight, mTempPaint);

    }


    private boolean isDown;
    private boolean isMove;
    float currentAngle = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                float downX = event.getX();
                float downY = event.getY();
                currentAngle = calcAngle(downX, downY);
                Log.d("currentAngle:=,", "" + currentAngle);
                break;
            case MotionEvent.ACTION_MOVE:
                float targetX = event.getX();
                float targetY = event.getY();
                float angle = calcAngle(targetX, targetY);
                float angleIncreased = angle - currentAngle;
                if (angleIncreased > 270) {
                    angleIncreased = angleIncreased - 360;
                } else if (angleIncreased < -270) {
                    angleIncreased = angleIncreased + 360;
                }
                addRotateAngle(angleIncreased);
                currentAngle = angle;
                invalidate();
                isMove = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isMove && isDown) {
                    mRotateAngle = (float) ((mTemperature - mMinTemp) * 4 * 4.5);

                    invalidate();
                    // 回调温度改变监听
                    isDown = false;
                    isMove = false;
                }
                break;
        }
        return true;
    }

    private float calcAngle(float targetX, float targetY) {
        float x = targetX - mWidth / 2;
        float y = targetY - mHeight / 2;
        double radian;
        if (x != 0) {
            float tan = Math.abs(y / x);
            if (x > 0) {
                if (y >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = Math.PI / 2;
            } else {
                radian = -Math.PI / 2;
            }
        }
        return (float) ((radian * 180) / Math.PI);
    }


    private void addRotateAngle(float angle) {
        mRotateAngle += angle;
        if (mRotateAngle <= 0) {
            mRotateAngle = 0;
        } else if (mRotateAngle >= 270) {
            mRotateAngle = 270;
        }
        
        mTemperature = (int) (mRotateAngle / 4.5) / 4 + mMinTemp;
    }

}
