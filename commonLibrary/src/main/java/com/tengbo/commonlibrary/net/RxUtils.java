package com.tengbo.commonlibrary.net;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by youke
 * Observable转换器，数据预处理，Observable.compose(RxUtils.handleResult())得到真正需要的数据
 */

public class RxUtils {
    public static <T> Observable.Transformer<BaseResponse<T>, T> handleResult() {
        return responseObservable -> responseObservable.map(baseResponse -> {
            if (!baseResponse.isSuccess()) {
                throw new ApiException(baseResponse.getCode(), baseResponse.getMessage());
            } else {
                return baseResponse.getData();
            }
        });
    }



    public static <T> Observable<T> dealObservable(Observable<BaseResponse<T>> observable) {
       return observable.compose(handleResult())
                .compose(applySchedule());
    }


    /**
     * 统一线程处理
     */
    public static <T> Observable.Transformer<T, T> applySchedule() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

