package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.RechargeRecordBean;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/11/23.
 */

public class RechargeDetailAdapter extends BaseQuickAdapter<RechargeRecordBean,BaseViewHolder>{

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private LinearLayout layout;

    public RechargeDetailAdapter(@LayoutRes int layoutResId, @Nullable List<RechargeRecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeRecordBean item) {
        helper.setText(R.id.tv_time,item.getMonth());
        layout = helper.getView(R.id.layout);
        layout.removeAllViews();
        for (int i = 0; i < item.getList().size(); i++) {
            addView(item.getList().get(i));
        }

    }

    private void addView(RechargeRecordBean.ListBean bean){
        View view = View.inflate(mContext,R.layout.item_recharge_detail,null);
        ViewHolder holder = new ViewHolder(view);
        holder.tvTitle.setText("充值" + bean.getPay_money() + "元");

        String pay_type = bean.getPay_type();
        if (pay_type.equals("alipay")) {
            holder.imgCover.setImageResource(R.mipmap.zhifubao2);
        } else {
            holder.imgCover.setImageResource(R.mipmap.weixin2);
        }

        int status = bean.getStatus();
        if (status == 0) {
            holder.tvRechargeState.setText("充值失败");
        } else if (status == 1) {
            holder.tvRechargeState.setText("充值成功");
        }

        String time = this.format.format(bean.getAdd_time()*1000);
        holder.tvTime2.setText(time);

        layout.addView(view);

    }
    static class ViewHolder {
        @Bind(R.id.img_cover)
        ImageView imgCover;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_time2)
        TextView tvTime2;
        @Bind(R.id.tv_recharge_state)
        TextView tvRechargeState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
