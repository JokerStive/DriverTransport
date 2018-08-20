package com.tengbo.commonlibrary.mvp;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V>  implements IPresenter<V>{

    protected V mView;

    private WeakReference<V> mViewWeakReference;




    public V getView() {
        if (isAttach()) {
            return mViewWeakReference.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
        return null != mViewWeakReference && null != mViewWeakReference.get();
    }

    @Override
    public void bindView(V view) {
        mView = view;
        mViewWeakReference = new WeakReference<>(view);
    }

    @Override
    public void unBindView() {
        if (null != mViewWeakReference) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
        mView = null;
    }
}
