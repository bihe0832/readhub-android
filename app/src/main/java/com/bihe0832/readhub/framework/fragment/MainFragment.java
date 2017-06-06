package com.bihe0832.readhub.framework.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.module.readhub.adapter.ViewPagerAdapter;
import com.bihe0832.readhub.framework.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.com_bihe0832_readhub_main_fragment, container, false);
    }

    @Override
    protected void initView() {
        mTabLayout = customFindViewById(R.id.sliding_tabs);
        mViewPager = customFindViewById(R.id.viewpager);


        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.page_key_topic));
        titles.add(getString(R.string.page_key_news));
        titles.add(getString(R.string.page_key_developer));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getMContext(), titles, getFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
