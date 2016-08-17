package com.panxiong.qqlayoumenu.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by panxi on 2016/3/30.
 * <p/>
 * 仿照QQ6.0版本的侧滑菜单控件 这个控件可以直接拿出来到其他项目中 因为没有其他资源文件与引用
 */
public class LeftMenuLayout extends FrameLayout {
    private Context context = null;

    /*滚动支持*/
    private CustomHorizontal customHorizontal = null;
    /*线性容器*/
    private LinearLayout rootLinearLayout = null;
    /*左，右视图*/
    private View leftView = null, rightView = null;
    /*滚动距离*/
    private int scrollSize_X = 0;
    /*左边菜单的宽度*/
    private int leftSize = 0;
    /*菜单是否是打开状态*/
    private boolean isOpen = false;
    /*蒙板视图*/
    private View maskView = null;
    /*菜单打开/关闭监听*/
    private MenuChangedListener menuChangedListener = null;

    public LeftMenuLayout(Context context) {
        super(context);
        this.context = context;
    }

    public LeftMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        init(); //
    }

    /*关闭菜单*/
    public void closeMenu() {
        isOpen = false;
        customHorizontal.smoothScrollTo(leftSize, 0);// 定位
        if (menuChangedListener != null) {
            menuChangedListener.onChanged(isOpen);
        }
        maskView.setVisibility(GONE);
    }

    /*打开菜单*/
    public void openMenu() {
        isOpen = true;
        customHorizontal.smoothScrollTo(0, 0);// 定位
        if (menuChangedListener != null) {
            menuChangedListener.onChanged(isOpen);
        }
        maskView.setVisibility(VISIBLE);
    }

    /*菜单是否打开*/
    public boolean isOpen() {
        return isOpen;
    }

    /*设置菜单打开/关闭监听*/
    public void setMenuChangedListener(MenuChangedListener menuChangedListener) {
        this.menuChangedListener = menuChangedListener;
    }

    /*初始化*/
    private void init() {
        /*创建根布局 横向滚动布局*/
        customHorizontal = new CustomHorizontal(context);
        FrameLayout.LayoutParams qqHorizontalLayoutParams = (FrameLayout.LayoutParams) customHorizontal.getLayoutParams();
        if (qqHorizontalLayoutParams == null) {
            qqHorizontalLayoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }
        qqHorizontalLayoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        qqHorizontalLayoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        customHorizontal.setLayoutParams(qqHorizontalLayoutParams);
        customHorizontal.setOverScrollMode(HorizontalScrollView.OVER_SCROLL_NEVER); // 关闭回弹效果
        customHorizontal.setHorizontalScrollBarEnabled(false);  // 关闭滚动条
        /*创建根布局 横向线性布局*/
        rootLinearLayout = new LinearLayout(context);
        LayoutParams rootLinearLayoutLayoutParams = (LayoutParams) rootLinearLayout.getLayoutParams();
        if (rootLinearLayoutLayoutParams == null) {
            rootLinearLayoutLayoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        rootLinearLayoutLayoutParams.width = LayoutParams.WRAP_CONTENT;
        rootLinearLayoutLayoutParams.height = LayoutParams.WRAP_CONTENT;
        rootLinearLayout.setLayoutParams(rootLinearLayoutLayoutParams);
        rootLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        /*获取子布局*/
        leftView = getChildAt(0);
        rightView = getChildAt(1);
        if (leftView == null || rightView == null) {
            throw new NullPointerException("左布局为空或者右布局为空，请至少给布局添加两个子布局。");
        }
        removeAllViews();   // 清空容器
        ViewGroup.LayoutParams layoutParamsLeft = leftView.getLayoutParams();   // 子布局管理器
        ViewGroup.LayoutParams layoutParamsRight = rightView.getLayoutParams(); // 子布局管理器
        int weight = getScreenSize(1);  // 屏幕宽度
        int height = getScreenSize(2);  // 屏幕高度
        leftSize = weight * 3 / 4; // 计算左布局的宽度
        layoutParamsLeft.width = leftSize;
        layoutParamsLeft.height = height;
        leftView.setLayoutParams(layoutParamsLeft);     // 设置左布局宽高
        layoutParamsRight.width = weight;
        layoutParamsRight.height = height;
        rightView.setLayoutParams(layoutParamsRight);    // 设置右布局宽高
        leftView.requestLayout();   // 刷新左布局
        rightView.requestLayout();  // 刷新右布局
        /*添加子布局*/
        rootLinearLayout.removeAllViews();
        rootLinearLayout.addView(leftView, 0);
        // rootLinearLayout.addView(rightView, 1);

        FrameLayout maskFrameLayout = new FrameLayout(context);
        maskFrameLayout.removeAllViews();
        maskFrameLayout.addView(rightView, 0);
        maskView = new View(context);
        ViewGroup.LayoutParams maskViewLayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        maskViewLayoutParams.width = getScreenSize(1);
        maskViewLayoutParams.height = getScreenSize(2);
        maskView.setLayoutParams(maskViewLayoutParams);
        maskView.setBackgroundColor(Color.argb(100, 0, 0, 0));
        maskFrameLayout.addView(maskView, 1);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen()) {
                    closeMenu();
                }
            }
        });
        rootLinearLayout.addView(maskFrameLayout, 1);

        /*保存到根布局*/
        customHorizontal.addView(rootLinearLayout);
        /*触摸捕捉*/
        customHorizontal.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (scrollSize_X > leftSize / 2) {
                        closeMenu();
                    } else {
                        openMenu();
                    }
                    return true;
                }
                return false;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeMenu();
            }
        }, 500);
        /*添加到界面*/
        addView(customHorizontal);
    }

    /*滑动支持*/
    private class CustomHorizontal extends HorizontalScrollView {

        public CustomHorizontal(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            scrollSize_X = l;
        }
    }

    /*获取屏幕宽高 flag 1 宽，2 高*/
    private int getScreenSize(int flag) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        if (flag == 1) {
            return outMetrics.widthPixels;
        } else {
            return outMetrics.heightPixels;
        }
    }

    /*菜单改变监听*/
    public interface MenuChangedListener {
        void onChanged(boolean isOpen);
    }

}
