package com.example.mylibrary;


public interface TabAdapter {
    int getCount();

    TabView.TabIcon getIcon(int position);

    TabView.TabTitle getTitle(int position);

    int getBackground(int position);
}
