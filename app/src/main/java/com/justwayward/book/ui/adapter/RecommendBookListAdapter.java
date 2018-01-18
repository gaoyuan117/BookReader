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
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.base.Constant;
import com.justwayward.book.bean.BookDetailBean;
import com.justwayward.book.common.OnRvItemClickListener;
import com.justwayward.book.manager.SettingManager;
import com.justwayward.book.utils.FormatUtils;
import com.justwayward.book.utils.NoDoubleClickListener;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lfh.
 * @date 16/8/7.
 */
public class RecommendBookListAdapter extends EasyRVAdapter<BookDetailBean.RecommendListBean> {

    private OnRvItemClickListener itemClickListener;

    public RecommendBookListAdapter(Context context, List<BookDetailBean.RecommendListBean> list,
                                    OnRvItemClickListener listener) {
        super(context, list, R.layout.item_book_detail_like);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindData(final EasyRVHolder holder, final int position, final BookDetailBean.RecommendListBean item) {
        if (!SettingManager.getInstance().isNoneCover()) {
            holder.setRoundImageUrl(R.id.ivBookListCover,item.getPic(), R.drawable.cover_default);
        }

        TextView tvNum = holder.getView(R.id.tvWordCount);
        if (item.getWord_num()<10000){
            tvNum.setText(item.getWord_num()+"字");
        }else {
            BigDecimal old = new BigDecimal(item.getWord_num() * 0.0001 ).setScale(1, BigDecimal.ROUND_HALF_UP);

            tvNum.setText(old+"万字");
        }

        holder.setText(R.id.tvBookListTitle, item.getTitle())
                .setText(R.id.tvBookListAuthor, item.getAuthor()+" | ")
                .setText(R.id.tvCatgory, item.getCategory_name()+" | ")
                .setText(R.id.time, FormatUtils.getDescriptionTimeFromDateString(item.getAdd_time()+"000"));

        holder.setOnItemViewClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                itemClickListener.onItemClick(holder.getItemView(), position, item);
            }
        });
    }

}