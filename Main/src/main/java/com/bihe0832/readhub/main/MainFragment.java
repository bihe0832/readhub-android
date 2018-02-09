package com.bihe0832.readhub.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.me.MyFragment;
import com.bihe0832.readhub.news.NewsListFragment;
import com.bihe0832.readhub.news.NewsTabFragment;
import com.bihe0832.readhub.topic.TopicListFragment;
import com.ottd.libs.framework.fragment.BaseMainFragment;

import me.yokeyword.fragmentation.SupportFragment;

import static com.bihe0832.readhub.news.NewsListFragmentKt.TYPE_NORMAL_NEWS;
import static com.bihe0832.readhub.news.NewsListFragmentKt.TYPE_TECH_NEWS;

public class MainFragment extends BaseMainFragment {
    private static final int REQ_MSG = 10;


    public static final int TAB_FOR_TOPIC = 0;
    public static final int TAB_FOR_NEWS = 1;
    public static final int TAB_FOR_ME = 2;

    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;


    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.com_bihe0832_readhub_main_fragment_main, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(TopicListFragment.class);
        if (firstFragment == null) {
            mFragments[TAB_FOR_TOPIC] = TopicListFragment.newInstance();
            mFragments[TAB_FOR_NEWS] = NewsListFragment.newInstance(TYPE_NORMAL_NEWS);
            mFragments[TAB_FOR_ME] = MyFragment.newInstance();


            loadMultipleRootFragment(R.id.app_main_main_fragment_content, TAB_FOR_TOPIC,
                    mFragments[TAB_FOR_TOPIC],
                    mFragments[TAB_FOR_NEWS],
                    mFragments[TAB_FOR_ME]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[TAB_FOR_TOPIC] = firstFragment;
            mFragments[TAB_FOR_NEWS] = findChildFragment(NewsListFragment.class);
            mFragments[TAB_FOR_ME] = findChildFragment(MyFragment.class);
        }
    }

    private void initView(View view) {
        mBottomBar = (BottomBar) view.findViewById(R.id.app_main_main_fragment_bottomBar);
        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_view_carousel_black_24dp, getString(R.string.page_key_topic)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.if_icon_news, getString(R.string.page_key_news)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_account_circle_white_24dp, getString(R.string.page_key_me)));

//        // 模拟未读消息
//        mBottomBar.getItem(TAB_FOR_NEWS).setUnreadCount(9);

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);

//                BottomBarTab tab = mBottomBar.getItem(TAB_FOR_NEWS);
//                if (position == TAB_FOR_NEWS) {
//                    tab.setUnreadCount(0);
//                } else {
//                    tab.setUnreadCount(tab.getUnreadCount() + 1);
//                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
//                EventBusActivityScope.getDefault(_mActivity).post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }
}
