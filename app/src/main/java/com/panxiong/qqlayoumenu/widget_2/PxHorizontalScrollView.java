package com.panxiong.qqlayoumenu.widget_2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by panxi on 2016/3/30.
 * <p/>
 * 修改滚动视图 回传滚动参数
 */
public class PxHorizontalScrollView extends HorizontalScrollView {

    private ScrollViewScrollChanged scrollViewScrollChanged = null;

    public PxHorizontalScrollView(Context context) {
        super(context);
    }

    public PxHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewScrollChanged != null) {
            scrollViewScrollChanged.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setScrollViewScrollChanged(ScrollViewScrollChanged scrollViewScrollChanged) {
        this.scrollViewScrollChanged = scrollViewScrollChanged;
    }

    public interface ScrollViewScrollChanged {
        void onScrollChanged(int horizontal, int vertical, int oldHorizontal, int oldVertical);
    }
}
