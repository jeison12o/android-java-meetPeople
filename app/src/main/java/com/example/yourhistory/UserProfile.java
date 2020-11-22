package com.example.yourhistory;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.example.yourhistory.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class UserProfile extends AppCompatActivity {

    private ViewPager _page;
    private TabLayout _tabs;
    private PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);

        _tabs = findViewById(R.id.tabMenus);
        _page = findViewById(R.id.viewPager);

        _tabs.addTab(_tabs.newTab().setText("PHOTOS"));
        _tabs.addTab(_tabs.newTab().setText("HISTORY"));



        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), _tabs.getTabCount());
        _page.setAdapter(pagerAdapter);
        _page.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(_tabs));
        _tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                _page.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}