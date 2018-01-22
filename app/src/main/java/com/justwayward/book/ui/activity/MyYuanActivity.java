package com.justwayward.book.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.book.AppConfig;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.bean.MyYuanListBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.MyYuanAdapter;
import com.justwayward.book.utils.RxUtil;
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyYuanActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_mingxi)
    TextView tvMingxi;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<MyYuanListBean> mList = new ArrayList<>();
    private MyYuanAdapter mAdapter;

    private String id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_yuan;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("我的源");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

        mAdapter = new MyYuanAdapter(this, R.layout.item_my_yuan, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(mAdapter);
        refresh.setOnRefreshListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        getList();
    }

    @Override
    public void configViews() {

    }

    @OnClick({R.id.img_back, R.id.tv_mingxi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_mingxi:
                showInputDialog("输入书名");
                break;
        }
    }

    /**
     * 更改昵称对话框
     */
    private void showInputDialog(final String title) {
        final EditText editText = new EditText(this);
        editText.setPadding(16, 100, 100, 16);
//        InputFilter[] filters = {new InputFilter.LengthFilter(8)};
//        editText.setFilters(filters);
        if (title.contains("书名")) {
            editText.setHint("请输入书名");
        } else {
            editText.setHint("http://");
        }
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(editText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = editText.getText().toString();
                        if (TextUtils.isEmpty(s)) {
                            showToastMsg("请" + title);
                            return;
                        }

                        if (title.contains("书名")) {
                            addBook(s);
                        } else {
                            if (!s.startsWith("http://")) {
                                showToastMsg("请输入正确的链接");
                                return;
                            }

                            if (!s.startsWith("https://")) {
                                showToastMsg("请输入正确的链接");
                                return;
                            }
                            addUrl(id, s);
                        }
                    }
                }).show();
    }


    private void addBook(String name) {
        RetrofitClient.getInstance().createApi().addBook(ReaderApplication.token, name)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("添加成功");
                        getList();
                    }
                });
    }

    private void addUrl(String id, String url) {
        RetrofitClient.getInstance().createApi().addSource(ReaderApplication.token, id, url)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("添加成功");
                        getList();
                    }
                });
    }

    public void getList() {
        RetrofitClient.getInstance().createApi().getNovelList(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<List<MyYuanListBean>>>io_main())
                .subscribe(new BaseObjObserver<List<MyYuanListBean>>(this, refresh) {
                    @Override
                    protected void onHandleSuccess(List<MyYuanListBean> list) {
                        mList.clear();
                        if (list == null || list.size() == 0) {
                            mAdapter.notifyDataSetChanged();
                            return;
                        }

                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void delNovel(String id) {
        RetrofitClient.getInstance().createApi().delNovel(ReaderApplication.token, id)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("删除成功");
                        getList();
                    }
                });
    }

    @Override
    public void onRefresh() {
        getList();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        id = mList.get(position).getId() + "";
        showInputDialog("小说网址");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        dialog(mList.get(position).getId() + "");
        return false;
    }


    private void dialog(final String id){
        new AlertDialog.Builder(this)
                .setMessage("是否移除该书")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delNovel(id);
                    }
                }).show();
    }
}
