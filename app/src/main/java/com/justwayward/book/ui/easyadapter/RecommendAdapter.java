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
import com.justwayward.book.bean.BookShelfBean;
import com.justwayward.book.utils.FormatUtils;
import com.justwayward.book.view.recyclerview.adapter.BaseViewHolder;
import com.justwayward.book.view.recyclerview.adapter.RecyclerArrayAdapter;

/**
 * @author yuyh.
 * @date 2016/9/7.
 */
public class RecommendAdapter extends RecyclerArrayAdapter<BookShelfBean.DataBean> {

    public RecommendAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<BookShelfBean.DataBean>(parent, R.layout.item_recommend_list) {
            @Override
            public void setData(final BookShelfBean.DataBean item) {
                super.setData(item);
//                String latelyUpdate = "";
//                if (!TextUtils.isEmpty(FormatUtils.getDescriptionTimeFromDateString(item.get))) {
//                    latelyUpdate = FormatUtils.getDescriptionTimeFromDateString(item.updated) + ":";
//                }

                holder.setText(R.id.tvRecommendTitle, item.getTitle())
                        .setText(R.id.tvLatelyUpdate, FormatUtils.getDescriptionTimeFromDateString(item.getChapter_info().getAdd_time()+"000"))
                        .setText(R.id.tvRecommendShort, item.getChapter_info().getChapter());
//                        .setVisible(R.id.ivTopLabel, item.isTop)
//                        .setVisible(R.id.ckBoxSelect, item.showCheckBox)
//                        .setVisible(R.id.ivUnReadDot, FormatUtils.formatZhuiShuDateString(item.updated)
//                                .compareTo(item.recentReadingTime) > 0);
                holder.setRoundImageUrl(R.id.ivRecommendCover, item.getPic(), R.drawable.cover_default);
//                if (item.path != null && item.path.endsWith(Constant.SUFFIX_PDF)) {
//                    holder.setImageResource(R.id.ivRecommendCover, R.drawable.ic_shelf_pdf);
//                } else if (item.path != null && item.path.endsWith(Constant.SUFFIX_EPUB)) {
//                    holder.setImageResource(R.id.ivRecommendCover, R.drawable.ic_shelf_epub);
//                } else if (item.path != null && item.path.endsWith(Constant.SUFFIX_CHM)) {
//                    holder.setImageResource(R.id.ivRecommendCover, R.drawable.ic_shelf_chm);
//                } else if (item.isFromSD) {
//                    holder.setImageResource(R.id.ivRecommendCover, R.drawable.ic_shelf_txt);
//                    long fileLen = FileUtils.getChapterFile(item._id, 1).length();
//                    if (fileLen > 10) {
//                        double progress = ((double) SettingManager.getInstance().getReadProgress(item._id)[2]) / fileLen;
//                        NumberFormat fmt = NumberFormat.getPercentInstance();
//                        fmt.setMaximumFractionDigits(2);
//                        holder.setText(R.id.tvRecommendShort, "当前阅读进度：" + fmt.format(progress));
//                    }
//                } else if (!SettingManager.getInstance().isNoneCover()) {
//                    holder.setRoundImageUrl(R.id.ivRecommendCover, Constant.IMG_BASE_URL + item.cover,
//                            R.drawable.cover_default);
//                } else {
//                    holder.setImageResource(R.id.ivRecommendCover, R.drawable.cover_default);
//                }

//                CheckBox ckBoxSelect = holder.getView(R.id.ckBoxSelect);
//                ckBoxSelect.setChecked(item.isSeleted);
//                ckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView,
//                                                 boolean isChecked) {
//                        item.isSeleted = isChecked;
//                    }
//                });
            }
        };
    }

}
