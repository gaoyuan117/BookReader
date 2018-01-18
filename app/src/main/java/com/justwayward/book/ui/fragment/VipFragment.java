package com.justwayward.book.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.bean.CategoryListBean;
import com.justwayward.book.bean.UserBean;
import com.justwayward.book.bean.user.Login;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.activity.BookDetailActivity;
import com.justwayward.book.ui.activity.MyVipActivity;
import com.justwayward.book.ui.activity.VipOtherActivity;
import com.justwayward.book.ui.adapter.CategoryAdapter;
import com.justwayward.book.utils.LogUtils;
import com.yuyh.easyadapter.glide.GlideCircleTransform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2017/11/17.
 */

public class VipFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<CategoryListBean.DataBean> mList = new ArrayList<>();
    private View headView;
    private CategoryAdapter mAdapter;
    private String title;
    private String type;
    private Intent intent;
    private TextView tvTime;
    private TextView tvBuy;
    private TextView tvName;
    private ImageView imgAvatar;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        headView = View.inflate(getActivity(), R.layout.head_book_city_vip, null);
        intent = new Intent(getActivity(), VipOtherActivity.class);
        imgAvatar = (ImageView) headView.findViewById(R.id.img_avatar);
        tvName = (TextView) headView.findViewById(R.id.tv_name);
        tvTime = (TextView) headView.findViewById(R.id.tv_time);
        tvBuy = (TextView) headView.findViewById(R.id.tv_buy);

        headView.findViewById(R.id.tv_chang_du).setOnClickListener(this);
        headView.findViewById(R.id.tv_vip).setOnClickListener(this);
        headView.findViewById(R.id.tv_other).setOnClickListener(this);
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyVipActivity.class));
            }
        });

        mAdapter = new CategoryAdapter(R.layout.item_sub_category_list, mList);
        mAdapter.addHeaderView(headView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);

        refresh.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {
        getList();
    }

    @Override
    public void configViews() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_chang_du://畅读书城
                title = "畅读书城";
                type = "changdu";
                break;
            case R.id.tv_vip://会员
                title = "会员";
                type = "huiyuan";
                break;
            case R.id.tv_other://其他
                title = "其他";
                type = "other";
                break;
        }
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void getList() {
        Map<String, Object> map = new HashMap<>();
        map.put("vip_read", 1);
        RetrofitClient.getInstance().createApi().getNovelByCategory(map, 1)
                .compose(RxUtils.<HttpResult<CategoryListBean>>io_main())
                .subscribe(new BaseObjObserver<CategoryListBean>(getActivity(), refresh) {
                    @Override
                    protected void onHandleSuccess(CategoryListBean categoryListBean) {

                        if (categoryListBean.getData().isEmpty()) {
                            return;
                        }
                        mList.clear();
                        mList.addAll(categoryListBean.getData());
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    @Override
    public void onRefresh() {
        getUser();
        getList();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BookDetailActivity.startActivity(getActivity(), mList.get(position).getId() + "");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getUser();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUser() {
        RetrofitClient.getInstance().createApi().getUser(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<UserBean>>io_main())
                .subscribe(new BaseObjObserver<UserBean>(getActivity(), false) {
                    @Override
                    protected void onHandleSuccess(UserBean userBean) {
                        setUserData(userBean);
                    }
                });
    }

    private void setUserData(UserBean userBean) {

        ReaderApplication.user = userBean;

        long expire_time = userBean.getExpire_time();//到期时间
        Date currentTime = new Date();
        long diff = expire_time * 1000 - currentTime.getTime();

        long days = diff / (1000 * 60 * 60 * 24);
        LogUtils.e("剩余时间：" + days);

        if (days <= 0) {
            tvBuy.setText("立即开通");
            tvTime.setText("暂未开通会员");
        } else {
            tvBuy.setText("续费");
            tvTime.setText("包月会员用户(剩余" + days + "天)");
        }

        tvName.setText(userBean.getUser_nickname());
        Glide.with(mContext).load(userBean.getAvatar())
                .error(R.drawable.avatar_default)
                .transform(new GlideCircleTransform(mContext)).into(imgAvatar);

    }
}
