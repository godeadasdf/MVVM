package com.example.kangning.mvvm;

import android.content.Context;
import android.graphics.Interpolator;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by kangning on 17/3/9.
 */

public class ScrollLinear extends LinearLayout {


    private LinearLayout mid;
    private LinearLayout left;
    private LinearLayout right;

    private Scroller scroller;
    private Interpolator interpolator;
    private float startX;

    public ScrollLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollLinear(Context context) {
        super(context);
        initView(context);
        //interpolator = new
        scroller = new Scroller(context);

    }

    private void initView(Context context) {
        mid = new LinearLayout(context);
        mid.setBackgroundResource(android.R.color.holo_green_light);
        mid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        left = new LinearLayout(context);
        left.setBackgroundResource(android.R.color.holo_red_light);
        left.setLayoutParams(new LayoutParams(getScreeWidth() / 2, LayoutParams.MATCH_PARENT));
        right = new LinearLayout(context);
        right.setBackgroundResource(android.R.color.holo_blue_light);
        right.setLayoutParams(new LayoutParams(getScreeWidth() / 2, LayoutParams.MATCH_PARENT));
        this.addView(left);
        this.addView(right);
        this.addView(mid);
    }

    public int getScreeWidth() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mid.measure(widthMeasureSpec, heightMeasureSpec);
        left.measure(left.getLayoutParams().width, heightMeasureSpec);
        right.measure(right.getLayoutParams().width, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mid.layout(l, t, r, b);
        right.layout(r, t, r + right.getLayoutParams().width, b);
        left.layout(l - left.getLayoutParams().width, t, l, b);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float offset = moveX - startX;
                if (getScrollX() == left.getLeft()) {
                    if (offset < 0) scrollBy(-(int) offset, 0);
                } else if (getScrollX() == right.getRight() - mid.getWidth()) {
                    if (offset > 0) scrollBy(-(int) offset, 0);
                } else {
                    if (getScrollX() - offset < left.getLeft())
                        scrollTo(left.getLeft(), 0);
                    else if (getScrollX() - offset > (right.getRight() - mid.getWidth()))
                        scrollTo(right.getRight() - mid.getWidth(), 0);
                    else
                        scrollBy(-(int) offset, 0);
                }
                startX = moveX;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int scrollX = getScrollX();

                Log.d("...", "scrollX" + scrollX + "right.getMeasuredWidth()" + right.getWidth());

                if (scrollX > right.getWidth() / 3 && scrollX < right.getWidth()) {
                    slipToRight(scrollX);
                } else if (scrollX < -left.getWidth() / 3 && scrollX > -left.getWidth()) {
                    slipToLeft(scrollX);
                } else if (scrollX > 0 && scrollX <= right.getWidth() / 3) {
                    slipToMid(1, scrollX);
                } else if (scrollX < 0 && scrollX >= -left.getWidth() / 3) {
                    slipToMid(2, scrollX);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void slipToLeft(int scrollX) {
        Log.d("...", "slipToLeft");
        float ratio = (float)(-left.getWidth()-scrollX) / (float) (-left.getWidth());
        scroller.startScroll(scrollX, 0, -left.getWidth()-scrollX, 0, (int)(1000 * ratio));
        invalidate();
    }


    private void slipToRight(int scrollX) {
        Log.d("...", "slipToRight");
        float ratio = (float)(right.getWidth()-scrollX) / (float) (right.getWidth());
        scroller.startScroll(scrollX, 0, right.getWidth()-scrollX, 0, (int)(1000 * ratio));
        invalidate();
    }

    private void slipToMid(int type, int scrollX) {
        if (type == 1) {
            Log.d("...", "slipToMid1");
            float ratio = (float)(scrollX) / (float) (right.getWidth());
            scroller.startScroll(scrollX, 0, -scrollX, 0, (int)(1000 * ratio));
            invalidate();
        } else if (type == 2) {
            Log.d("...", "slipToMid2");
            float ratio = (float)(-scrollX) / (float) (left.getWidth());
            scroller.startScroll(scrollX, 0, -scrollX, 0, (int)(1000 * ratio));
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
    }

    public void addViewToMiddle(View view){
        mid.addView(view);
    }
}
