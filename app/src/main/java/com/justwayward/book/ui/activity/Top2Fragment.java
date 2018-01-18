package com.justwayward.book.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.bean.RankingList;
import com.justwayward.book.bean.SiteListBean;
import com.justwayward.book.common.OnRvItemClickListener;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.TopRankAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class Top2Fragment extends BaseFragment {

    @Bind(R.id.tv_hot)
    TextView tvHot;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.tv_collect)
    TextView tvCollect;
    @Bind(R.id.tv_word)
    TextView tvWord;
    @Bind(R.id.elvMale)
    ExpandableListView elvMale;

    private String id;
    private Intent intent;
    private String type, title;

    private List<RankingList.MaleBean> maleGroups = new ArrayList<>();
    private List<List<RankingList.MaleBean>> maleChilds = new ArrayList<>();
    private TopRankAdapter maleAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_top2;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        intent = new Intent(getActivity(), RankDetailActivity.class);
        id = getArguments().getString("id");
    }

    @Override
    public void attachView() {
        maleAdapter = new TopRankAdapter(getActivity(), maleGroups, maleChilds);
        elvMale.setAdapter(maleAdapter);
        getSiteList();
        maleAdapter.setItemClickListener(new ClickListener());
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }

    @OnClick({R.id.tv_hot, R.id.tv_save, R.id.tv_collect, R.id.tv_word})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_hot:
                type = "click";
                title = "最热榜";
                break;
            case R.id.tv_save:
                type = "view";
                title = "留存榜";
                break;
            case R.id.tv_collect:
                type = "collect";
                title = "收藏榜";
                break;
            case R.id.tv_word:
                type = "word";
                title = "字数榜";
                break;
        }

        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private void updateMale(List<SiteListBean> list) {

        maleGroups.clear();
        maleChilds.clear();
        List<RankingList.MaleBean> list1 = new ArrayList<>();

        RankingList.MaleBean bean1 = new RankingList.MaleBean();
        bean1._id = "click";
        bean1.cover = R.mipmap.zuire + "";
        bean1.title = "最热榜";

        RankingList.MaleBean bean2 = new RankingList.MaleBean();
        bean2._id = "view";
        bean2.cover = R.mipmap.liucun + "";
        bean2.title = "留存榜";

        RankingList.MaleBean bean3 = new RankingList.MaleBean();
        bean3._id = "collect";
        bean3.cover = R.mipmap.shoucang + "";
        bean3.title = "收藏榜";

        RankingList.MaleBean bean4 = new RankingList.MaleBean();
        bean4._id = "word";
        bean4.cover = R.mipmap.zishu + "";
        bean4.title = "字数榜";

        maleGroups.add(bean1);
        maleGroups.add(bean2);
        maleGroups.add(bean3);
        maleGroups.add(bean4);
        maleGroups.add(new RankingList.MaleBean("别人家的排行榜"));


        for (int i = 0; i < list.size(); i++) {
            String site_name = list.get(i).getSite_name();
            int id = list.get(i).getId();

            RankingList.MaleBean bean = new RankingList.MaleBean();
            bean.title = site_name;
            bean._id = id + "";

            list1.add(bean);

        }


        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(new ArrayList<RankingList.MaleBean>());
        maleChilds.add(list1);

        maleAdapter.notifyDataSetChanged();

    }

    private void getSiteList() {
        RetrofitClient.getInstance().createApi().getSiteList("")
                .compose(RxUtils.<HttpResult<List<SiteListBean>>>io_main())
                .subscribe(new BaseObjObserver<List<SiteListBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<SiteListBean> list) {
                        updateMale(list);
                    }
                });
    }


    class ClickListener implements OnRvItemClickListener<RankingList.MaleBean> {

        @Override
        public void onItemClick(View view, int position, RankingList.MaleBean data) {
            if (TextUtils.isEmpty(data.cover)) {

                intent.putExtra("id", id);
                intent.putExtra("site_id", data._id);
                intent.putExtra("title", data.title);
                startActivity(intent);
            } else {
                intent.putExtra("id", id);
                intent.putExtra("type", data._id);
                intent.putExtra("title", data.title);
                startActivity(intent);
            }
        }
    }

}
