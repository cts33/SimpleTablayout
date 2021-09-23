package com.example.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class SimpleTabLayout extends ScrollView {
    private int mTabMargin;
    private int mTabHeight;
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

        mTabMargin = (int) typedArray.getDimension(R.styleable.simple_TabLayout_tab_margin, dp2px(10));
        mTabHeight = (int) typedArray.getDimension(R.styleable.simple_TabLayout_tab_height, dp2px(50));
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

            mSelectedTab = view;

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
    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        if (listener != null) {
            mTabSelectedListeners.add(listener);
        }
    }

    private void addTabWithMode(TabView tabView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        initTabViewLayoutParams(params);
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

                }
            });
        }
    }


    public TabView getTabAt(int position) {
        return (TabView) mTabStrip.getChildAt(position);
    }

    private void initTabViewLayoutParams(LinearLayout.LayoutParams params) {

        params.height = mTabHeight;
        params.weight = 0f;
        params.setMargins(0, mTabMargin, 0, 0);
        setFillViewport(false);

    }



    public void setTabAdapter(TabAdapter adapter) {
        removeAllTabs();
        if (adapter != null) {
            mTabAdapter = adapter;
            for (int i = 0; i < adapter.getCount(); i++) {
                TabItemView tabItemView = new TabItemView(getContext());
                tabItemView.setPadding(20,20,20,20);

                tabItemView.setTitle(adapter.getTitle(i));
                addTab(tabItemView);
            }
        }
    }

    public void removeAllTabs() {
        mTabStrip.removeAllViews();
        mSelectedTab = null;
    }

    private class TabStrip extends LinearLayout {


        public TabStrip(Context context) {
            super(context);

            setWillNotDraw(false);
            setOrientation(LinearLayout.VERTICAL);

        }



    }

    public interface OnTabSelectedListener {

        void onTabSelected(TabView tab, int position);

        void onTabReselected(TabView tab, int position);
    }

    public int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
