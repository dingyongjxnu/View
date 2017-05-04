package com.dingyong.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dingyong.view.R;

/**
 * Created by Administrator on 2017/4/13.
 * CustomVolumeControlBar
 */
public class CustomVolumeControlBar extends View {
    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private int mCurrentCount;
    private int mCount;
    private Bitmap mImage;
    private int mSplitSize;
    private Paint mPaint;
    private Rect mRect;
    private int xDown;
    private int xUp;

    public CustomVolumeControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumeControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomVolumeControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumeControlBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomVolumeControlBar_firstColor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomVolumeControlBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.CYAN);
                    break;
                case R.styleable.CustomVolumeControlBar_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, DensityUtil.dip2px(context, 15));
                    break;
                case R.styleable.CustomVolumeControlBar_dotCount:
                    mCount = a.getInt(attr, 20);
                    break;
                case R.styleable.CustomVolumeControlBar_splitSize:
                    mSplitSize = a.getDimensionPixelSize(attr, DensityUtil.dip2px(context, 15));
                    break;
                case R.styleable.CustomVolumeControlBar_bg:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;

            }
        }
        a.recycle();
        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setStrokeWidth(mCircleWidth);//设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        //获取圆心的坐标
        int center = getWidth() / 2;
        //获取半径
        int radius = center - mCircleWidth / 2;
        drawOval(canvas, center, radius);

        //获得内圆的半径
        int relRadius = radius - mCircleWidth / 2;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = DensityUtil.dip2px(getContext(), 200);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = DensityUtil.dip2px(getContext(), 200);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDown) {
                    down();
                } else {
                    up();
                }
                break;

        }
        return true;
    }

    private void down() {
        if (mCurrentCount < mCount) {
            mCurrentCount++;
            postInvalidate();
        }

    }

    private void up() {
        if (mCurrentCount > 0) {
            mCurrentCount--;
            postInvalidate();
        }

    }


    private void drawOval(Canvas canvas, int centre, int radius) {
        float itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;
        RectF rectF = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        //设置圆环的颜色
        mPaint.setColor(mFirstColor);
        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(rectF, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
        mPaint.setColor(mSecondColor);
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(rectF, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
    }

}
