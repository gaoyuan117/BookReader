package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.view.View;

import com.justwayward.book.R;
import com.justwayward.book.bean.BookDetailBean;
import com.justwayward.book.common.OnRvItemClickListener;
import com.justwayward.book.manager.SettingManager;
import com.justwayward.book.utils.NoDoubleClickListener;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * Created by gaoyuan on 2018/1/10.
 */

public class LikeBooksListAdapter extends EasyRVAdapter<BookDetailBean.RecommendListBean> {

    private OnRvItemClickListener itemClickListener;

    public LikeBooksListAdapter(Context context, List<BookDetailBean.RecommendListBean> list,
                                    OnRvItemClickListener listener) {
        super(context, list, R.layout.item_book_city_home_type1);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final BookDetailBean.RecommendListBean item) {
        if (!SettingManager.getInstance().isNoneCover()) {
            holder.setRoundImageUrl(R.id.img_item_book_city, item.getPic(), R.drawable.cover_default);
        }

        holder.setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_author, item.getAuthor());
//                .setText(R.id.tvCatgory, item.getCategory_name())
//                .setText(R.id.tvWordCount, "数字")
//                .setText(R.id.time, FormatUtils.getDescriptionTimeFromDateString(item.getAdd_time()+"000"));

        holder.setOnItemViewClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });

    }
}
