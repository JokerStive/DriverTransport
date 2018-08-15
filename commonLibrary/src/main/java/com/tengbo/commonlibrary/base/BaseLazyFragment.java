package com.tengbo.commonlibrary.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

/**
 * 懒加载fragment
 */
public abstract class BaseLazyFragment extends Fragment {

    private boolean isViewCreated;
    private boolean isDataLoad;

    private View mRootView;
    protected FragmentManager mSupportFragmentManager;
    protected FragmentActivity mFragmentActivity;


    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();
    private Unbinder mUnbinder;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && !isDataLoad) {
            loadData();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            getTransferData(arguments);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentActivity = getActivity();
        if (mFragmentActivity != null) {
            mSupportFragmentManager = mFragmentActivity.getSupportFragmentManager();
        }

        //非viewPager中使用fragment，setUserVisibleHint是不会调用的
        if (getUserVisibleHint()) {
            loadData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
            initView();
            isViewCreated = true;
        }

        return mRootView;
    }

    protected abstract void initView();

    protected abstract int getLayoutId();

    protected abstract void getTransferData(Bundle arguments);

    protected void loadData() {
        isDataLoad = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if (mSubscriptionManager.hasSubscriptions() && !mSubscriptionManager.isUnsubscribed()) {
            mSubscriptionManager.unsubscribe();
        }
    }
}
