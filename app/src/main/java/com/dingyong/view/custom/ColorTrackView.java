package com.dingyong.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.dingyong.view.R;

/**
 * Created by Administrator on 2017/4/17.
 *
 */
public class ColorTrackView extends View {
    private int mTextStartX;

    public enum Direction {
        LEFT, RIGHT;
    }

    private String mText;
    private Paint mPaint;
    private int mTextSize = DensityUtil.dip2px(getContext(), 15);
    private int mTextOriginColor = 0xff000000;
    private int mTextChangeColor = 0xffff0000;
    private int mDirection = DIRECTION_LEFT;
    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_RIGHT = 1;

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    private float mProgress;
    private int mTextWidth;
    private int mRealWidth;
    private Rect mTextBound = new Rect();

    public ColorTrackView(Context context) {
        this(context, null);
    }

    public ColorTrackView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ColorTrackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackView);
        mText = ta.getString(R.styleable.ColorTrackView_text);
        mTextSize = ta.getDimensionPixelSize(R.styleable.ColorTrackView_text_size, DensityUtil.dip2px(context,10));
        mTextOriginColor = ta.getColor(R.styleable.ColorTrackView_text_origin_color, mTextOriginColor);
        mTextChangeColor = ta.getColor(R.styleable.ColorTrackView_text_change_color, mTextChangeColor);
        mProgress = ta.getFloat(R.styleable.ColorTrackView_progress, 0);
        mDirection = ta.getInt(R.styleable.ColorTrackView_direction, mDirection);
        ta.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        measureText();
    }

    private void measureText() {
        mTextWidth = (int) mPaint.measureText(mText);
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mTextStartX = mRealWidth / 2 - mTextWidth / 2;
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = mTextWidth;
        }
        width = mode == MeasureSpec.AT_MOST ? Math.min(width, size) : width;
        width = width + getPaddingLeft() + getPaddingRight();
        return width;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = mTextBound.height();
        }
        height = mode == MeasureSpec.AT_MOST ? Math.min(height, size) : height;
        height = height + getPaddingBottom() + getPaddingTop();
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDirection == DIRECTION_LEFT) {
            drawChangeLeft(canvas);
            drawOriginLeft(canvas);
        }else {
            drawOriginRight(canvas);
            drawChangeRight(canvas);
        }
    }

    private void drawChangeRight(Canvas canvas) {
        drawText(canvas, mTextChangeColor, (int) (mTextStartX +(1-mProgress)*mTextWidth), mTextStartX+mTextWidth );
    }

    private void drawOriginRight(Canvas canvas) {
        drawText(canvas, mTextOriginColor, mTextStartX, (int) (mTextStartX +(1-mProgress)*mTextWidth) );
    }

    private void drawChangeLeft(Canvas canvas) {
        drawText(canvas, mTextChangeColor, mTextStartX, (int) (mTextStartX + mProgress * mTextWidth));
    }

    private void drawOriginLeft(Canvas canvas) {
        drawText(canvas, mTextOriginColor, (int) (mTextStartX + mProgress * mTextWidth), mTextStartX +mTextWidth );
    }
    private void drawText(Canvas canvas, int color, int startX, int endX) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, mTextBound.height());
        canvas.drawText(mText, mTextStartX, mTextBound.height(), mPaint);
        canvas.restore();
    }
}
