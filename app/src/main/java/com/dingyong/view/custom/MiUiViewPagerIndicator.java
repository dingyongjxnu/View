package com.dingyong.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingyong.view.R;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 * ViewPagerIndicator
 */
public class MiUiViewPagerIndicator extends LinearLayout {
    private static final String TAG = MiUiViewPagerIndicator.class.getSimpleName();

    private Paint mPaint;
    private Path mPath;
    /**
     * 三角形的宽度
     */
    private int mTriangleWidth;
    /**
     * 三角形的高度
     */
    private int mTriangleHeight;
    private static final float RADIO_TRIANGEL = 1.0f / 6;
    private static final int COUNT_DEFAULT_TAB = 4;
    private int mTabVisibleCount = COUNT_DEFAULT_TAB;
    /**
     * 标题正常时的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    private static final int COLOR_TEXT_HIGHLIGHT_COLOR = 0xFFFFFFFF;
    public ViewPager mViewPager;
    private final int DIMENSION_TRIANGEL_WIDTH = (int) (getScreenWidth() / 3 * RADIO_TRIANGEL);
    /**
     * 初始时，三角形指示器的偏移量
     */
    private int mInitTranslationX;
    /**
     * 手指滑动时的偏移量
     */
    private float mTranslationX;
    private PageChangeListener mPageChangeListener;

    public void setPageChangeListener(PageChangeListener pageChangeListener) {
        mPageChangeListener = pageChangeListener;
    }
    public MiUiViewPagerIndicator(Context context) {
        this(context, null);
    }
    public MiUiViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MiUiViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);


    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MiUiViewPagerIndicator);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.MiUiViewPagerIndicator_item_count:
                    mTabVisibleCount = array.getInt(attr, COUNT_DEFAULT_TAB);
                    break;
            }
        }

        if (mTabVisibleCount <= 0) {
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        }
        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onFinishInflate() {
        Log.d(TAG, "onFinishInflate");
        int childCount = getChildCount();
        if (childCount <= 0) return;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.weight = 0;
            params.width = getScreenWidth() / mTabVisibleCount;
            view.setLayoutParams(params);
        }
        setItemClick();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        intTriangle(w);
    }

    private void intTriangle(int w){
        mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGEL);
        mTriangleWidth = Math.min(DIMENSION_TRIANGEL_WIDTH, mTriangleWidth);
        mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mTriangleWidth / 2;
        mPath = new Path();
        mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, - mTriangleHeight);
        mPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    private void setItemClick() {
        int childCount = getChildCount();
        if (childCount <= 0) return;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            final int j = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                    highLightTextView(j);
                }
            });
        }

    }

    public void setViewPager(ViewPager viewPager, int pos) {
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (mPageChangeListener != null){
                    mPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                highLightTextView(position);
                if (mPageChangeListener != null){
                    mPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPageChangeListener != null){
                    mPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }

    private void scroll(int position, float offset) {
        mTranslationX = getWidth() / mTabVisibleCount * (position + offset);
        int tabWidth = getScreenWidth() / mTabVisibleCount;
        if (offset > 0 && position >= mTabVisibleCount - 2 && getChildCount() > mTabVisibleCount) {
            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth + (int) (tabWidth * offset), 0);
            } else {
                this.scrollTo(position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        invalidate();
    }

    protected void highLightTextView(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                if (i == position) {
                    ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT_COLOR);
                } else {
                    ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
                }
            }
        }
    }

    protected void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 获得屏幕的宽度
     *
     * @return 屏幕的宽度
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public void setTabItemTitles(List<String> list) {
        if (list != null && list.size() > 0) {
            this.removeAllViews();
            for (String s : list) {
                addView(generateTextView(s));
            }
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
                if (mTabVisibleCount > getChildCount()) {
                    mTabVisibleCount = getChildCount();
                }
                lp.width = getScreenWidth() / mTabVisibleCount;
                lp.height = LayoutParams.MATCH_PARENT;
                view.setLayoutParams(lp);
            }
            setItemClick();
        }
    }

    private TextView generateTextView(String title) {
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        return tv;
    }



    public interface PageChangeListener {
         void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
         void onPageSelected(int position);
         void onPageScrollStateChanged(int state);
    }
}
