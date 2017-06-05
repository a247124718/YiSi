package com.yisi.picture.baselib.base.inter;

import java.util.List;

/**
 * Created by roy on 2017/2/5.
 */

public interface IBaseRefreshPresenter<T> extends IBasePresenter {
    void onSuccess(List<T> t);

    void request(boolean readCache);

    void onEmpty();

    void onFail(int errorCode);

}