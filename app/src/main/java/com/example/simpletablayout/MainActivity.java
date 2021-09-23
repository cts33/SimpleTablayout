package com.example.simpletablayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.ITabView;
import com.example.mylibrary.SimpleTabLayout;
import com.example.mylibrary.TabAdapter;
import com.example.mylibrary.TabItemView;
import com.example.mylibrary.TabView;

public class MainActivity extends AppCompatActivity {

    private SimpleTabLayout mTablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTablayout = (SimpleTabLayout) findViewById(R.id.tablayout);
        mTablayout.setItemMargin(50);
        mTablayout.setItemHeight(100);

        mTablayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return 13;
            }

            @Override
            public TabView.TabIcon getIcon(int position) {
                return new ITabView.TabIcon.Builder()

                        .setIcon(R.drawable.un_ic_baseline_5g_24, R.drawable.ic_baseline_5g_24)
                        .setIconGravity(Gravity.START)
                        .setIconMargin(20)
                        .setIconSize(40, 40)

                        .build();
            }

            @Override
            public TabView.TabTitle getTitle(int position) {

                return new TabView.TabTitle.Builder()
                        .setContent("GGG-" + position)
                        .setTextColor(R.color.black, android.R.color.holo_red_light)

                        .setTextSize(22)
                        .build();
            }


        });

        mTablayout.addOnTabSelectedListener(new SimpleTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                Log.d(TAG, "onTabSelected: " + position);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

                Log.d(TAG, "onTabReselected: " + position);
            }
        });


    }

    private static final String TAG = "MainActivity";
}