package com.bihe0832.readhub.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.test.debug.TestDebugFragment;
import com.bihe0832.readhub.news.NewsListFragment;
import com.bihe0832.readhub.topic.TopicListFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.ottd.libs.framework.fragment.BaseMainFragment;


/**
 * Created by hardyshi on 16/6/30.
 */
public class MainFragment extends BaseMainFragment {
    private ViewPager mViewPager;
    private SlidingTabLayout mTabBar;

    public static final int DEFAULT_TAB = 0;
    public static final String INTENT_EXTRA_KEY_TEST_ITEM_TAB = MainFragment.class.getName() + "INTENT_KEY_TAB";
    private static final String TAB_FOR_DEVELOPER = "动态测试";
    private static final String TAB_FOR_NEW = "资讯测试";
    private String[] mTabString = new String[]{
            TAB_FOR_DEVELOPER,TAB_FOR_NEW
    };

    public static MainFragment newInstance(int tab) {

        Bundle args = new Bundle();
        args.putInt(MainFragment.INTENT_EXTRA_KEY_TEST_ITEM_TAB, tab);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.com_bihe0832_test_main_fragment, container, false);
        initView(view);
        Bundle bundle = getArguments();
        int tab = DEFAULT_TAB;
        if (bundle != null) {
            tab = bundle.getInt(MainFragment.INTENT_EXTRA_KEY_TEST_ITEM_TAB);
        }
        mTabBar.setCurrentTab(tab);

        return view;
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.framework_viewPager);
        mViewPager.setAdapter(new MyTaskPagerFragmentAdapter(getChildFragmentManager()));

        mTabBar = (SlidingTabLayout) view.findViewById(R.id.framework_tab);
        mTabBar.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabBar.hideMsg(position);
            }

            @Override
            public void onPageSelected(int position) {
                mTabBar.hideMsg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MyTaskPagerFragmentAdapter extends FragmentPagerAdapter {

        public MyTaskPagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mTabString[position].equals(TAB_FOR_NEW)) {
                //修改此处，重新编译，即可使用new tab调试任意独立的无Activity模块的fragment
                return NewsListFragment.newInstance();
            }else if(mTabString[position].equals(TAB_FOR_DEVELOPER)){
                return TopicListFragment.newInstance();
            }else{
                return TestDebugFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return mTabString.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabString[position];
        }
    }
}
