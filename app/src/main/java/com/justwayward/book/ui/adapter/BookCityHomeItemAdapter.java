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
 * Created by gaoyuan on 2017/11/17.
 */

public class BookCityHomeItemAdapter extends BaseAdapter {
    private Context context;
    private List<BookCityBean.ListBean> mList;

    public BookCityHomeItemAdapter(Context context, List<BookCityBean.ListBean> mList) {
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
            view = View.inflate(context, R.layout.item_book_city_home_type1, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BookCityBean.ListBean listBean = mList.get(i);

        holder.tv_author.setText(listBean.getAuthor());
        holder.tv_name.setText(listBean.getTitle());

        Glide.with(context).load(listBean.getPic()).error(R.drawable.cover_default).into(holder.imgItemBookCity);

        return view;
    }

    static class ViewHolder {
        @Bind(R.id.img_item_book_city)
        ImageView imgItemBookCity;

        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_author)
        TextView tv_author;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
