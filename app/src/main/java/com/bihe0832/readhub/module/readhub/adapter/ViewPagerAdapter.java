package com.bihe0832.readhub.module.readhub.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.Shakeba;
import com.bihe0832.readhub.module.readhub.topic.TopicFragment;

import java.util.List;

/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:11:48
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    private final List<String> mTitleList;
    private final Context mContext;

    public ViewPagerAdapter(Context context, List<String> titles, FragmentManager fm) {
        super(fm);
        mContext = context;
        mTitleList = titles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = createFragmentByTitle(mTitleList.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    private Fragment createFragmentByTitle(String title) {
        //默认的Fragment
        Fragment result = null;
        if (title.equals( getString(R.string.page_key_topic))) {
            result = new TopicFragment();
        }else if(title.equals( getString(R.string.page_key_news))){
//            result = new NewsFragment();
            result = new TopicFragment();
        }
        return result;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }


    private String getString(int id){
        return Shakeba.getInstance().getApplicationContext().getString(id);
    }
}
