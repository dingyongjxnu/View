package com.dingyong.view.custom;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.dingyong.view.R;

/**
 * Created by Administrator on 2017/4/22.
 * MyRoundIndicatorView
 */
public class MyRoundIndicatorView extends View {
    private static final String TAG = MyRoundIndicatorView.class.getSimpleName();

    private static final int DEFAULT_MAX_NUM = 500;
    private static final int DEFAULT_START_ANGLE = 150;
    private static final int DEFAULT_SWEEP_ANGLE = 240;
    private int mMaxNum = DEFAULT_MAX_NUM;
    private int mStartAngle = DEFAULT_START_ANGLE;
    private int mSweepAngle = DEFAULT_SWEEP_ANGLE;
    private int mSweepInWidth;//内圆的宽度
    private int mSweepOutWidth;//外圆的宽度
    private int mRadius;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Paint mTextPaint;

    private String[] mTitleArr = {"较差", "中等", "良好", "优秀", "极好"};


    private int mCurrentNum = 0;
    private Paint mPaint2;
    private Paint mPaint3;
    private Paint mPaint4;

    public MyRoundIndicatorView(Context context) {
        this(context, null);
    }

    public MyRoundIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyRoundIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0xFFFF6347);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyRoundIndicatorView);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.MyRoundIndicatorView_maxNum:
                    mMaxNum = array.getInt(attr, DEFAULT_MAX_NUM);
                    break;
                case R.styleable.MyRoundIndicatorView_startAngle:
                    mStartAngle = array.getInt(attr, DEFAULT_START_ANGLE);
                    break;
                case R.styleable.MyRoundIndicatorView_sweepAngle:
                    mSweepAngle = array.getInt(attr, DEFAULT_SWEEP_ANGLE);
                    break;
            }

        }
        array.recycle();
    }

    private void init() {
        mSweepInWidth = DensityUtil.dip2px(getContext(), 8);
        mSweepOutWidth = DensityUtil.dip2px(getContext(), 3);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xffffffff);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(0xffffffff);


        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint4 = new Paint(Paint.ANTI_ALIAS_FLAG);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = DensityUtil.dip2px(getContext(), 300);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = DensityUtil.dip2px(getContext(), 400);
        }

        mWidth = mWidth + getPaddingRight() + getPaddingLeft();
        mHeight = mHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRound(canvas);
        drawScale(canvas);
        drawIndicator(canvas);
        drawCenterText(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h) / 3;
        mWidth = w;
        mHeight = h;

    }

    private void drawRound(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        //画内圆
        mPaint.setStrokeWidth(mSweepInWidth);
        mPaint.setAlpha(40);
        RectF rectFIn = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        canvas.drawArc(rectFIn, mStartAngle, mSweepAngle, false, mPaint);

        //画外园
        mPaint.setStrokeWidth(mSweepOutWidth);
        mPaint.setAlpha(100);
        int w = DensityUtil.dip2px(getContext(), 10);
        RectF rectFOut = new RectF(-mRadius - w, -mRadius - w, mRadius + w, mRadius + w);
        canvas.drawArc(rectFOut, mStartAngle, mSweepAngle, false, mPaint);
        canvas.restore();
    }


    private void drawScale(Canvas canvas) {
        canvas.save();
        mPaint.setAlpha(100);
        float angle = mSweepAngle / 30;
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.rotate(-270 + mStartAngle);
        for (int i = 0; i <= 30; i++) {
            if (i % 6 == 0) {
                mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
                mPaint.setAlpha(100);
                canvas.drawLine(0, -mRadius - mSweepInWidth / 2, 0, -mRadius + mSweepInWidth / 2, mPaint);
                String text = String.valueOf(mMaxNum * i / 30);
                drawText(canvas, text, mTextPaint);
            } else {
                mPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
                mPaint.setAlpha(50);
                canvas.drawLine(0, -mRadius - mSweepInWidth / 2, 0, -mRadius + mSweepInWidth / 2, mPaint);
            }

            canvas.rotate(angle);

            if (i == 2 || i == 8 || i == 14 || i == 20 || i == 26) {
                String text = mTitleArr[(i - 2) / 6];
                mTextPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
                mTextPaint.setAlpha(50);
                drawText(canvas, text, mTextPaint);
            }
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas, String text, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(DensityUtil.dip2px(getContext(), 12));
        float widthText = paint.measureText(text);
        canvas.drawText(text, -widthText / 2, -mRadius + DensityUtil.dip2px(getContext(), 20), paint);

    }

    private int[] indicatorColor = {0xffffffff, 0x00ffffff, 0x99ffffff, 0xffffffff};

    private void drawIndicator(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint2.setStyle(Paint.Style.STROKE);
        int sweep;
        if (mCurrentNum < mMaxNum) {
            sweep = (int) ((float) mCurrentNum / (float) mMaxNum * mSweepAngle);
        } else {
            sweep = mSweepAngle;
        }
        mPaint2.setStrokeWidth(mSweepOutWidth);
        Shader shader = new SweepGradient(0, 0, indicatorColor, null);
        mPaint2.setShader(shader);
        int width = DensityUtil.dip2px(getContext(), 10);
        RectF rectF = new RectF(-mRadius - width, -mRadius - width, mRadius + width, mRadius + width);
        canvas.drawArc(rectF, mStartAngle, mSweepAngle, false, mPaint2);

        float x = (float) ((mRadius + DensityUtil.dip2px(getContext(), 10)) * Math.cos(Math.toRadians(mStartAngle + sweep)));
        float y = (float) ((mRadius + DensityUtil.dip2px(getContext(), 10)) * Math.sin(Math.toRadians(mStartAngle + sweep)));
        mPaint3.setStyle(Paint.Style.FILL);
        mPaint3.setColor(0xffffffff);
        mPaint3.setMaskFilter(new BlurMaskFilter(DensityUtil.dip2px(getContext(), 3), BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(x, y, DensityUtil.dip2px(getContext(), 3), mPaint3);
        canvas.restore();
    }

    private void drawCenterText(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint4.setStyle(Paint.Style.FILL);
        mPaint4.setTextSize(DensityUtil.dip2px(getContext(), 50));
        mPaint4.setColor(Color.WHITE);
        String currentNum = String.valueOf(mCurrentNum);
        float currentWidth = mPaint4.measureText(currentNum);
        canvas.drawText(currentNum, -currentWidth / 2, 0, mPaint4);

        String content = "信用";
        if (mCurrentNum < mMaxNum / 5) {
            content += mTitleArr[0];
        } else if (mCurrentNum < mMaxNum * 2 / 5) {
            content += mTitleArr[1];
        } else if (mCurrentNum < mMaxNum * 3 / 5) {
            content += mTitleArr[2];
        } else if (mCurrentNum < mMaxNum * 4 / 5) {
            content += mTitleArr[3];
        } else {
            content += mTitleArr[4];
        }
        mPaint4.setTextSize(DensityUtil.dip2px(getContext(), 30));
        Rect r = new Rect();
        mPaint4.getTextBounds(content, 0, content.length(), r);

        canvas.drawText(content, -r.width() / 2, r.height() + DensityUtil.dip2px(getContext(), 20), mPaint4);
        canvas.restore();
        canvas.restore();
    }


    public int getCurrentNum() {
        return mCurrentNum;
    }

    public void setCurrentNum(int currentNum) {
        mCurrentNum = currentNum;
        invalidate();
    }

    public void setCurrentNumAnim( int currentNum) {
        if (currentNum >= mMaxNum) {
            currentNum = mMaxNum;
        }
        float duration = (float) Math.abs(currentNum - mCurrentNum) / mMaxNum * 1500 + 500; //根据进度差计算动画时间
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "mCurrentNum", currentNum);
        animator.setDuration((long) Math.min(duration, 2000));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (int) animation.getAnimatedValue();
                int color = calculateColor(value);
                setBackgroundColor(color);
                mCurrentNum = value;


            }
        });
        animator.start();

    }

    private int calculateColor(int value) {
        ArgbEvaluator evaluator = new ArgbEvaluator();
        float fraction = 0;
        int color = 0;
        if (value <= mMaxNum / 2) {
            fraction = (float) value / (mMaxNum / 2);
            color = (int) evaluator.evaluate(fraction, 0xFFFF6347, 0xFFFF8C00); //由红到橙
        } else {
            fraction = ((float) value - mMaxNum / 2) / (mMaxNum / 2);
            color = (int) evaluator.evaluate(fraction, 0xFFFF8C00, 0xFF00CED1); //由橙到蓝
        }
        return color;
    }
}
