package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.FeedBackBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/12/6.
 */

public class FeedbackAdapter extends BaseQuickAdapter<FeedBackBean, BaseViewHolder> {
    private int p = -1;

    public FeedbackAdapter(@LayoutRes int layoutResId, @Nullable List<FeedBackBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedBackBean item) {
        TextView textView = helper.getView(R.id.tv_title);
        if (p == helper.getPosition()) {
            textView.setSelected(true);
        } else {
            textView.setSelected(false);
        }
        textView.setText(item.getType_name());
    }

    public void setPosition(int position) {
        p = position;
        notifyDataSetChanged();
    }
}


//    private Context context;
//    private List<FeedBackBean> list;
//
//    private int p = -1;
//
//    public FeedbackAdapter(Context context, List<FeedBackBean> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder;
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_feed, null);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        if (p == position) {
//            viewHolder.tvTitle.setSelected(true);
//        } else {
//            viewHolder.tvTitle.setSelected(false);
//        }
//
//        viewHolder.tvTitle.setText(list.get(position).getType_name());
//
//
//        return convertView;
//    }
//
//    public void setPosition(int position) {
//        p = position;
//        notifyDataSetChanged();
//    }
//
//static class ViewHolder {
//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//
//    ViewHolder(View view) {
//        ButterKnife.bind(this, view);
//    }
//}
