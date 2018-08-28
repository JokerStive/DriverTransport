package com.tengbo.commonlibrary.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tengbo.commonlibrary.fragmentation.SupportFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends SupportFragment {

    protected View mRootView;
    protected FragmentManager mSupportFragmentManager;
    protected FragmentActivity mFragmentActivity;


    protected CompositeSubscription mSubscriptionManager = new CompositeSubscription();
    private Unbinder mUnBinder;


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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            mUnBinder = ButterKnife.bind(this, mRootView);
            initView();
        }

        return mRootView;
    }

    protected abstract void initView();

    protected abstract int getLayoutId();

    protected abstract void getTransferData(Bundle arguments);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
        if (mSubscriptionManager.hasSubscriptions() && !mSubscriptionManager.isUnsubscribed()) {
            mSubscriptionManager.unsubscribe();
        }
    }
}
