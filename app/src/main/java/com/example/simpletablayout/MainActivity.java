package com.example.simpletablayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.ITabView;
import com.example.mylibrary.SimpleTabLayout;
import com.example.mylibrary.TabAdapter;
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
        mTablayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public TabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public TabView.TabTitle getTitle(int position) {
                return new ITabView.TabTitle.Builder().setContent("GGG-"+position).build();
            }

            @Override
            public int getBackground(int position) {
                return R.drawable.unselected_bg_shape;
            }
        });


    }
}