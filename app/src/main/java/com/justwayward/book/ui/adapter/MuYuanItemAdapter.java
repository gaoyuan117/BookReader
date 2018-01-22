package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.bean.BookCityBean;
import com.justwayward.book.bean.MyYuanListBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2018/1/22.
 */

public class MuYuanItemAdapter extends BaseAdapter {
    private Context context;
    private List<MyYuanListBean.SourceBean> mList;

    public MuYuanItemAdapter(Context context, List<MyYuanListBean.SourceBean> mList) {
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
            view = View.inflate(context, R.layout.item_yuan_layout, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MyYuanListBean.SourceBean bean = mList.get(i);
        holder.tvYuanUrl.setText(bean.getUrl());


        return view;
    }


    static class ViewHolder {
        @Bind(R.id.tv_yuan_url)
        TextView tvYuanUrl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
