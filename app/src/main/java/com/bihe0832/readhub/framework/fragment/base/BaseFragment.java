package com.bihe0832.readhub.framework.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author code@bihe0832.com
 */
public abstract class BaseFragment extends Fragment {

    private View mContentView;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = setContentView(inflater, container);
        mContext = getContext();
        mProgressDialog = new ProgressDialog(getMContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
        init();
        initView();
        initData();
        return mContentView;
    }

    protected void initData() {

    }

    protected void init() {

    }

    protected void initView() {
    }

    protected <T extends View> T customFindViewById(int id) {
        return (T) mContentView.findViewById(id);
    }

    protected abstract View setContentView(LayoutInflater inflater, ViewGroup container);

    protected View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }

    protected ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }
}
