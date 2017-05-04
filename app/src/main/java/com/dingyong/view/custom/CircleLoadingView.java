package com.dingyong.view.custom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.dingyong.view.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Administrator on 2017/4/14.
 * CircleLoadingView
 */
public class CircleLoadingView extends View {

    private int radius;  // 小球的半径
    private int[] colors;  // 颜色数组
    private int maxOffset; // 小球偏移的最大值
    private int minOffset; // 小球偏移的最小值
    private int duration; // 动画执行的时间

    private Paint mPaint;
    private int mCanvasAngle; // 画笔目前的角度
    private float mOffset; // 小球目前的偏移
    private int mCenterX;
    private int mCenterY;
    private AnimatorSet mAnimationSet;
    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.CircleLoadingView_radius:
                    radius = array.getDimensionPixelSize(attr,DensityUtil.dip2px(context,10));
                    break;
                case R.styleable.CircleLoadingView_colors:
                    colors = array.getResources().getIntArray(array.getResourceId(attr, R.array.colors));
                    break;
                case R.styleable.CircleLoadingView_maxOffset:
                    maxOffset =  array.getDimensionPixelSize(attr,DensityUtil.dip2px(context,24));
                    break;
                case R.styleable.CircleLoadingView_minOffset:
                    minOffset = array.getDimensionPixelSize(attr,DensityUtil.dip2px(context,12));
                    break;
                case R.styleable.CircleLoadingView_duration:
                    duration = array.getInt(attr,2000);
                    break;
            }
        }
        array.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        startAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;
        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            width = DensityUtil.dip2px(getContext(),100)  + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else{
            height = DensityUtil.dip2px(getContext(),100) + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width,height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0 ;i<colors.length ;i++){
            mPaint.setColor(colors[i]);
            canvas.rotate(mCanvasAngle+i*(360/colors.length),mCenterX,mCenterY);
            // 根据目前的偏移来决定小球的位置
            canvas.drawCircle(mCenterX+mOffset,mCenterX+mOffset,radius,mPaint);
            // 还原画布的旋转
            canvas.rotate(-(mCanvasAngle+i*(360/colors.length)),mCenterX,mCenterY);
        }
    }

    private void startAnimation(){
        mAnimationSet = new AnimatorSet();
        Collection<Animator> animList = new ArrayList<>();
        ValueAnimator canvasAnim = ValueAnimator.ofInt(0,(colors.length+1)*180/colors.length,(colors.length+1)*360/colors.length);
        canvasAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCanvasAngle = (int) animation.getAnimatedValue();
            }
        });
        //canvasAnim.setRepeatCount(1000);
        canvasAnim.setRepeatMode(ValueAnimator.RESTART);

        animList.add(canvasAnim);

        ValueAnimator circleAnim = ValueAnimator.ofFloat(maxOffset, minOffset, maxOffset);
        circleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        // 如上
       // circleAnim.setRepeatCount(1000);
        circleAnim.setRepeatMode(ValueAnimator.RESTART);
        animList.add(circleAnim);

        // 设置动画集合的属性
        mAnimationSet.setDuration(duration);
        mAnimationSet.playTogether(animList);
        mAnimationSet.setInterpolator(new LinearInterpolator());
        // 开始动画
        mAnimationSet.start();

    }
}
