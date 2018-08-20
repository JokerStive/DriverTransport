package com.tengbo.commonlibrary.mvp;

public interface IPresenter<V> {
    void bindView(V view);
    void unBindView();
}
