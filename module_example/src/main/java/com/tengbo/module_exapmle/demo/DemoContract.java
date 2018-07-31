package com.tengbo.module_exapmle.demo;

import com.tengbo.commonlibrary.mvp.IModel;
import com.tengbo.commonlibrary.mvp.IPresenter;
import com.tengbo.commonlibrary.mvp.IView;


public interface DemoContract {
    interface View extends IView {

    }


    interface Presenter extends IPresenter<Model, View> {

    }


    interface Model extends IModel {

    }
}
