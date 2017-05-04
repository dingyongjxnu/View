package com.dingyong.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.dingyong.view.R;

/**
 * Created by Administrator on 2017/4/20.
 * CreditScoreView
 */
public class CreditScoreView extends View {
    private int mDataCount = 5;
    private int mWidth;
    private int mHeight;
    private float mRadius;
    private float mRadian = (float) (Math.PI * 2 / mDataCount);
    private int mCenterX;
    private int mCenterY;
    private Paint mMainPaint;
    private Paint mValuePaint;
    private Paint mScorePaint;
    private Paint mTitlePaint;
    private Paint mIconPaint;
    private float[] mDdata = {170, 180, 180, 170, 180};
    private int mScoreSize = DensityUtil.dip2px(getContext(), 20);
    private int mTitleSize = DensityUtil.dip2px(getContext(), 12);
    private int mRradarMargin = DensityUtil.dip2px(getContext(), 15);
    //各维度标题
    private String[] mTitles = {"履约能力", "信用历史", "人脉关系", "行为偏好", "身份特质"};
    private int[] icons = {
            R.mipmap.ic_performance,
            R.mipmap.ic_history,
            R.mipmap.ic_contacts,
            R.mipmap.ic_predilection,
            R.mipmap.ic_identity
    };

    public CreditScoreView(Context context) {
        this(context, null);
    }

    public CreditScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CreditScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
        mMainPaint.setStrokeWidth(0.3f);
        mMainPaint.setColor(Color.WHITE);
        mMainPaint.setStyle(Paint.Style.STROKE);

        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setAlpha(120);

        mScorePaint = new Paint();
        mScorePaint.setAntiAlias(true);
        mScorePaint.setColor(Color.WHITE);
        mScorePaint.setTextSize(mScoreSize);
        mScorePaint.setStyle(Paint.Style.FILL);


        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setColor(Color.WHITE);
        mTitlePaint.setTextSize(mTitleSize);
        mTitlePaint.setStyle(Paint.Style.FILL);

        mIconPaint = new Paint();
        mIconPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth / 2, mHeight / 2) * 0.5f;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
        drawLine(canvas);
        drawRegion(canvas);
        drawScore(canvas);
        drawTitle(canvas);
        drawBitmap(canvas);
    }


    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        for (int i = 1; i < (mDataCount + 1); i++) {

            if (i == 1) {
                float x = (float) (mCenterX + mRadius * Math.sin(mRadian));
                float y = (float) (mCenterY - mRadius * Math.cos(mRadian));
                path.moveTo(x, y);
            } else {
                float x = (float) (mCenterX + mRadius * Math.sin(mRadian * i));
                float y = (float) (mCenterY - mRadius * Math.cos(mRadian * i));
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, mMainPaint);

    }

    private void drawLine(Canvas canvas) {
        Path path = new Path();
        for (int i = 1; i < (mDataCount + 1); i++) {
            float x = (float) (mCenterX + mRadius * Math.sin(mRadian * i));
            float y = (float) (mCenterY - mRadius * Math.cos(mRadian * i));
            path.reset();
            path.moveTo(mCenterX, mCenterY);
            path.lineTo(x, y);
            path.close();
            canvas.drawPath(path, mMainPaint);
        }
    }


    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        for (int i = 1; i < (mDataCount + 1); i++) {
            float maxValue = 190;
            float percent = mDdata[i - 1] / maxValue;
            if (i == 1) {
                float x = (float) (mCenterX + mRadius * Math.sin(mRadian) * percent);
                float y = (float) (mCenterY - mRadius * Math.cos(mRadian) * percent);
                path.moveTo(x, y);
            } else {
                float x = (float) (mCenterX + mRadius * Math.sin(mRadian * i) * percent);
                float y = (float) (mCenterY - mRadius * Math.cos(mRadian * i) * percent);
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, mValuePaint);
    }

    private void drawScore(Canvas canvas) {
        float score = 0;
        for (float v : mDdata) {
            score += v;
        }
        canvas.drawText(String.valueOf(score), mCenterX - mScoreSize, mCenterY + mScoreSize / 2, mScorePaint);
    }

    private void drawTitle(Canvas canvas) {
        mValuePaint.setTextSize(DensityUtil.dip2px(getContext(), 14));
        for (int i = 1; i < (mDataCount + 1); i++) {
            float titleWidth = mTitlePaint.measureText(mTitles[i - 1]);
            float x = (float) (mCenterX + mRadius * Math.sin(mRadian * i));
            float y = (float) (mCenterY - mRadius * Math.cos(mRadian * i));
            if (i == 1) {
                x += mRradarMargin;
                y -= mRradarMargin;

            } else if (i == 2) {
                x += mRradarMargin;
                y += mRradarMargin;

            } else if (i == 3) {
                x -= mRradarMargin;
                y += mRradarMargin;
                x -= titleWidth;

            } else if (i == 4) {
                x -= mRradarMargin;
                y -= mRradarMargin;
                x -= titleWidth;

            } else if (i == 5) {
                x -= titleWidth / 2;
                y -= mRradarMargin;
            }
            canvas.drawText(mTitles[i - 1], x, y, mTitlePaint);
        }
    }

    private void drawBitmap(Canvas canvas) {
        for (int i = 1; i < (mDataCount + 1); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), icons[i - 1]);
            int iconHeight = bitmap.getHeight();
            float titleWidth = mValuePaint.measureText(mTitles[i - 1]);
            float titleHeight = getTextHeight(mTitlePaint);
            float x = (float) (mCenterX + mRadius * Math.sin(mRadian * i));
            float y = (float) (mCenterY - mRadius * Math.cos(mRadian * i));
            if (i == 1) {
                x = x + mRradarMargin;
                y = y - mRradarMargin - iconHeight - titleHeight;

            } else if (i == 2) {
                x += mRradarMargin;
                y = y + mRradarMargin + titleHeight / 2;

            } else if (i == 3) {
                x =  x - mRradarMargin - titleWidth / 5 * 4  ;
                y += mRradarMargin + titleHeight / 2;

            } else if (i == 4) {
                x = x -  mRradarMargin - titleWidth ;
                y =  y - mRradarMargin - titleHeight  - iconHeight;
            } else if (i == 5) {
                x = x -  titleWidth / 2;
                y = y - mRradarMargin - iconHeight - titleHeight ;
            }
            canvas.drawBitmap(bitmap, x, y, mIconPaint);
        }
    }

    private int getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) (fontMetrics.descent - fontMetrics.ascent);
    }
}
