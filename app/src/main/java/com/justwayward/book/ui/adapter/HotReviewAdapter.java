/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.view.View;

import com.justwayward.book.R;
import com.justwayward.book.base.Constant;
import com.justwayward.book.bean.BookDetailBean;
import com.justwayward.book.common.OnRvItemClickListener;
import com.justwayward.book.utils.FormatUtils;
import com.justwayward.book.view.XLHRatingBar;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * @author lfh.
 * @date 16/8/6.
 */
public class HotReviewAdapter extends EasyRVAdapter<BookDetailBean.CommentListBean> {
    private OnRvItemClickListener itemClickListener;

    public HotReviewAdapter(Context context, List<BookDetailBean.CommentListBean> list, OnRvItemClickListener listener) {
        super(context, list, R.layout.item_book_detai_hot_review_list);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final BookDetailBean.CommentListBean item) {
        holder.setCircleImageUrl(R.id.ivBookCover, Constant.IMG_BASE_URL + item.getAvatar(), R.drawable.avatar_default)
                .setText(R.id.tvBookTitle, item.getUser_nickname())
//                .setText(R.id.tvBookType, String.format(mContext.getString(R.string
//                        .book_detail_user_lv), item.get))
                .setText(R.id.tvTitle, item.getTitle())
                .setText(R.id.tvContent, String.valueOf(item.getComment()))
                .setText(R.id.tvHelpfulYes, String.valueOf(item.getPraise_num()))
                .setText(R.id.tvTime, FormatUtils.getDescriptionTimeFromDateString(item.getAdd_time()+"000"));
        XLHRatingBar ratingBar = holder.getView(R.id.rating);
        ratingBar.setCountSelected(item.getScore());
        holder.setOnItemViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });
    }

}