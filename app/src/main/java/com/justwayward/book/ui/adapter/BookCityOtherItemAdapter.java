package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justwayward.book.R;
import com.justwayward.book.bean.BookCityBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/11/19.
 */

public class BookCityOtherItemAdapter extends BaseAdapter {
    private Context context;
    private List<BookCityBean.ListBean> mList;

    public BookCityOtherItemAdapter(Context context, List<BookCityBean.ListBean> mList) {
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
            view = View.inflate(context, R.layout.item_sub_category_list, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BookCityBean.ListBean bean = mList.get(i);

        Glide.with(context)
                .load(bean.getPic())
                .error(R.drawable.cover_default)
                .into(holder.ivSubCateCover);

        holder.tvSubCateTitle.setText(bean.getTitle());
        holder.tvSubCateShort.setText( bean.getDesc());
        String percent = "0";
        if (bean.getCollect_num() != 0 && bean.getView_num() != 0) {
            double d = (bean.getCollect_num() * 100 / bean.getView_num());
            percent = String.format("%.0f", d);
        }

        holder.tvSubCateMsg.setText( String.format(context.getResources().getString(R.string.category_book_msg), bean.getView_num(), percent));

        holder.tvSubCateAuthor.setText((bean.getAuthor() == null ? "未知" : bean.getAuthor()) + " | " + (bean.getLabels() == null ? "未知" : bean.getLabels()));

        return view;
    }


    class ViewHolder {
        @Bind(R.id.ivSubCateCover)
        ImageView ivSubCateCover;
        @Bind(R.id.tvSubCateTitle)
        TextView tvSubCateTitle;
        @Bind(R.id.tvSubCateAuthor)
        TextView tvSubCateAuthor;
        @Bind(R.id.tvSubCateShort)
        TextView tvSubCateShort;
        @Bind(R.id.tvSubCateMsg)
        TextView tvSubCateMsg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
