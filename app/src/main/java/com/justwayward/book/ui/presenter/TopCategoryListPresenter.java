/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justwayward.book.ui.presenter;

import com.justwayward.book.ReaderApplication;
import com.justwayward.book.api.BookApi;
import com.justwayward.book.base.RxPresenter;
import com.justwayward.book.bean.CategoryBean;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.contract.TopCategoryListContract;

import java.util.List;

import javax.inject.Inject;

/**
 * @author lfh.
 * @date 2016/8/30.
 */
public class TopCategoryListPresenter extends RxPresenter<TopCategoryListContract.View> implements TopCategoryListContract.Presenter<TopCategoryListContract.View> {

    private BookApi bookApi;

    @Inject
    public TopCategoryListPresenter(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    @Override
    public void getCategoryList() {
//        String key = StringUtils.creatAcacheKey("book-category-list");
//        Observable<CategoryList> fromNetWork = bookApi.getCategoryList()
//                .compose(RxUtil.<CategoryList>rxCacheBeanHelper(key));
//
//        //依次检查disk、network
//        Subscription rxSubscription = Observable.concat(RxUtil.rxCreateDiskObservable(key, CategoryList.class), fromNetWork)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<CategoryList>() {
//                    @Override
//                    public void onNext(CategoryList data) {
//                        if (data != null && mView != null) {
//                            mView.showCategoryList(data);
//                        }
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        LogUtils.i("complete");
//                        mView.complete();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtils.e(e.toString());
//                        mView.complete();
//                    }
//                });
//        addSubscrebe(rxSubscription);
//

        RetrofitClient.getInstance().createApi().getCategoryStatistical(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<List<CategoryBean>>>io_main())
                .subscribe(new BaseObjObserver<List<CategoryBean>>(mView.getContext()) {
                    @Override
                    protected void onHandleSuccess(List<CategoryBean> categoryBeen) {
                        if(categoryBeen!=null){
                            mView.showCategoryList(categoryBeen);
                        }
                    }
                });
    }
}
