package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.bean.RechargeRecordBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/11/23.
 */

public class RechargeItemAdapter extends BaseAdapter {
    private Context context;
    private List<RechargeRecordBean.ListBean> mList;


    public RechargeItemAdapter(Context context, List<RechargeRecordBean.ListBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_recharge_detail, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        RechargeRecordBean.ListBean bean = mList.get(i);

        return view;
    }


    static class ViewHolder {
        @Bind(R.id.img_cover)
        ImageView imgCover;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_time2)
        TextView tvTime2;
        @Bind(R.id.tv_recharge_state)
        TextView tvRechargeState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
