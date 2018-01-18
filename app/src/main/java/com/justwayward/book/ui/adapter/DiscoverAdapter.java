package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.DiscoverBean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/20.
 */

public class DiscoverAdapter extends BaseQuickAdapter<DiscoverBean, BaseViewHolder> {

    public DiscoverAdapter(@LayoutRes int layoutResId, @Nullable List<DiscoverBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverBean item) {
        ImageView img = helper.getView(R.id.img_icon);
        Log.e("GY", "item:" + item.getZone_name());
        if (item.getZone_name().equals("排行榜")) {
            img.setImageResource(R.mipmap.paihang_discover);
        } else if (item.getZone_name().equals("会员专区")) {
            img.setImageResource(R.mipmap.huiyuan_discover);
        } else {
            Glide.with(mContext).load(item.getIcon()).error(R.drawable.cover_default).into(img);
        }

        helper.setText(R.id.tv_name, item.getZone_name());


    }
}
