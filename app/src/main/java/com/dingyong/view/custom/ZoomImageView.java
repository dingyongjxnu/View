package com.dingyong.view.custom;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import static android.view.GestureDetector.*;

/**
 * Created by dy on 2017/4/15.
 * MyZoomImageView
 */
public class ZoomImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener, ViewTreeObserver.OnGlobalLayoutListener {
    private final String TAG = ZoomImageView.class.getSimpleName();
    private ScaleGestureDetector mScaleGestureDetector;
    public static final float SCALE_MAX = 10.0f;
    private static final float SCALE_MID = 2.0f;
    private float initScale = 1.0f;
    /**
     * 用于存放矩阵的9个值
     */
    private final float[] matrixValues = new float[9];
    private Matrix mScaleMatrix = new Matrix();
    private boolean isCanDrag;
    private int lastPointerCount;
    private float mLastX;
    private float mLastY;
    private int mTouchSlop;
    private boolean isAutoScale;
    private boolean isCheckTopAndBottom = true;
    private boolean isCheckLeftAndRight = true;
    private GestureDetector mGestureDetector;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setScaleType(ScaleType.MATRIX);
        init(context);
    }

    private void init(Context context) {
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d(TAG, "onDoubleTap");

                float x = e.getX();
                float y = e.getY();
                if (getScale() < SCALE_MID) {
                    postDelayed(new AutoScaleRunnable(SCALE_MID, x, y), 16);
                    isAutoScale = true;
                } else if (getScale() >= SCALE_MID && getScale() < SCALE_MAX) {
                    postDelayed(new AutoScaleRunnable(SCALE_MAX, x, y), 16);
                    isAutoScale = true;
                } else {
                    postDelayed(new AutoScaleRunnable(initScale, x, y), 16);
                    isAutoScale = true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d(TAG, "onScale");
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        if (getDrawable() == null) {
            return true;
        }
        //缩放的范围控制
        if ((scale < SCALE_MAX && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)) {
            //最大值和最小值判断
            if (scale * scaleFactor < initScale) {//缩小
                scaleFactor = scaleFactor / scale;
            }
            if (scale * scaleFactor > SCALE_MAX) {//放大
                scaleFactor = SCALE_MAX / scale;
            }
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkCenter();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    private void checkCenter() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }
        } else {
            deltaX = width * 0.5f - rectF.right + 0.5f * rectF.width();
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        } else {
            deltaY = height * 0.5f - rectF.bottom + 0.5f * rectF.height();
        }
        Log.d(TAG, "deltaX:" + deltaX + ",deltaY;" + deltaY);
        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleBegin");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleEnd");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) return true;
        mScaleGestureDetector.onTouchEvent(event);
        float x = 0, y = 0;
        //得到触摸点的个数
        final int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX();
            y += event.getY();
        }
        x = x / pointerCount;
        y = y / pointerCount;
        if (pointerCount != lastPointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        lastPointerCount = pointerCount;
        RectF rectF = getMatrixRectF();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy);
                }
                if (isCanDrag) {
                    if (getDrawable() != null) {
                        if (rectF.left == 0 && dx > 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        if (rectF.right == getWidth() && dx < 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        if (rectF.width() < getWidth()) {
                            dx = 0;
                            isCheckLeftAndRight = false;
                        }
                        if (rectF.height() < getHeight()) {
                            dy = 0;
                            isCheckTopAndBottom = false;
                            mScaleMatrix.postTranslate(dx, dy);
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkMatrixBounds();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                lastPointerCount = 0;
                break;
        }
        return true;
    }

    private boolean isCanDrag(float x, float y) {
        return Math.sqrt((x * x) + (y * y)) >= mTouchSlop;
    }

    private boolean once = true;

    @Override
    public void onGlobalLayout() {
        if (once) {
            Log.d(TAG, "onGlobalLayout");
            if (getDrawable() != null) {
                Drawable d = getDrawable();

                int width = getWidth();//屏幕的宽
                int height = getHeight();//屏幕的高
                int dw = d.getIntrinsicWidth();//图片的宽
                int dh = d.getIntrinsicHeight();//图片的高
                float scale = 1.0f;
                //图片的宽大于屏幕的宽.图片的高度小于等于屏幕的高度
                if (dw > width && dh < height) {
                    scale = width * 1.0f / dw;
                }
                if (dw <= width && dh > height) {
                    scale = height * 1.0f / dh;
                }
                if (dw > width && dh > height) {
                    scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
                }
                initScale = scale;
                //将图片移动到屏幕的中心位置
                mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
                mScaleMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
                setImageMatrix(mScaleMatrix);
                once = false;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    private final float getScale() {
        mScaleMatrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    //根据当前图片的Matrix获得图片的范围
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rect = new RectF();
        if (getDrawable() != null) {
            Drawable d = getDrawable();
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    private void checkMatrixBounds() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0, deltaY = 0;
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        if (rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        if (rectF.bottom < viewHeight && isCheckTopAndBottom) {
            deltaY = viewHeight - rectF.bottom;
        }
        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < viewWidth && isCheckLeftAndRight) {
            deltaX = viewWidth - rectF.right;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;
        private float x;
        private float y;

        public AutoScaleRunnable(float x, float y, float targetScale) {
            this.x = x;
            this.y = y;
            this.mTargetScale = targetScale;
            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }
        }

        @Override
        public void run() {
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkCenter();
            setImageMatrix(mScaleMatrix);
            float currentScale = getScale();
            if (((tmpScale > 1f) && (currentScale < mTargetScale)) || ((tmpScale < 1f) && (mTargetScale < currentScale))) {
                postDelayed(this, 16);
            } else {
                final float deltaScale = mTargetScale / currentScale;
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                checkCenter();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }
}
