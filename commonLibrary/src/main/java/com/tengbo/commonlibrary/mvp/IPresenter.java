package com.tengbo.commonlibrary.mvp;

public interface IPresenter<M,V> {
    void bindView(V view);
    void unBindView();
}
