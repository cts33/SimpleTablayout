# 1.SimpleTablayout

SimpleTablayout 只支持竖向展示的tabLayout样式，且item的样式目前也是固定的。后续如果需要会添加其他样式。

![https://github.com/cts33/SimpleTablayout/blob/master/PIC/image1.png?raw=true]()



## 2.使用

```java


 		//simpleTablelayout 组件只适用与竖向的tabLayout结构
        mTablayout = (SimpleTabLayout) findViewById(R.id.tablayout);
        //item之间的上下间距
        mTablayout.setItemMargin(50);

        //item的高度
        mTablayout.setItemHeight(100);

        mTablayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return 13;
            }

            @Override
            public TabView.TabIcon getIcon(int position) {
                //配置item里的图片
                return new ITabView.TabIcon.Builder()
                        //图片的状态切换
                        .setIcon(R.drawable.un_ic_baseline_5g_24, R.drawable.ic_baseline_5g_24)
                        //图片的位置
                        .setIconGravity(Gravity.START)
                        //图片和文字的间距
                        .setIconMargin(20)
                        //图片的大小
                        .setIconSize(40, 40)

                        .build();
            }

            @Override
            public TabView.TabTitle getTitle(int position) {
                //文字内容  文字的状态切换后的颜色，文字大小
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
```

