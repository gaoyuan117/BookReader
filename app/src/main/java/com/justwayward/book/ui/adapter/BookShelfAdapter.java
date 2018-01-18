package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.BookShelfBean;
import com.justwayward.book.ui.fragment.BookShelfFragment;
import com.justwayward.book.utils.FormatUtils;
import com.yuyh.easyadapter.glide.GlideRoundTransform;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/15.
 */

public class BookShelfAdapter extends BaseQuickAdapter<BookShelfBean.DataBean, BaseViewHolder> {

    private boolean isShowCheck;
    private BookShelfFragment fragment;

    public BookShelfAdapter(@LayoutRes int layoutResId, @Nullable List<BookShelfBean.DataBean> data, BookShelfFragment fragment) {
        super(layoutResId, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(final BaseViewHolder helper, BookShelfBean.DataBean item) {
        TextView tvAd  = helper.getView(R.id.tvAd);
        if (item.getType() != null && item.getType().equals("ad")) {
            tvAd.setVisibility(View.VISIBLE);
            ImageView mIvBookCover = helper.getView(R.id.ivRecommendCover);
            Glide.with(mContext)
                    .load(item.getPic())
                    .placeholder(R.drawable.cover_default)
                    .transform(new GlideRoundTransform(mContext))
                    .into(mIvBookCover);

            helper.setText(R.id.tvRecommendTitle, item.getTitle())
                    .setText(R.id.tvLatelyUpdate, item.getDesc())
                    .setText(R.id.tvRecommendShort, "");
        } else {
            tvAd.setVisibility(View.GONE);
            helper.setText(R.id.tvRecommendTitle, item.getTitle())
                    .setText(R.id.tvLatelyUpdate, FormatUtils.getDescriptionTimeFromDateString(item.getRead_time() + "000"))
                    .setText(R.id.tvRecommendShort, "：" + item.getChapter_info().getChapter());

            ImageView tvUpdate = helper.getView(R.id.ivUnReadDot);

            if (item.getIs_update()==0){
                tvUpdate.setVisibility(View.GONE);
            }else {
                tvUpdate.setVisibility(View.VISIBLE);
            }

            //图片
            ImageView mIvBookCover = helper.getView(R.id.ivRecommendCover);
            Glide.with(mContext)
                    .load(item.getPic())
                    .placeholder(R.drawable.cover_default)
                    .transform(new GlideRoundTransform(mContext))
                    .into(mIvBookCover);
            ImageView ivTopLabel = helper.getView(R.id.ivTopLabel);

            //是否置顶
            int is_top = item.getIs_top();
            if (is_top == 0) {
                ivTopLabel.setVisibility(View.GONE);
            } else {
                ivTopLabel.setVisibility(View.VISIBLE);
            }
        }

        //复选框
        CheckBox checkBox = helper.getView(R.id.ckBoxSelect);
        if (isShowCheck && TextUtils.isEmpty(item.getType())) {

            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                fragment.list.set(helper.getAdapterPosition(), cb.isChecked());
            }
        });

        if (fragment.list.size() > 0) {
            checkBox.setChecked(fragment.list.get(helper.getAdapterPosition()));
        }


    }

    /**
     * 是否显示复选框
     *
     * @param b
     */
    public void setCheckShow(boolean b) {
        isShowCheck = b;
    }

}
