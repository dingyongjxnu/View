package com.dingyong.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.dingyong.view.R;

/**
 * Created by dy on 2017/5/4.
 * SportView
 */
public class ScoreView extends View {
    private int mTextSize = DensityUtil.dip2px(getContext(), 30);
    private int mTextColor = getResources().getColor(R.color.colorAccent);

    private int mOutCircleColor = getResources().getColor(R.color.color_01_line);
    private int mInCircleColor = getResources().getColor(R.color.color_01_point_in);

    private int mOutCircleWidth = DensityUtil.dip2px(getContext(), 0.5f);
    private int mInCircleWidth = DensityUtil.dip2px(getContext(), 3f);

    private float mCurrentAngle = 0f;
    private int mCurrentPercent = 0;
    private int mTargetPercent = 100;
    private Paint mTextPaint;
    private Paint mInCirclePaint;



    private Paint mOutCirclePaint;
    private int mCenterX;
    private int mCenterY;
    private float mRadius;
    private int mOffset = DensityUtil.dip2px(getContext(), 10);

    public ScoreView(Context context) {
        this(context, null);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScoreView);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.ScoreView_sportView_in_circle_color:
                    mInCircleColor = array.getColor(attr, getResources().getColor(R.color.color_02_point));
                    break;
                case R.styleable.ScoreView_sportView_out_circle_color:
                    mOutCircleColor = array.getColor(attr, getResources().getColor(R.color.color_01_line));
                    break;
                case R.styleable.ScoreView_sportView_text_color:
                    mTextColor = array.getColor(attr, getResources().getColor(R.color.color_text));
                    break;
                case R.styleable.ScoreView_sportView_text_size:
                    mTextSize = array.getDimensionPixelSize(attr, DensityUtil.dip2px(getContext(), 14));
                    break;
            }
        }
        array.recycle();

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);

        mTextPaint.setStyle(Paint.Style.FILL);

        mInCirclePaint = new Paint();
        mInCirclePaint.setAntiAlias(true);

        mInCirclePaint.setStyle(Paint.Style.STROKE);


        mOutCirclePaint = new Paint();
        mOutCirclePaint.setAntiAlias(true);

        mOutCirclePaint.setStyle(Paint.Style.STROKE);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize / 2;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize / 2;
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float width = w - getPaddingLeft() - getPaddingRight();
        float height = h - getPaddingBottom() - getPaddingTop();

        mCenterX = (int) (width / 2);
        mCenterY = (int) (height / 2);
        mRadius = Math.min(width, height) / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        drawOutCircle(canvas);
        drawText(canvas);
        drawInCircle(canvas);
        canvas.restore();
    }

    private void drawOutCircle(Canvas canvas) {
        mOutCirclePaint.setStrokeWidth(mOutCircleWidth);
        mOutCirclePaint.setColor(mOutCircleColor);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mOutCirclePaint);

    }

    private void drawText(Canvas canvas) {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        String text = String.valueOf(mCurrentPercent) + "分";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);

        canvas.drawText(text, mCenterX - rect.width() / 2, mCenterY + rect.height() / 2, mTextPaint);

    }

    private void drawInCircle(Canvas canvas) {
        mInCirclePaint.setStrokeWidth(mInCircleWidth);
        mInCirclePaint.setColor(mInCircleColor);
        RectF rectF = new RectF();
        rectF.left = mCenterX - mRadius + mOffset;
        rectF.top = mCenterY - mRadius + mOffset;
        rectF.right = mCenterX + mRadius - mOffset;
        rectF.bottom = mCenterY + mRadius - mOffset;
        float startSweepValue = -90f;
        canvas.drawArc(rectF, startSweepValue,mCurrentAngle,false,mInCirclePaint);
        if (mCurrentPercent < mTargetPercent){
            mCurrentAngle = mCurrentAngle + 3.6f;
            mCurrentPercent = mCurrentPercent + 1;
            postInvalidateDelayed(100);
        }
    }



    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setOutCircleColor(int outCircleColor) {
        mOutCircleColor = outCircleColor;
    }

    public void setInCircleColor(int inCircleColor) {
        mInCircleColor = inCircleColor;
    }

    public void setOutCircleWidth(int outCircleWidth) {
        mOutCircleWidth = outCircleWidth;
    }

    public void setInCircleWidth(int inCircleWidth) {
        mInCircleWidth = inCircleWidth;
    }


    public void setCurrentPercent(int currentPercent) {
        mCurrentPercent = currentPercent;
    }

    public void setTargetPercent(int targetPercent) {
        mTargetPercent = targetPercent;
    }

    public void setNumber(int number){
        mTargetPercent = number;
        mCurrentPercent = 0;
        mCurrentAngle = 0;
        postInvalidate();
    }
}
