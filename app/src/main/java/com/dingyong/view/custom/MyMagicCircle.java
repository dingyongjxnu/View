package com.dingyong.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.dingyong.view.R;

/**
 * Created by Administrator on 2017/4/19.
 * MyMagicCircle
 */
public class MyMagicCircle extends View {
    private Paint mPaint;
    private int mCenterX;
    private int mCenterY;
    private int mWidth;
    private int mHeight;
    private Path mPath;
    private float mRadius;
    private float mBlackMagic = 0.551915024494f;
    private float c;
    private LeftAndRightPoint mLeftPoint;
    private LeftAndRightPoint mRightPoint;
    private TopAndBottomPoint mTopPoint;
    private TopAndBottomPoint mBottomPoint;
    private float mInterpolatedTime;
    private float mStretchDistance;
    private float mDistance;
    private float maxLength;

    public MyMagicCircle(Context context) {
        this(context, null);
    }

    public MyMagicCircle(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyMagicCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);

        mPath = new Path();
        mLeftPoint = new LeftAndRightPoint();
        mRightPoint = new LeftAndRightPoint();
        mTopPoint = new TopAndBottomPoint();
        mBottomPoint = new TopAndBottomPoint();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mRadius = 50;
        mStretchDistance = mRadius;
        c = mRadius * mBlackMagic;
        mDistance = c * 0.45f;
        maxLength = mWidth - mRadius - mRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        canvas.translate(mRadius, mRadius);


        if (mInterpolatedTime >= 0 && mInterpolatedTime <= 0.2) {
            setFirstChangeState(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.2 && mInterpolatedTime <= 0.5) {
            setSecondChangeState(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.5 && mInterpolatedTime <= 0.8) {
            setThirdChangeState(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.8 && mInterpolatedTime <= 0.9) {
            setFourChangeState(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.9 && mInterpolatedTime <= 1.0) {
            setFiveChangeState(mInterpolatedTime);
        }

/*
        float offset = maxLength*(mInterpolatedTime-0.2f);
        offset = offset>0?offset:0;
        mBottomPoint.adjustAllX(offset);
        mRightPoint.adjustAllX(offset);
        mTopPoint.adjustAllX(offset);
        mLeftPoint.adjustAllX(offset);*/

        mPath.moveTo(mBottomPoint.x, mBottomPoint.y);
        mPath.cubicTo(mBottomPoint.right.x, mBottomPoint.right.y, mRightPoint.bottom.x, mRightPoint.bottom.y, mRightPoint.x, mRightPoint.y);
        mPath.cubicTo(mRightPoint.top.x, mRightPoint.top.y, mTopPoint.right.x, mTopPoint.right.y, mTopPoint.x, mTopPoint.y);
        mPath.cubicTo(mTopPoint.left.x, mTopPoint.left.y, mLeftPoint.top.x, mLeftPoint.top.y, mLeftPoint.x, mLeftPoint.y);
        mPath.cubicTo(mLeftPoint.bottom.x, mLeftPoint.bottom.y, mBottomPoint.left.x, mBottomPoint.left.y, mBottomPoint.x, mBottomPoint.y);


        canvas.drawPath(mPath, mPaint);
    }

    private void initState() {
        mLeftPoint.setX(-mRadius);
        mRightPoint.setX(mRadius);
        mLeftPoint.y = mRightPoint.y = 0;
        mLeftPoint.top.y = mRightPoint.top.y = -c;
        mLeftPoint.bottom.y = mRightPoint.bottom.y = c;

        mTopPoint.setY(-mRadius);
        mBottomPoint.setY(mRadius);
        mTopPoint.x = mBottomPoint.x = 0;
        mTopPoint.left.x = mBottomPoint.left.x = -c;
        mTopPoint.right.x = mBottomPoint.right.x = c;

    }

    private void setFirstChangeState(float time) {//0-0.2
        initState();
        mRightPoint.setX(mRadius + mStretchDistance * time * 5);
    }

    private void setSecondChangeState(float time) {//0.2 - 0.5
        setFirstChangeState(0.2f);
        time = (time - 0.2f) * (10f / 3);
        mBottomPoint.adjustAllX(mStretchDistance / 2 * time);
        mTopPoint.adjustAllX(mStretchDistance / 2 * time);
        mLeftPoint.adjustY(mDistance * time);
        mRightPoint.adjustY(mDistance * time);
    }

    private void setThirdChangeState(float time) {//0.5-0.8
        setSecondChangeState(0.5f);
        time = (time - 0.5f) * (10f / 3);
        mBottomPoint.adjustAllX(mStretchDistance / 2 * time);
        mTopPoint.adjustAllX(mStretchDistance / 2 * time);
        mLeftPoint.adjustY(-mDistance * time);
        mRightPoint.adjustY(-mDistance * time);
    }

    private void setFourChangeState(float time) {//0.8-0.9
        setThirdChangeState(0.8f);
        time = (time - 0.8f) * (10f / 3);
        mLeftPoint.adjustAllX(mStretchDistance / 2 * time);

    }

    private void setFiveChangeState(float time) {//0.9-1.0
        setFourChangeState(0.9f);
        time = (time - 0.9f);
        //initState();

    }

    public void startAnimation() {
        mPath.reset();
        mInterpolatedTime = 0;
        MoveAnimation animation = new MoveAnimation();
        animation.setDuration(10000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        startAnimation(animation);
    }

    class LeftAndRightPoint {
        public float x;
        public float y;
        public PointF top = new PointF();
        public PointF bottom = new PointF();

        public void setX(float x) {
            this.x = x;
            this.top.x = x;
            this.bottom.x = x;
        }

        public void adjustY(float offset) {
            this.top.y = this.top.y - offset;
            this.bottom.y = this.bottom.y + offset;
        }

        public void adjustAllX(float offset) {
            this.x = this.x + offset;
            this.top.x = this.top.x + offset;
            this.bottom.y = this.bottom.y + offset;
        }

    }

    class TopAndBottomPoint {
        public float x;
        public float y;
        public PointF left = new PointF();
        public PointF right = new PointF();

        public void setY(float y) {
            this.y = y;
            this.left.y = y;
            this.right.y = y;
        }

        public void adjustAllX(float offset) {
            this.x = this.x + offset;
            this.left.x = this.left.x + offset;
            this.right.x = this.right.x + offset;
        }
    }

    class MoveAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }
}
