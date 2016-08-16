package com.panxiong.qqlayoumenu.widget_2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.panxiong.qqlayoumenu.R;

public class MainActivity extends Activity {

    private PxHorizontalScrollView horizontalScrollView;
    private LinearLayout layoutLeft;
    private LinearLayout layoutRight;

    private int moveSize = 0;  //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horizontalScrollView = (PxHorizontalScrollView) findViewById(R.id.horizontalscrollview);
        layoutLeft = (LinearLayout) findViewById(R.id.layout_left);
        layoutRight = (LinearLayout) findViewById(R.id.layout_right);

        LinearLayout.LayoutParams layoutParamsLeft = (LinearLayout.LayoutParams) layoutLeft.getLayoutParams();
        LinearLayout.LayoutParams layoutParamsRight = (LinearLayout.LayoutParams) layoutRight.getLayoutParams();

        int weight = getScreenHeight(this, true);
        final int height = getScreenHeight(this, false);

        final int leftSize = Integer.valueOf(weight * 3 / 4);

        layoutParamsLeft.width = leftSize;
        layoutParamsLeft.height = height;

        layoutParamsRight.width = weight;
        layoutParamsRight.height = height;

        layoutLeft.setLayoutParams(layoutParamsLeft);
        layoutRight.setLayoutParams(layoutParamsRight);

        layoutLeft.requestLayout();
        layoutRight.requestLayout();

        horizontalScrollView.setScrollViewScrollChanged(new PxHorizontalScrollView.ScrollViewScrollChanged() {
            @Override
            public void onScrollChanged(int horizontal, int vertical, int oldHorizontal, int oldVertical) {
                moveSize = horizontal;
            }
        });

        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (moveSize > leftSize / 2) {
                        horizontalScrollView.smoothScrollTo(leftSize, 0);
                    } else {
                        horizontalScrollView.smoothScrollTo(0, 0);
                    }
                    return true;
                }
                return false;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.smoothScrollTo(leftSize, 0);
            }
        }, 500);
    }

    public static int getScreenHeight(Context context, boolean width) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        if (width) {
            return outMetrics.widthPixels;
        } else {
            return outMetrics.heightPixels;
        }
    }
}
