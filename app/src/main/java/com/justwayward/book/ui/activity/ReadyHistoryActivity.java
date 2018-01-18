package com.justwayward.book.ui.activity;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.HistoryBean;
import com.justwayward.book.bean.HistoryBeanDao;
import com.justwayward.book.bean.SwitchBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.ReadyHistoryAdapter;
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ReadyHistoryActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_clear)
    TextView tvClear;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private ReadyHistoryAdapter mAdapter;
    private HistoryBeanDao historyBeanDao;
    private List<HistoryBean> list;
    private boolean isOpen = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ready_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        historyBeanDao = ReaderApplication.getDaoInstant().getHistoryBeanDao();
        list = this.historyBeanDao.queryBuilder().list();

        if (list != null) {
            Collections.reverse(list);
            mAdapter = new ReadyHistoryAdapter(R.layout.item_ready_history, list);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
            itemDecoration.setDrawLastItem(false);
            recyclerview.addItemDecoration(itemDecoration);
            recyclerview.setAdapter(mAdapter);
            mAdapter.setOnItemChildClickListener(this);
        }
    }

    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
        getMemberSwitch();
    }

    @Override
    public void configViews() {

    }

    @OnClick({R.id.img_back, R.id.tv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_clear:
                if (list == null || list.isEmpty()) {
                    return;
                }
                showClearDialog();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        HistoryBean bean = list.get(position);
        if (isOpen) {//后台开启了会员模式
            if (ReaderApplication.isvip) {//用户是会员
                ReadActivity.startActivity(this, bean.getTitle(), bean.getBookId(), bean.getIsShelf(), bean.getPic(), bean.getPic(), bean.getDes());
            } else {//不是会员
                CoomonApi.showBuyVipDialog(getActivity());
            }
        } else {//没有开启会员开关
            ReadActivity.startActivity(this, bean.getTitle(), bean.getBookId(), bean.getIsShelf(), bean.getPic(), bean.getPic(), bean.getDes());
        }
    }

    /**
     * 获取会员开关
     */
    private void getMemberSwitch() {
        RetrofitClient.getInstance().createApi().getMemberSwitch(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<SwitchBean>>io_main())
                .subscribe(new BaseObjObserver<SwitchBean>(this, "获取中") {
                    @Override
                    protected void onHandleSuccess(SwitchBean switchBean) {
                        isOpen = switchBean.isMember_switch();
                    }});
    }

    /**
     * 清空对话框
     */
    private void showClearDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否清空历史浏览记录")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }})
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        historyBeanDao.deleteAll();
                        list.clear();
                        mAdapter.notifyDataSetChanged();
                    }})
                .create().show();
    }
}
