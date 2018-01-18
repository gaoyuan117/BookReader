package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.TtsThemeBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/12/3.
 */

public class TtsAdapter extends BaseQuickAdapter<TtsThemeBean, BaseViewHolder> {

    private int p;

    public TtsAdapter(@LayoutRes int layoutResId, @Nullable List<TtsThemeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TtsThemeBean item) {
        TextView type = helper.getView(R.id.tv_type);
        type.setText(item.type);
        if (p == helper.getPosition()) {
            type.setSelected(true);
        } else {
            type.setSelected(false);
        }

        helper.setText(R.id.tv_type, item.type);
    }

    public  void setSelect(int position) {
        p = position;
        notifyDataSetChanged();
    }
}
