package com.justwayward.book.ui.fragment;

import com.justwayward.book.R;
import com.justwayward.book.api.BookApi;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.bean.RankingList;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.ui.adapter.TopRankAdapter;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.RxUtil;
import com.justwayward.book.utils.StringUtils;
import com.justwayward.book.view.CustomExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by gaoyuan on 2017/11/20.
 */

public class TopFragment extends BaseFragment {

    @Bind(R.id.elvMale)
    CustomExpandableListView elvMale;

    private List<RankingList.MaleBean> maleGroups = new ArrayList<>();
    private List<List<RankingList.MaleBean>> maleChilds = new ArrayList<>();
    private TopRankAdapter maleAdapter;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_top;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        maleAdapter = new TopRankAdapter(getActivity(), maleGroups, maleChilds);
        elvMale.setAdapter(maleAdapter);

    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {
        getRankList();
    }

    @Override
    public void configViews() {

    }

    private void updateMale(RankingList rankingList) {
        List<RankingList.MaleBean> list = rankingList.male;
        List<RankingList.MaleBean> collapse = new ArrayList<>();
        for (RankingList.MaleBean bean : list) {
            if (bean.collapse) { // 折叠
                collapse.add(bean);
            } else {
                maleGroups.add(bean);
                maleChilds.add(new ArrayList<RankingList.MaleBean>());
            }
        }
        if (collapse.size() > 0) {
            maleGroups.add(new RankingList.MaleBean("别人家的排行榜"));
            maleChilds.add(collapse);
        }
        maleAdapter.notifyDataSetChanged();
    }

    public void getRankList() {
        BookApi api = new BookApi(new OkHttpClient());
        String key = StringUtils.creatAcacheKey("book-ranking-list");
        Observable<RankingList> fromNetWork = api.getRanking()
                .compose(RxUtil.<RankingList>rxCacheBeanHelper(key));

        //依次检查disk、network
        Subscription rxSubscription = Observable.concat(RxUtil.rxCreateDiskObservable(key, RankingList.class), fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RankingList>() {
                    @Override
                    public void onNext(RankingList data) {
                        if (data != null ) {
                            updateMale(data);
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("getRankList:" + e.toString());
                    }
                });
    }

}
