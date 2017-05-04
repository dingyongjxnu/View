package com.dingyong.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.dingyong.view.R;

/**
 * Created by Administrator on 2017/4/13.
 */
public class QQListView extends ListView {
    private static final String TAG = QQListView.class.getSimpleName();

    private PopupWindow mPopupWindow;
    private LayoutInflater mInflater;
    private int mPopupWindowWidth;
    private int mPopupWindowHeight;
    private Button mButton;
    private int xDown;
    private int yDown;
    private int xMove;
    private int yMove;
    private int mCurrentViewPos;
    private View mCurrentView;
    private boolean isSliding;
    private int touchSlop;

    public QQListView(Context context) {
        this(context, null);
    }

    public QQListView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public QQListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.delete_btn, null);
        mButton = (Button) view.findViewById(R.id.id_item_btn);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.getContentView().measure(0, 0);
        mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();
        mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown = x;
                yDown = y;
                if (mPopupWindow.isShowing()) {
                    dismissPopWindow();

                }
                mCurrentViewPos = pointToPosition(xDown, yDown);
                mCurrentView = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;
                int dy = yMove - yDown;
                // 判断是否是从右到左的滑动
                if (xMove < xDown && Math.abs(dx) > touchSlop && Math.abs(dy) < touchSlop) {
                    isSliding = true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (isSliding) {
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    int location[] = new int[2];
                    // 获得当前item的位置x与y
                    mCurrentView.getLocationOnScreen(location);
                    mPopupWindow.update();
                    mPopupWindow.setAnimationStyle(R.style.popwindow_delete_btn_anim_style);
                    mPopupWindow.showAtLocation(mCurrentView, Gravity.LEFT | Gravity.TOP,
                            location[0] + mCurrentView.getWidth(), location[1] + mCurrentView.getHeight() / 2
                                    - mPopupWindowHeight / 2);
                    mButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mDeleteClickListener != null) {
                                mDeleteClickListener.onClick(v,mCurrentViewPos);
                                dismissPopWindow();

                            }
                        }
                    });
                    break;
                case MotionEvent.ACTION_UP:
                    isSliding = false;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void dismissPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private OnDeleteClickListener mDeleteClickListener;

    public void setDeleteClickListener(OnDeleteClickListener deleteClickListener) {
        mDeleteClickListener = deleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onClick(View view,int position);
    }


}
