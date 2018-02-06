package com.bihe0832.readhub.test.debug;

import android.os.Bundle;

import com.bihe0832.readhub.test.base.BaseTestFragment;
import com.bihe0832.readhub.test.base.OnTestItemClickListener;
import com.bihe0832.readhub.test.base.TestItem;
import com.ottd.libs.framework.OttdFramework;

import java.util.ArrayList;
import java.util.List;

public class TestDebugFragment extends BaseTestFragment {

    public static TestDebugFragment newInstance() {

        Bundle args = new Bundle();
        TestDebugFragment fragment = new TestDebugFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public List<TestItem> getDataList() {
        List<TestItem> items = new ArrayList<TestItem>();
        TestItem item;

        item = new TestItem("调试入口", new OnTestItemClickListener() {
            @Override
            public void onItemClick() {
                OttdFramework.getInstance().showWaitting();
            }
        });
        items.add(item);

        return items;
    }

}
