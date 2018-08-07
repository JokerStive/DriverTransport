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

import com.tengbo.commonlibrary.mvp.BasePresenter;
import com.tengbo.commonlibrary.mvp.IView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseMvpFragment<T extends BasePresenter> extends Fragment implements IView{


    private View mRootView;
    protected FragmentManager mSupportFragmentManager;
    protected FragmentActivity mFragmentActivity;
    private T mPresent;
    private Unbinder mUnbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            getTransferData(arguments);
        }

        initPresent();
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
            mUnbinder = ButterKnife.bind(this, mRootView);
            initView();
        }

        return mRootView;
    }

    protected abstract  void initPresent();

    protected abstract void initView();

    protected abstract int getLayoutId();

    protected abstract void getTransferData(Bundle arguments);

    @Override
    public void onDetach() {
        super.onDetach();
        mPresent.unBindView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
