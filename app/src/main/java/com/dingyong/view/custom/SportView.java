package com.dingyong.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.dingyong.view.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/6.
 * SportView
 */
public class SportView extends View {
    private Paint mBackGroundPaint;
    private int mBgHeight;
    private int mBgWidth;
    private int mBgRadius;
    private int mDrawBackGroundColor = Color.WHITE;
    private int mArcLineColor = getResources().getColor(R.color.color_ccc);
    private int mArcSmallColor = getResources().getColor(R.color.color_63B8FF);
    private int mTextColor = getResources().getColor(R.color.color_63B8FF);
    private int mTextOtherColor = getResources().getColor(R.color.color_ccc);
    private float mCurrentAngle = 60;
    private Paint mArcPaint;
    private Paint mTextPaint;
    private Paint mDottedLinePaint;

    private long mStepNumber = 1250;
    private int mRankNumNumber = 11;
    private long mAverageFriendsStepNumber = 2530;
    private long mAverageStepNumber = 3530;

    private int mCenterX;
    private int mCenterY;
    private float mArcRadius;

    public SportView(Context context) {
        this(context, null);
    }

    public SportView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SportView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBackGroundPaint = new Paint();
        mBackGroundPaint.setAntiAlias(true);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);

        mDottedLinePaint = new Paint();
        mDottedLinePaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
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
            height = heightSize * 3 / 4;
        }
        mBgHeight = height;
        mBgWidth = width;
        mCenterX = mBgWidth / 2;
        mCenterY = mBgWidth / 12 * 5;
        mArcRadius = mBgWidth / 4;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawArc(canvas);
        drawText(canvas);
        drawDottedLine(canvas);
    }

    /**
     * 绘制一个左上角和又上角为圆角
     * 左下角,右下角为直角的背景色
     *
     * @param canvas canvas
     */
    private void drawBackground(Canvas canvas) {
        canvas.save();
        mBgRadius = mBgWidth / 20;
        Path path = new Path();
        path.moveTo(0, mBgHeight);
        path.moveTo(0, mBgRadius);
        path.quadTo(0, 0, mBgRadius, 0);
        path.lineTo(mBgWidth - mBgRadius, 0);
        path.quadTo(mBgWidth, 0, mBgWidth, mBgRadius);
        path.lineTo(mBgWidth, mBgHeight);
        path.lineTo(0, mBgHeight);
        mBackGroundPaint.setColor(mDrawBackGroundColor);
        canvas.drawPath(path, mBackGroundPaint);
        canvas.restore();
    }

    private void drawArc(Canvas canvas) {
        canvas.save();
        mArcPaint.setStrokeWidth(mBgWidth / 20);//设置画笔宽度
        mArcPaint.setStyle(Paint.Style.STROKE);//设置为空心
        mArcPaint.setDither(true);//设置防抖
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);// //连接处为圆弧
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);//画笔的触角为圆角
        mArcPaint.setColor(mArcLineColor);//设置颜色
        RectF rectF = new RectF(mCenterX - mArcRadius, mCenterY - mArcRadius, mCenterX + mArcRadius, mCenterY + mArcRadius);
        float startAngle = 120f;
        float angle = 300f;
        canvas.drawArc(rectF, startAngle, angle, false, mArcPaint);

        mArcPaint.setColor(mArcSmallColor);
        canvas.drawArc(rectF, startAngle, mCurrentAngle, false, mArcPaint);
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        canvas.save();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(DensityUtil.dip2px(getContext(), 35));
        String step = String.valueOf(mStepNumber);
        Rect rectStep = new Rect();
        mTextPaint.getTextBounds(step, 0, step.length(), rectStep);
        canvas.drawText(step, mCenterX - rectStep.width() / 2, mCenterY + rectStep.height() / 2, mTextPaint);

        mTextPaint.setTextSize(DensityUtil.dip2px(getContext(), 20));
        mTextPaint.setColor(mTextColor);
        String rankText = String.valueOf(mRankNumNumber);
        Rect rankRect = new Rect();
        mTextPaint.getTextBounds(rankText, 0, rankText.length(), rankRect);
        canvas.drawText(rankText, mCenterX - rankRect.width() / 2, mCenterY + mArcRadius + rankRect.height() / 2, mTextPaint);

        mTextPaint.setTextSize(DensityUtil.dip2px(getContext(), 12));
        mTextPaint.setColor(mTextOtherColor);
        String textDi = "第";
        String textMing = "名";
        Rect diRect = new Rect();

        mTextPaint.getTextBounds(textDi, 0, textDi.length(), diRect);
        canvas.drawText(textDi, mCenterX - rankRect.width() / 2 - DensityUtil.dip2px(getContext(), 5) - diRect.width(), mCenterY + mArcRadius + rankRect.height() / 2, mTextPaint);

        canvas.drawText(textMing, mCenterX + rankRect.width() / 2 + DensityUtil.dip2px(getContext(), 8), mCenterY + mArcRadius + rankRect.height() / 2, mTextPaint);

        String textTime = "截止" + getCurrentHoseTime() + "已走";
        Rect timeRect = new Rect();
        mTextPaint.getTextBounds(textTime, 0, textTime.length(), timeRect);
        canvas.drawText(textTime, mCenterX - timeRect.width() / 2, mCenterY - mArcRadius / 2 + timeRect.height() / 2, mTextPaint);


        String textFriendsAverage = "好友平均" + String.valueOf(mAverageFriendsStepNumber) + "步";
        Rect averageFriendsRect = new Rect();
        mTextPaint.getTextBounds(textFriendsAverage, 0, textFriendsAverage.length(), averageFriendsRect);
        canvas.drawText(textFriendsAverage, mCenterX - averageFriendsRect.width() / 2, mCenterY + mArcRadius / 2 + averageFriendsRect.height() / 2, mTextPaint);


        String sevenDays = "最近七天";
        Rect sevenDaysRect = new Rect();
        mTextPaint.getTextBounds(sevenDays, 0, sevenDays.length(), sevenDaysRect);
        canvas.drawText(sevenDays, DensityUtil.dip2px(getContext(), 20), mCenterY * 2 + sevenDaysRect.height() / 2, mTextPaint);

        String textAverage = "平均" + mAverageStepNumber + "步/天";
        Rect averageRect = new Rect();
        mTextPaint.getTextBounds(textAverage, 0, textAverage.length(), averageRect);
        canvas.drawText(textAverage, mBgWidth - DensityUtil.dip2px(getContext(), 20) - averageRect.width(), mCenterY * 2 + averageRect.height() / 2, mTextPaint);
        canvas.restore();




    }


    /**
     * 绘制中间的虚线
     * @param canvas canvas
     */
    private void drawDottedLine(Canvas canvas) {
        canvas.save();
        mDottedLinePaint.setColor(mArcLineColor);
        mDottedLinePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2f));
        mDottedLinePaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        //mCenterY * 2 向下偏移40dp
        path.moveTo(DensityUtil.dip2px(getContext(), 20), mCenterY * 2 + DensityUtil.dip2px(getContext(), 40));
        path.lineTo(mBgWidth - DensityUtil.dip2px(getContext(), 20), mCenterY * 2 + DensityUtil.dip2px(getContext(), 40));
        //设置虚线
        mDottedLinePaint.setPathEffect(new DashPathEffect(new float[]{ 5, 5}, 1));
        canvas.drawPath(path, mDottedLinePaint);
        canvas.restore();
    }

    /**
     * 得到当前的时间 格式为 HH:mm
     * @return currentTime str
     */
    private String getCurrentHoseTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(System.currentTimeMillis()));
    }
}
