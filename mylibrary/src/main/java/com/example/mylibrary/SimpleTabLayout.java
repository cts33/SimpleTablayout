package com.example.mylibrary;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class SimpleTabLayout extends ScrollView {
    private float mIndicatorCorners;
    private int mIndicatorGravity;
    private int mTabMargin;
    private int mTabMode;
    private int mTabHeight;
    private int mIndicatorWidth;
    private int mColorIndicator;
    private static final int TAB_MODE_FIXED = 10;
    private static final int TAB_MODE_SCROLLABLE = 11;
    private TabStrip mTabStrip;
    private TabView mSelectedTab;
    private List<OnTabSelectedListener> mTabSelectedListeners = new ArrayList<>();
    private TabAdapter mTabAdapter;

    public SimpleTabLayout(Context context) {
        this(context, null);
    }

    public SimpleTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SimpleTabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.simple_TabLayout);
        mColorIndicator = typedArray.getColor(R.styleable.simple_TabLayout_indicator_color,
                context.getResources().getColor(R.color.purple_200));
        mIndicatorWidth = (int) typedArray.getDimension(R.styleable.simple_TabLayout_indicator_width, 9);
        mIndicatorCorners = typedArray.getDimension(R.styleable.simple_TabLayout_indicator_corners, 0);
        mIndicatorGravity = typedArray.getInteger(R.styleable.simple_TabLayout_indicator_gravity, Gravity.LEFT);

        mTabMargin = (int) typedArray.getDimension(R.styleable.simple_TabLayout_tab_margin, 0);
        mTabMode = typedArray.getInteger(R.styleable.simple_TabLayout_tab_mode, TAB_MODE_FIXED);
        int defaultTabHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
        mTabHeight = (int) typedArray.getDimension(R.styleable.simple_TabLayout_tab_height, defaultTabHeight);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) removeAllViews();
        initTabStrip();
    }

    private void initTabStrip() {
        mTabStrip = new TabStrip(getContext());
        addView(mTabStrip, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void addTab(TabView tabView) {
        if (tabView != null) {
            addTabWithMode(tabView);
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = mTabStrip.indexOfChild(view);
                    setTabSelected(position);
                }
            });
        } else {
            throw new IllegalStateException("tabview can't be null");
        }
    }

    public void setTabSelected(final int position) {
        setTabSelected(position, true, true);
    }

    private void setTabSelected(final int position, final boolean updataIndicator, final boolean callListener) {
        post(new Runnable() {
            @Override
            public void run() {
                setTabSelectedImpl(position, updataIndicator, callListener);
            }
        });
    }

    private void setTabSelectedImpl(final int position, boolean updataIndicator, boolean callListener) {
        TabView view = getTabAt(position);
        boolean selected;
        if (selected = (view != mSelectedTab)) {
            if (mSelectedTab != null) {
                mSelectedTab.setChecked(false);
            }
            view.setChecked(true);
            if (updataIndicator) {
                // TODO 切换tab的动画
//                mTabStrip.moveIndicatorWithAnimator(position);
            }
            mSelectedTab = view;
            scrollToTab(position);
        }
        if (callListener) {
            for (int i = 0; i < mTabSelectedListeners.size(); i++) {
                OnTabSelectedListener listener = mTabSelectedListeners.get(i);
                if (listener != null) {
                    if (selected) {
                        listener.onTabSelected(view, position);
                    } else {
                        listener.onTabReselected(view, position);
                    }
                }
            }
        }
    }

    private void scrollToTab(int position) {
        final TabView tabView = getTabAt(position);
        int y = getScrollY();
        int tabTop = tabView.getTop() + tabView.getHeight() / 2 - y;
        int target = getHeight() / 2;
        if (tabTop > target) {
            smoothScrollBy(0, tabTop - target);
        } else if (tabTop < target) {
            smoothScrollBy(0, tabTop - target);
        }
    }

    private void addTabWithMode(TabView tabView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        initTabWithMode(params);
        mTabStrip.addView(tabView, params);
        if (mTabStrip.indexOfChild(tabView) == 0) {
            tabView.setChecked(true);
            params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            tabView.setLayoutParams(params);
            mSelectedTab = tabView;
            mTabStrip.post(new Runnable() {
                @Override
                public void run() {
                    mTabStrip.moveIndicator(0);
                }
            });
        }
    }


    public TabView getTabAt(int position) {
        return (TabView) mTabStrip.getChildAt(position);
    }

    private void initTabWithMode(LinearLayout.LayoutParams params) {
        if (mTabMode == TAB_MODE_FIXED) {
            params.height = 0;
            params.weight = 1.0f;
            params.setMargins(0, 0, 0, 0);
            setFillViewport(true);
        } else if (mTabMode == TAB_MODE_SCROLLABLE) {
            params.height = mTabHeight;
            params.weight = 0f;
            params.setMargins(0, mTabMargin, 0, 0);
            setFillViewport(false);
        }
    }

    public void setTabMode(int mode) {


    }

    public void setTabHeight() {

    }

    public void setTabAdapter(TabAdapter adapter) {
        removeAllTabs();
        if (adapter != null) {
            mTabAdapter = adapter;
            for (int i = 0; i < adapter.getCount(); i++) {
                TabItemView tabItemView = new TabItemView(getContext());
                tabItemView
                        .setTitle(adapter.getTitle(i))
                        .setBackground(adapter.getBackground(i));
                addTab(tabItemView);
            }
        }
    }

    public void removeAllTabs() {
        mTabStrip.removeAllViews();
        mSelectedTab = null;
    }

    private class TabStrip extends LinearLayout {

        private float mIndicatorTopY;
        private float mIndicatorX;
        private float mIndicatorBottomY;
        private int mLastWidth;
        private Paint mIndicatorPaint;
        private RectF mIndicatorRect;
        private AnimatorSet mIndicatorAnimatorSet;

        public TabStrip(Context context) {
            super(context);

            setWillNotDraw(false);
            setOrientation(LinearLayout.VERTICAL);
            mIndicatorPaint = new Paint();
            mIndicatorPaint.setAntiAlias(true);
            mIndicatorGravity = mIndicatorGravity == 0 ? Gravity.LEFT : mIndicatorGravity;
            mIndicatorRect = new RectF();
            setIndicatorGravity();
        }

        protected void setIndicatorGravity() {
            if (mIndicatorGravity == Gravity.LEFT) {
                mIndicatorX = 0;
                if (mLastWidth != 0) mIndicatorWidth = mLastWidth;
                setPadding(mIndicatorWidth, 0, 0, 0);
            } else if (mIndicatorGravity == Gravity.RIGHT) {
                if (mLastWidth != 0) mIndicatorWidth = mLastWidth;
                setPadding(0, 0, mIndicatorWidth, 0);
            } else if (mIndicatorGravity == Gravity.FILL) {
                mIndicatorX = 0;
                setPadding(0, 0, 0, 0);
            }
            post(new Runnable() {
                @Override
                public void run() {
                    if (mIndicatorGravity == Gravity.RIGHT) {
                        mIndicatorX = getWidth() - mIndicatorWidth;
                    } else if (mIndicatorGravity == Gravity.FILL) {
                        mLastWidth = mIndicatorWidth;
                        mIndicatorWidth = getWidth();
                    }
                    invalidate();
                }
            });
        }

        protected void moveIndicator(float offset) {
            calcIndicatorY(offset);
            invalidate();
        }

        private void calcIndicatorY(float offset) {
            int index = (int) Math.floor(offset);
            View childView = getChildAt(index);
            if (Math.floor(offset) != getChildCount() - 1 && Math.ceil(offset) != 0) {
                View nextView = getChildAt(index + 1);
                mIndicatorTopY = childView.getTop() + (nextView.getTop() - childView.getTop()) * (offset - index);
                mIndicatorBottomY = childView.getBottom() + (nextView.getBottom() -
                        childView.getBottom()) * (offset - index);
            } else {
                mIndicatorTopY = childView.getTop();
                mIndicatorBottomY = childView.getBottom();
            }
        }
    }

    public interface OnTabSelectedListener {

        void onTabSelected(TabView tab, int position);

        void onTabReselected(TabView tab, int position);
    }
}
