package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.bean.MonthPackageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/11/21.
 */

public class MyVipPayAdapter extends BaseAdapter {
    private Context context;
    private List<MonthPackageBean> mList;
    private boolean isSelect = false;
    private List<CheckBox> list = new ArrayList<>();

    public MyVipPayAdapter(Context context, List<MonthPackageBean> mList) {
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
            view = View.inflate(context, R.layout.item_my_pay_vip, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
            list.add(holder.cb);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MonthPackageBean bean = mList.get(i);

        holder.tvTitle.setText(bean.getMonth() + "个月会员 " + bean.getMoney());

        return view;
    }

    public void setAllFalse() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.cb_wx_pay)
        CheckBox cb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
