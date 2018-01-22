package com.justwayward.book.ui.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.WebViewActivity;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.bean.MyYuanListBean;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.activity.MyYuanActivity;
import com.justwayward.book.ui.activity.YuanWebViewActivity;
import com.justwayward.book.utils.ToastUtils;
import com.justwayward.book.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyuan on 2018/1/22.
 */

public class MyYuanAdapter extends BaseQuickAdapter<MyYuanListBean, BaseViewHolder> {
    MyYuanActivity activity;

    public MyYuanAdapter(MyYuanActivity activity, int layoutResId, @Nullable List<MyYuanListBean> data) {
        super(layoutResId, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyYuanListBean item) {

        helper.addOnClickListener(R.id.tv_yuan_add);

        helper.setText(R.id.tv_yuan_name, item.getNovel());

        MyGridView gridView = helper.getView(R.id.grid_view);

        MuYuanItemAdapter adapter = new MuYuanItemAdapter(mContext, item.getSource());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.startActivity(new Intent(mContext, YuanWebViewActivity.class).putExtra("title", item.getNovel())
                        .putExtra("url", item.getSource().get(position).getUrl()));
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialog(item.getSource().get(position).getId() + "");
                return true;
            }
        });
    }

    private void delResoue(String id) {
        RetrofitClient.getInstance().createApi().delSource(ReaderApplication.token, id)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(mContext) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showToast("删除成功");
                        activity.getList();

                    }
                });
    }


    private void dialog(final String id) {
        new AlertDialog.Builder(mContext)
                .setMessage("是否移除该链接")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delResoue(id);
                    }
                }).show();
    }

}
