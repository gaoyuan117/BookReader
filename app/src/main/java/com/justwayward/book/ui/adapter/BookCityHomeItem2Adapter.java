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

public class BookCityHomeItem2Adapter extends BaseAdapter {
    private Context context;
    private List<BookCityBean.ListBean> mList;

    public BookCityHomeItem2Adapter(Context context, List<BookCityBean.ListBean> mList) {
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
            view = View.inflate(context, R.layout.item_book_city_home_type2, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BookCityBean.ListBean listBean = mList.get(i);

        Glide.with(context).load(listBean.getPic()).error(R.drawable.cover_default).into(holder.imgCover);

        holder.tvAuthor.setText(listBean.getAuthor());
        holder.tvTitle.setText(listBean.getTitle());
        holder.tvContent.setText(listBean.getDesc());
        holder.tvNum.setText(listBean.getWord_num()+"");
        holder.tvType.setText(listBean.getLabels());
        return view;
    }


    static class ViewHolder {
        @Bind(R.id.img_cover)
        ImageView imgCover;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.tv_type)
        TextView tvType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
