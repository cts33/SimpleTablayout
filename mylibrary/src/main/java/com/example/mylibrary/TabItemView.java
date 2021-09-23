package com.example.mylibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * tab的item 对象
 */
public class TabItemView extends TabView{

    private Context mContext;
    private TextView mTitle;
    private TabIcon mTabIcon;
    private TabTitle mTabTitle;
    private boolean mChecked;

    public TabItemView(Context context) {
        super(context);

        mContext = context;
        mTabIcon = new TabIcon.Builder().build();
        mTabTitle = new TabTitle.Builder().build();

        initView();
    }

    private void initView() {
        setMinimumHeight(dp2px(25));
        if (mTitle == null) {
            mTitle = new TextView(mContext);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;

            mTitle.setLayoutParams(params);
            this.addView(mTitle);
        }
        initTitleView();
        initIconView();
    }
    private void initTitleView() {
        setBackground(isChecked() ? getResources().getDrawable(R.drawable.selected_bg_shape):getResources().getDrawable(R.drawable.unselected_bg_shape));
        mTitle.setTextSize(mTabTitle.getTitleTextSize());
        mTitle.setText(mTabTitle.getContent());
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setEllipsize(TextUtils.TruncateAt.END);
        refreshDrawablePadding();
    }

    private void initIconView() {
        int iconResid = mChecked ? mTabIcon.getSelectedIcon() : mTabIcon.getNormalIcon();
        Drawable drawable = null;
        if (iconResid != 0) {
            drawable = mContext.getResources().getDrawable(iconResid);
            int r = mTabIcon.getIconWidth() != -1 ? mTabIcon.getIconWidth() : drawable.getIntrinsicWidth();
            int b = mTabIcon.getIconHeight() != -1 ? mTabIcon.getIconHeight() : drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, r, b);
        }
        switch (mTabIcon.getIconGravity()) {
            case Gravity.START:
                mTitle.setCompoundDrawables(drawable, null, null, null);
                break;
            case Gravity.TOP:
                mTitle.setCompoundDrawables(null, drawable, null, null);
                break;
            case Gravity.END:
                mTitle.setCompoundDrawables(null, null, drawable, null);
                break;
            case Gravity.BOTTOM:
                mTitle.setCompoundDrawables(null, null, null, drawable);
                break;
        }
        refreshDrawablePadding();
    }


    private void refreshDrawablePadding() {
        int iconResid = mChecked ? mTabIcon.getSelectedIcon() : mTabIcon.getNormalIcon();
        if (iconResid != 0) {
            if (!TextUtils.isEmpty(mTabTitle.getContent()) && mTitle.getCompoundDrawablePadding() != mTabIcon.getMargin()) {
                mTitle.setCompoundDrawablePadding(mTabIcon.getMargin());
            } else if (TextUtils.isEmpty(mTabTitle.getContent())) {
                mTitle.setCompoundDrawablePadding(0);
            }
        } else {
            mTitle.setCompoundDrawablePadding(0);
        }
    }
    @Override
    public ImageView getIconView() {
        return null;
    }

    @Override
    public TextView getTitleView() {
        return null;
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setSelected(checked);
        setBackground(isChecked() ? getResources().getDrawable(R.drawable.selected_bg_shape):getResources().getDrawable(R.drawable.unselected_bg_shape));
        refreshDrawableState();
        initIconView();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public TabView setIcon(TabIcon icon) {
        if (icon==null) return null;

        mTabIcon = icon;

        initIconView();
        return this;
    }

    @Override
    public TabView setTitle(TabTitle title) {
        if (title==null) return null;

        mTabTitle = title;
        initTitleView();
        return this;
    }

    @Override
    public TabIcon getIcon() {
        return mTabIcon;
    }

    @Override
    public TabTitle getTitle() {
        return mTabTitle;
    }


    public   int dp2px(  float dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5F);
    }
}
