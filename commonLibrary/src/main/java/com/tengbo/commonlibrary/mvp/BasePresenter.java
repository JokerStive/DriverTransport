package com.tengbo.commonlibrary.mvp;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<M,V>  implements IPresenter<M,V>{
    protected M mModel;

    protected V mView;

    private WeakReference<V> mViewWeakReference;


    protected abstract void bindModel();


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
        mViewWeakReference = new WeakReference<>(view);
        bindModel();
    }

    @Override
    public void unBindView() {
        if (null != mViewWeakReference) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
    }
}
