package com.ruanyun.australianews.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 监听滚动监听，改变TopBar透明度
 * Created by  on 2016/12/8.
 */
public class TranslucentRecyclerView extends RecyclerView {

    private LinearLayoutManager mManager;// 不考虑StaggeredGridLayoutManager
    private int mPosition;// 第一个Item的Position
    private int mHeight;// 滑动高度
    private int mHeaderHeight;// 头部高度

    /**
     * 需要改变透明度的比例 默认滚动1px 返回1
     * 例如设置为2 滚动2px 返回1
     */
    private double ratio = 1.2;

    public TranslucentRecyclerView(Context context) {
        super(context);
        initView();
    }

    public TranslucentRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TranslucentRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        mManager = (LinearLayoutManager) layout;
    }

    private void initView() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mPosition = mManager.findFirstVisibleItemPosition();

                // 第一个可见的View
                View firstVisibleChildView = mManager.findViewByPosition(mPosition);
                // 第一个可见的View的高度
                int itemHeight = firstVisibleChildView.getHeight();

                if (mPosition == 0) {
                    // 获取header
                    View headerView = mManager.findViewByPosition(0);
                    // 获取第一个可见item的高度
                    mHeaderHeight = headerView.getHeight();

                    mHeight = mPosition * itemHeight - firstVisibleChildView.getTop();
                }
    
                if (mPosition != 0) {
                    mHeight = mPosition * itemHeight - firstVisibleChildView.getTop() + mHeaderHeight;
                }

                // 改变透明度
                if (translucentListener != null) {
                    mHeight /= ratio;
                    translucentListener.onTranslucent(mHeight > 255 ? 255 : mHeight);
                }
            }
        });
    }


    TranslucentListener translucentListener;

    public interface TranslucentListener {
        /**
         * 透明度的回调
         *
         * @param alpha
         */
        void onTranslucent(int alpha);
    }

    public void setTranslucentListener(TranslucentListener translucentListener) {
        this.translucentListener = translucentListener;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
