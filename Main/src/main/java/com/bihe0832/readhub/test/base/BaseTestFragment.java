package com.bihe0832.readhub.test.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bihe0832.readhub.R;
import com.ottd.libs.framework.fragment.BaseBackFragment;

import java.util.List;

public abstract class BaseTestFragment extends BaseBackFragment {
    private RecyclerView mRecy;
    private TestPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.com_bihe0832_test_tab_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mAdapter = new TestPagerAdapter(_mActivity);
        mAdapter.setDatas(getDataList());
        mRecy = (RecyclerView) view.findViewById(R.id.test_recy);
        mRecy.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        mRecy.setLayoutManager(manager);
        mRecy.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public abstract List<TestItem> getDataList();

    protected void sendInfo(String title, String content){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, title));
    }

}
