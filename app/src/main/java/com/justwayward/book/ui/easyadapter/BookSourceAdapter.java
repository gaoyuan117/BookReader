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
package com.justwayward.book.ui.easyadapter;

import android.content.Context;
import android.view.ViewGroup;

import com.justwayward.book.R;
import com.justwayward.book.bean.BookSource;
import com.justwayward.book.bean.SourceBean;
import com.justwayward.book.view.LetterView;
import com.justwayward.book.view.recyclerview.adapter.BaseViewHolder;
import com.justwayward.book.view.recyclerview.adapter.RecyclerArrayAdapter;

/**
 * 查询
 *
 * @author yuyh.
 * @date 16/9/3.
 */
public class BookSourceAdapter extends RecyclerArrayAdapter<SourceBean.DataBean> {


    public BookSourceAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<SourceBean.DataBean>(parent, R.layout.item_book_source) {
            @Override
            public void setData(SourceBean.DataBean item) {
                holder.setText(R.id.tv_source_title, item.getSite_name())
                        .setText(R.id.tv_source_content, item.getSite_url());

//                LetterView letterView = holder.getView(R.id.letter_view);
//                letterView.setText(item.host);
            }
        };
    }
}
